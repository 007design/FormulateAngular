package com.lcp.formulate.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sourceforge.stripes.util.Log;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Notification;
import com.lcp.formulate.entities.ormlite.Recipient;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.Value;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.entities.ormlite.ViewField;

public class EmailSender {
	private static final Log log = Log.getInstance(EmailSender.class);
	
	public static void sendNotifications(Notification notification, Submission submission) throws NamingException {
		log.debug("Email subject: "+notification.getSubject());
		
		Context env = (Context)new InitialContext().lookup("java:comp/env");
				
		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", env.lookup("EmailServer"));
		props.put("mail.smtp.auth", "false");

		// Get session
		Session session = Session.getDefaultInstance(props, null);
		
		// Init velocity
		Velocity.init();
		VelocityContext context = new VelocityContext();

		JdbcConnectionSource conn = null;
		try {
			conn = EntityManager.getConnection();
			Dao<User, String> userDao = DaoManager.createDao(conn, User.class);
			Dao<View, String> viewDao = DaoManager.createDao(conn, View.class);
			Dao<ViewField, String> viewFieldDao = DaoManager.createDao(conn, ViewField.class);

			// Add the user objects to the context
			context.put("submission-id", submission.getId().toString() );
			
			// Add the user objects to the context
			context.put("submission-user", userDao.queryForId( submission.getUser().getId().toString() ));
			
			// Add the view name to the context
			context.put("submission-formName", viewDao.queryForId( submission.getView().getId().toString() ).getName());
			
			// Add the submission timestamp
//			context.put("submission-timestamp", DateFormat.getDateTimeInstance().format( submission.getTimestamp() ));
			
			// Parse each viewfield and add it to the context
			for (ViewField viewField : submission.getView().getViewFields()) {
				String value = "";
				
				// Build the camelCaseName of the viewfield
				StringBuffer sb = new StringBuffer();
				
				ViewField vf = viewFieldDao.queryForId(viewField.getId().toString());
				String label = vf.getLabel();
				
//				System.err.println("stripped: "+label.toLowerCase().replaceAll("[^a-z|0-9| ]", " "));
				
				String[] bits = label.toLowerCase().replaceAll("[^a-z|0-9| ]", "").replaceAll(" +", " ").split(" ");
				
				for (int a=0; a<bits.length; a++) {
					if (a==0)
						sb.append(bits[a]);
					else
						sb.append(Character.toUpperCase(bits[a].charAt(0)) + bits[a].substring(1));
				}
				
				
				// Add each submission value to the context using camelCased viewfield name for key
				// Only process submission values with a value and a viewfield
				for (Value v : submission.getValues()) {
					if (v != null)
						if (v.getValue() == null || v.getViewField() == null) {
							continue;				
						} else if (v.getViewField().getId() == viewField.getId()) {
							/**
							 * Format upload field values
							 */
							if (viewField.getFieldType().getId().equals(6)) {
								try {
									String[] fileBits = v.getValue().replaceAll("[\"|\\{|\\}]", "").split(":");
									value = "<a href=\"http://webapps.lakecountypress.com/formulate/download/"+fileBits[0]+"\">"+fileBits[1]+"</a>";
								} catch (Exception x) {}
							} else {							
								value = v.getValue();
							}
							break;
						}
				}
	
				context.put("field-"+sb.toString(), value);
//				System.err.println("field-"+sb.toString());
			}
			
			Properties velocityProps = new Properties();
			velocityProps.setProperty("resource.loader", "string");
			velocityProps.setProperty("string.resource.loader.description", "Velocity StringResource loader");
			velocityProps.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
			
			VelocityEngine ve = new VelocityEngine();
			ve.init(velocityProps);
			
			StringResourceRepository repo = StringResourceLoader.getRepository();
	
			repo.putStringResource("subject.vm", notification.getSubject());
			repo.putStringResource("body.vm", notification.getBody());
	
			// Render the email body
			Template subjectTemplate = ve.getTemplate("subject.vm");
			Template bodyTemplate = ve.getTemplate("body.vm");
			
			Writer writer = new StringWriter();
			subjectTemplate.merge(context, writer);
			String emailSubject = writer.toString();
			
			writer = new StringWriter();
			bodyTemplate.merge(context, writer);
			String emailBody = writer.toString();
			
			PreparedQuery<User> su = userDao.queryBuilder().where()
				.eq("accounts_id", submission.getView().getAccount())
				.and().isNotNull("users_systemUser").prepare();
			
			System.err.println(su.getStatement());
			User systemUser = userDao.queryForFirst( su ); 
			
			System.err.println("su id: "+systemUser.getEmail());
							
			List<Recipient> recipients = new ArrayList<Recipient>(notification.getRecipients());
			/**
			 * If the notification is marked to send to the user
			 * Add the submitting user to the recipients array
			 */
			if (notification.getEmailUser()) {
				Recipient r = new Recipient();
				User user = userDao.queryForId(submission.getUser().getId().toString());
				r.setEmail(user.getEmail());
				recipients.add(r);
			}
			
			// Parse each recipient
			for (Recipient r : recipients) {	
				
				/**
				 * Validate the email address format
				 */
				String emailAddress = null;
				
				if (EmailValidator.getInstance().isValid(r.getEmail()))
					emailAddress = r.getEmail();
				
				/**
				 * Try getting the email address from the submission
				 */
				else {
					log.debug("Email '"+emailAddress+"' is invalid. Trying to get from context. -"+context.get(r.getEmail())+"-");
					if (context.get(r.getEmail()) != null)
						emailAddress = (String)context.get(r.getEmail());
				}
				
				if (emailAddress == null || !EmailValidator.getInstance().isValid(r.getEmail()))
					log.error("Invalid recipient email: '"+r.getEmail()+"' in notification ID: "+notification.getId());
				
				else {				
					try {				
						log.debug("Sending notification email to "+r.getEmail());
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress( systemUser.getEmail() ));
						message.addRecipient(Message.RecipientType.TO, new InternetAddress( emailAddress ));
						message.setSubject(emailSubject);
						message.setContent(emailBody, notification.getFormat() == 1 ? "text/plain":"text/html");
						
						Transport.send(message);
					} catch (Exception x) {
						x.printStackTrace();
					}
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}

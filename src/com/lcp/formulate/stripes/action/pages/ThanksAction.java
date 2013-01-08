package com.lcp.formulate.stripes.action.pages;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.Value;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.entities.ormlite.ViewField;
import com.lcp.formulate.stripes.action.BaseActionBean;

@UrlBinding("/thankyou/{account}/{view}/{submission}")
public class ThanksAction extends BaseActionBean {
	static Logger log = org.apache.log4j.Logger.getLogger(ThanksAction.class);
	
//	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
		
//	@Validate(required=true)
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
		
//	@Validate(required=true)
	private Submission submission;
	public Submission getSubmission() { return submission; }
	public void setSubmission(Submission submission) { this.submission = submission; }
		
	@DefaultHandler
	public Resolution show() {

		log.debug("[ThanksAction] - User requesting thank you");
		log.debug("[ThanksAction] - Account: "+getAccount().getAlias());
		log.debug("[ThanksAction] - View: "+getView().getAlias());
		log.debug("[ThanksAction] - Submission: "+getSubmission().getId());
		
		/**
		 * Build the path to the thank you html
		 */
		String path = getContext().getServletContext().getInitParameter("ViewFiles")+
			"/"+getAccount().getAlias()+"/"+getView().getAlias();
		
		Properties velocityProps = new Properties();
		velocityProps.setProperty("resource.loader", "file");
		velocityProps.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		velocityProps.setProperty("file.resource.loader.path", path);
		
		VelocityEngine ve = new VelocityEngine(velocityProps);
		Template template = ve.getTemplate("thankyou.html");
		VelocityContext context = new VelocityContext();
		
		/**
		 * Try to populate the context
		 */
		JdbcConnectionSource conn = null;
		try {
			conn = EntityManager.getConnection();
			Dao<User, String> userDao = DaoManager.createDao(conn, User.class);
			Dao<View, String> viewDao = DaoManager.createDao(conn, View.class);
			Dao<ViewField, String> viewFieldDao = DaoManager.createDao(conn, ViewField.class);

			// Add the user objects to the context
			context.put("submission-id", getSubmission().getId().toString() );
			
			// Add the user objects to the context
			context.put("submission-user", userDao.queryForId( submission.getUser().getId().toString() ));
			
			// Add the view name to the context
			context.put("submission-formName", viewDao.queryForId( submission.getView().getId().toString() ).getName());
			
			// Parse each viewfield and add it to the context
			for (ViewField viewField : submission.getView().getViewFields()) {
				String value = "";
				
				// Build the camelCaseName of the viewfield
				StringBuffer sb = new StringBuffer();
				
				ViewField vf = viewFieldDao.queryForId(viewField.getId().toString());
				String label = vf.getLabel();
				
				System.err.println("stripped: "+label.toLowerCase().replaceAll("[^a-z|0-9| ]", " "));

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
							value = v.getValue();
							break;
						}
				}

				context.put("field-"+sb.toString(), value);
				System.err.println("field-"+sb.toString());
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try { conn.close(); } catch (Exception x) {}
		}
				
		Writer writer = new StringWriter();
		template.merge(context, writer);
		String html = writer.toString();
		
		Resolution resolution = new StreamingResolution("text/html", html);
		return resolution;
	}
	
}

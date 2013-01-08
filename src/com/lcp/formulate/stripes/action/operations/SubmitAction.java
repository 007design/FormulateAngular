package com.lcp.formulate.stripes.action.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.naming.NamingException;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.EventCondition;
import com.lcp.formulate.entities.ormlite.Notification;
import com.lcp.formulate.entities.ormlite.NotificationCondition;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.UserView;
import com.lcp.formulate.entities.ormlite.Value;
import com.lcp.formulate.entities.ormlite.ValueSet;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.entities.ormlite.ViewEvent;
import com.lcp.formulate.entities.ormlite.ViewField;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;
import com.lcp.formulate.util.EmailSender;

@UrlBinding("/submit/{account}/{view}")
public class SubmitAction extends BaseActionBean implements AjaxAction {
	private static final Log log = Log.getInstance(SubmitAction.class);
	
	private Submission submission;
	public void setSubmission(Submission submission) { this.submission = submission; }
	public Submission getSubmission() { return submission; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void intercept() throws Exception {
		log.info("SubmitInterceptor.intercept()");
		
		/**
		 * Get the action bean
		 */
		MyActionBeanContext ctx = (MyActionBeanContext)getContext();

		log.info("    Event is "+ctx.getEventName());

		try {
			ViewTypeConverter vtc = new ViewTypeConverter(getAccount());
			View view = vtc.convert(	
					ctx.getRequest().getParameter("view"), 
					View.class, 
					new ArrayList<ValidationError>());
			setView( view );
			log.info("    Set view to "+view.getId());
		} catch (Exception x) {
			log.info("    Exception getting view: "+ctx.getRequest().getParameter("view"));
			throw x;
		}
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = ctx.getRequest().getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("submission json: "+jb.toString());
		
		try {	
			Gson gson = new GsonBuilder().serializeNulls().create();
			
			setSubmission( gson.fromJson(jb.toString(), Submission.class) );
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Prevent access by the system user
		 */
		if (ctx.getUser() != null) 
			if (ctx.getUser().getSystemUser() != null)
				throw new AjaxException(this, ctx.getEventName(), "Invalid User");

		/**
		 * View requires login
		 */
		if (getView().getRequireUser()) {
			/**
			 * If the user is not logged in
			 */
			if (ctx.getUser() == null)
				throw new AjaxException(this, getContext().getEventName(), "Login Required");

			ConnectionSource conn = null;
			try {	
				conn = EntityManager.getConnection();
				/**
				 * Check if the user may access this view
				 * Get their userviews
				 */
				Dao<UserView, String> uvDao = DaoManager.createDao(conn, UserView.class);
				PreparedQuery<UserView> p = uvDao.queryBuilder().where()
				.eq("views_id", getView().getId()).and()
				.eq("users_id", ctx.getUser().getId().toString()).prepare();

				log.info(p.getStatement());

				UserView uv = uvDao.queryForFirst( p );

				if (uv == null)
					throw new AjaxException(this, getContext().getEventName(), "Access Denied");

			} catch (SQLException x) {
				x.printStackTrace();
			} catch (NamingException e) {
				e.printStackTrace();
			} finally {
				try { conn.close(); } catch (Exception x) {}
			}
			
			getSubmission().setUser( ctx.getUser() );
		}

		/**
		 * View does not require login, set the submission user to the system user
		 */
		else {
			log.info("    Setting default user");
			getSubmission().setUser( 
					EntityManager.getSystemUser( getView()) );
		}
	}
	
	@DontBind
	@DefaultHandler
	public Resolution submit() {
		boolean isNew = getSubmission().getId() == null;
		log.info((isNew?"CREATING NEW":"UPDATING")+" SUBMISSION by "+getContext().getUser().getName());

		ConnectionSource conn = null;
		
		try {
			conn = EntityManager.getConnection();
			final Dao<Submission, String> subDao = DaoManager.createDao(conn, Submission.class);
			final Dao<View, String> viewDao = DaoManager.createDao(conn, View.class);
			final Dao<ViewField, String> viewFieldDao = DaoManager.createDao(conn, ViewField.class);
//			final Dao<User, String> userDao = DaoManager.createDao(conn, User.class);
			final Dao<ValueSet, String> valSetDao = DaoManager.createDao(conn, ValueSet.class);
			final Dao<Value, String> valDao = DaoManager.createDao(conn, Value.class);
			
			log.info("Begin transaction");
			
			/**
			 * Begin a transaction to insert the new submission
			 */
			try {
				TransactionManager.callInTransaction(conn, 
					new Callable<Void>(){
						public Void call() throws Exception {
							try {
								CreateOrUpdateStatus status = subDao.createOrUpdate(getSubmission());
								
								log.info(status.isCreated() ? "created: "+getSubmission().getId() : "updated: "+getSubmission().getId());
							} catch (Exception x) {
								log.info("Failed, user id: "+getSubmission().getUser().getId());
								getSubmission().setUser(getContext().getUser());
								CreateOrUpdateStatus status = subDao.createOrUpdate(getSubmission());
								
								log.info(status.isCreated() ? "created2: "+getSubmission().getId() : "updated2: "+getSubmission().getId());
							}
							
							// Create a new ValueSet
							ValueSet valSet = new ValueSet();
							valSet.setSubmission(getSubmission());
							valSet.setUser(getContext().getUser());
							valSetDao.create(valSet);
	
							for (Value v : getSubmission().getValues()) {
								if (v != null) {
									if (v.getValue() != null && v.getViewField() != null) {
										v.setValueSet(valSet);
										v.setSubmission(getSubmission());
										v.setField(viewFieldDao.queryForId(v.getViewField().getId().toString()).getField());
										valDao.create(v);
									}
								}
							}
													
							return null;
						}
				});
			} catch (Exception x) {
				log.error("transaction error", x);
				x.printStackTrace();
			}
			
			log.info("Transaction Complete. Parsing uploads");
					
			try {
				// Check for any attachments in the uploaded values
				for (Value v : getSubmission().getValues()) {
					if (v != null) {
						if (v.getValue() != null && v.getViewField() != null) {
							ViewField vf = viewFieldDao.queryForId(v.getViewField().getId().toString());
							if (vf.getFieldType().getId()==6) {
								log.info("    parsing upload field: "+vf.getLabel());
								
								String tmpPath = getContext().getServletContext().getInitParameter("UploadTempLocation");
								String savePath = getContext().getServletContext().getInitParameter("SubmissionUploadLocation");
								
								// Parse the json string into a map
								Type mapType = new TypeToken<Map<String, String>>() {}.getType();
								Map<String, String> json = new Gson().fromJson(v.getValue(), mapType);
								
								for (Map.Entry<String, String> entry : json.entrySet()) {
									String uid = entry.getKey();
									String fn = entry.getValue();
									String filename = uid + "." + FilenameUtils.getExtension(fn);
									
									log.info("    uid: "+uid+" / fn: "+fn);
									
									try {
										// Move the attachment file from the temp directory to the submission uploads directory
										FileUtils.moveFile(
											new File(tmpPath, filename), 
											new File(savePath+"/"+getSubmission().getId()+"/"+filename) );
										
									} catch (IOException e) {
										e.printStackTrace();
										log.error("error moving file", e);
									}	
								}
							}
						}
					}
				}
			} catch (Exception x) {
				log.error("uploads error", x);
			}
						
			/**
			 * Retrieve the view from the database for processing events and notifications
			 */
			View theView = viewDao.queryForId( getSubmission().getView().getId().toString() );
			getSubmission().setView(theView);

			log.info("Processing Events");
			/**
			 * Parse view events
			 * Do this before the notifications so the event values will trigger notifications
			 */		
			for (ViewEvent viewEvent : theView.getEvents()) {
				try {
					log.info("Parsing event: "+viewEvent.getId());
					boolean doEvent = true;
					
					// Reject non-initial events on new submission
					// Reject non-initial/update only notifications on new submission
					if (isNew) {
						log.info("New submission: "+getSubmission().getId()+" event type: "+viewEvent.getInitial());
						if (viewEvent.getInitial() == Notification.UPDATE)
							doEvent = false;
					} else {
						log.info("Update submission: "+getSubmission().getId()+" event type: "+viewEvent.getInitial());
						// Reject initial / non-update only on non-new submissions
						if (viewEvent.getInitial() == Notification.INITIAL)
							doEvent = false;
					}
					
					log.info("  do event after checking init? "+doEvent);
					
					if (doEvent) {
						// Verify event conditions
						for (EventCondition cond : viewEvent.getEventConditions()) {
							// Check each value in the submission, find the one with the same field as the condition target
							
							log.info("Valueset size: "+getSubmission().getValues().size());
							for (Value value : getSubmission().getValues()) {
								if (value != null) {
									if (value.getValue() != null) {
										if (value.getField().getId() == cond.getTarget().getId()) {
											log.info("condition val: "+cond.getValue());
											log.info("value val: "+value.getValue());
											switch (cond.getComparison()) {
												case 1: doEvent = cond.getValue().equalsIgnoreCase(value.getValue()) ? true : (value.getValue().length()>0 && cond.getValue().equals("*")); 
													break;
												case 2: doEvent = !cond.getValue().equalsIgnoreCase(value.getValue());
													break;
											}
										}
									}
								}
							}
							if (!doEvent)
								break;
						}
					}
					log.info("  do event after checking conds? "+doEvent);
					
					// Execute the event
					if (doEvent) {
						try {
							User sysUser = EntityManager.getSystemUser(getView());						
							
							// Create new valueset
							ValueSet valSet = new ValueSet();
							valSet.setSubmission(getSubmission());
							valSet.setUser(sysUser);
							valSetDao.createOrUpdate(valSet);
							
							// Create new value
							Value val = new Value();
							val.setField(viewEvent.getTarget());
							val.setSubmission(getSubmission());
							val.setValue(viewEvent.getValue());
							val.setValueSet(valSet);
							valDao.createOrUpdate(val);
						} catch (Exception x) {
							log.error("event error", x);
							x.printStackTrace();
						}
					}
	
				} catch (Exception x) {
					log.info("Exception while processing event "+viewEvent.getId());
					x.printStackTrace();
				}
			}
			
			log.info("Processing Notifications");
			
			/**
			 * Parse notifications
			 */
			for (Notification notification : theView.getNotifications()) {
				try {
					boolean send = true;
					
					// Reject non-initial/update only notifications on new submission
					if (isNew) {
						log.info("Notification is update only, submission is new");
						if (notification.getInitial() == Notification.UPDATE)
							send = false;
					} else {
						log.info("Notification is initial only, submission is update");
						// Reject initial / non-update only on non-new submissions
						if (notification.getInitial() == Notification.INITIAL)
							send = false;
					}
					
					
					if (send) {
						log.info("Parsing notification conditions");
						// Verify notification conditions
						for (NotificationCondition nc : notification.getNotificationConditions()) {
							log.info("Evaluating condition: "+nc.getTarget().getName() + " : " + nc.getComparison() + " : " + nc.getValue());
							for (Value value : getSubmission().getValues()) {
								send = false;
								if (value != null) {
									log.info("Comparing value: "+value.getValue());
									if (value.getViewField() != null) {
										if (value.getField().getId().equals(nc.getTarget().getId())) {
											log.info("Matched IDs: "+value.getField().getId());
											switch (nc.getComparison()) {
												case 1: 
													send = nc.getValue().equals("*") || nc.getValue().equalsIgnoreCase(value.getValue());
													break;
												case 2: 
													send = !nc.getValue().equalsIgnoreCase(value.getValue());
													break;
												default: send = false;
											}
										}
									}
								}
								if (send)
									break;
							}
							if (!send)
								break;
						}
					}

					
					if (send) {
						log.info("Notification conditions passed");
						try {
							EmailSender.sendNotifications(notification, getSubmission());
						} catch (Exception x) {
							log.error(x);
						}
					} else {
						log.info("Notification conditions failed");
					}
				} catch (Exception x) {
					x.printStackTrace();
					log.info("Exception processing notification "+notification.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException x) {}
		}
		
		return new StreamingResolution("text/json","{\"success\":true, \"id\":\""+getSubmission().getId()+"\"}");
	}
}

package com.lcp.formulate.stripes.action.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.EventCondition;
import com.lcp.formulate.entities.ormlite.Filter;
import com.lcp.formulate.entities.ormlite.Notification;
import com.lcp.formulate.entities.ormlite.NotificationCondition;
import com.lcp.formulate.entities.ormlite.Recipient;
import com.lcp.formulate.entities.ormlite.Trigger;
import com.lcp.formulate.entities.ormlite.TriggerCondition;
import com.lcp.formulate.entities.ormlite.TriggerValue;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.entities.ormlite.ViewEvent;
import com.lcp.formulate.entities.ormlite.ViewField;
import com.lcp.formulate.entities.ormlite.ViewOption;
import com.lcp.formulate.entities.ormlite.VisibilityCondition;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.action.data.ViewDataAction;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.util.TriggerDao;

@UrlBinding("/save/view/{account}")
public class SaveViewAction extends BaseActionBean implements AjaxAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SaveViewAction.class);
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return this.view; }

	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
		
	@DefaultHandler
	public Resolution save() {
		log.info("save/view");
		ConnectionSource conn = null;
		
		try {
			conn = EntityManager.getConnection();
			final Dao<View, String> viewDao = DaoManager.createDao(conn, View.class);
			
			final Dao<ViewField, String> vfDao = DaoManager.createDao(conn, ViewField.class);
			final Dao<ViewOption, String> optDao = DaoManager.createDao(conn, ViewOption.class);
			
			final Dao<Trigger, String> tDao = new TriggerDao(conn, Trigger.class);
			final Dao<TriggerValue, String> tvDao = DaoManager.createDao(conn, TriggerValue.class);
			final Dao<TriggerCondition, String> tcDao = DaoManager.createDao(conn, TriggerCondition.class);
			
			final Dao<Notification, String> nDao = DaoManager.createDao(conn, Notification.class);
			final Dao<Recipient, String> rDao = DaoManager.createDao(conn, Recipient.class);
			final Dao<NotificationCondition, String> ncDao = DaoManager.createDao(conn, NotificationCondition.class);
			
			final Dao<Filter, String> fDao = DaoManager.createDao(conn, Filter.class);
			
			final Dao<ViewEvent, String> veDao = DaoManager.createDao(conn, ViewEvent.class);
			final Dao<EventCondition, String> ecDao = DaoManager.createDao(conn, EventCondition.class);
			
			final Dao<VisibilityCondition, String> vcDao = DaoManager.createDao(conn, VisibilityCondition.class);
						
			/**
			 * Create a transaction to save the view
			 */
			TransactionManager.callInTransaction(conn, 
				new Callable<Void>(){
					public Void call() throws Exception {
						
					/**
					 * Save the view
					 */
					try {
						CreateOrUpdateStatus status = viewDao.createOrUpdate(getView());
						
						if (status.isCreated())
							createDirectories();
						
					} catch (SQLException x) {
						log.info("SAVE VIEW FAILED!!!");
						throw x;
					}
					
					/**
					 * Save the view fields
					 */
					try {
						if (getView().getViewFields() != null) {
							for (ViewField oldVf : vfDao.queryForEq("views_id", getView().getId())) {
								if (!getView().getViewFields().contains(oldVf))
									vfDao.delete(oldVf);
							}
							for (ViewField vf : getView().getViewFields()) {
								vf.setView(getView());
								vfDao.createOrUpdate(vf);
								
								/**
								 * Save the viewoptions
								 */
								try {
									if (vf.getViewOptions() != null) {
										for (ViewOption oldOpt : optDao.queryForEq("viewfields_id", vf.getId())) {
											if (!vf.getViewOptions().contains(oldOpt))
												optDao.delete(oldOpt);
										}
										for (ViewOption opt : vf.getViewOptions()) {
											opt.setViewField(vf);
											optDao.createOrUpdate(opt);
										}
									}
								} catch (Exception x) {
									log.info("SAVE VIEWOPTIONS FAILED!!!");
									throw x;
								}
								
								/**
								 * Save the triggers
								 */
								try  {
									if (vf.getTriggers() != null) {
										for (Trigger old : tDao.queryForEq("viewfields_id", vf.getId())) {
											if (!vf.getTriggers().contains(old))
												tDao.delete(old);
										}
										for (Trigger t : vf.getTriggers()) {
											t.setViewField(vf);
											tDao.createOrUpdate(t);
											
											/**
											 * Save the trigger values
											 */
											try {
												if (t.getTriggerValues() != null) {
													for (TriggerValue old : tvDao.queryForEq("triggers_id", t.getId())) {
														if (!t.getTriggerValues().contains(old))
															tvDao.delete(old);
													}
													for (TriggerValue opt : t.getTriggerValues()) {
														opt.setTrigger(t);
														tvDao.createOrUpdate(opt);
													}
												}
											} catch (Exception x) {
												log.info("SAVE TRIGGERVALUE FAILED!!!");
												throw x;
											}
											
											/**
											 * Save trigger conditions
											 */
											try {
												if (t.getTriggerConditions() != null) {
													for (TriggerCondition old : tcDao.queryForEq("triggers_id", t.getId())) {
														if (!t.getTriggerConditions().contains(old))
															tcDao.delete(old);
													}
													for (TriggerCondition opt : t.getTriggerConditions()) {
														opt.setTrigger(t);
														tcDao.createOrUpdate(opt);
													}
												}
											} catch (Exception x) {
												log.info("SAVE TRIGGERCONDITION FAILED!!!");
												throw x;
											}
										}
									}
								} catch (Exception x) {
									log.info("SAVE TRIGGER FAILED!!!");
									throw x;
								}
								
								/**
								 * Save Visibility Conditions
								 */
								try {
									if (vf.getVisibilityConditions() != null) {
										for (VisibilityCondition old : vcDao.queryForEq("viewfields_id", vf.getId())) {
											if (!vf.getVisibilityConditions().contains(old))
												vcDao.delete(old);
										}
										for (VisibilityCondition t : vf.getVisibilityConditions()) {
											t.setViewField(vf);
											vcDao.createOrUpdate(t);
										}
									}
								} catch (Exception x) {
									log.info("SAVE VISIBILITY CONDITION FAILED!!!");
									throw x;
								}
							}
						}
					} catch (Exception x) {
						log.info("SAVE VIEWFIELD FAILED!!!");
						throw x;
					}
		
					/**
					 * Save the notifications
					 */
					try {
						if (getView().getNotifications() != null) {
							for (Notification old : nDao.queryForEq("views_id", getView().getId())) {
								if (!getView().getNotifications().contains(old))
									nDao.delete(old);
							}
							for (Notification n : getView().getNotifications()) {
								n.setView(getView());
								nDao.createOrUpdate(n);
								
								if (n.getRecipients() != null) {
									for (Recipient r : rDao.queryForEq("notifications_id", n.getId())) {
										if (!n.getRecipients().contains(r))
											rDao.delete(r);
									}
									for (Recipient opt : n.getRecipients()) {
										opt.setNotification(n);
										rDao.createOrUpdate(opt);
									}
								}
	
								if (n.getNotificationConditions() != null) {
									for (NotificationCondition nc : ncDao.queryForEq("notifications_id", n.getId())) {
										if (!n.getNotificationConditions().contains(nc))
											ncDao.delete(nc);
									}
									for (NotificationCondition opt : n.getNotificationConditions()) {
										opt.setNotification(n);
										ncDao.createOrUpdate(opt);
									}
								}
							}
						}
					} catch (Exception x) {
						log.info("SAVE NOTIFICATIONS FAILED!!!");
						throw x;
					}
			
					/**
					 * Save the filters
					 */
					try {
						if (getView().getFilters() != null) {
							for (Filter old : fDao.queryForEq("views_id", getView().getId())) {
								if (!getView().getFilters().contains(old))
									fDao.delete(old);
							}
							for (Filter opt : getView().getFilters()) {
								opt.setView(getView());
								fDao.createOrUpdate(opt);
							}
						}
					} catch (Exception x) {
						log.info("SAVE FILTERS FAILED!!!");
						throw x;
					}
		
					/**
					 * Save the events
					 */
					try {
						if (getView().getEvents() != null) {
							for (ViewEvent old : veDao.queryForEq("views_id", getView().getId())) {
								if (!getView().getEvents().contains(old))
									veDao.delete(old);
							}
							for (ViewEvent opt : getView().getEvents()) {
								opt.setView(getView());
								veDao.createOrUpdate(opt);
	
								if (opt.getEventConditions() != null) {
									for (EventCondition old : ecDao.queryForEq("viewevents_id", opt.getId())) {
										if (!opt.getEventConditions().contains(old))
											ecDao.delete(old);
									}
									for (EventCondition ec : opt.getEventConditions()) {
										ec.setViewEvent(opt);
										ecDao.createOrUpdate(ec);
									}
								}
							}
						}
					} catch (Exception x) {
						log.info("SAVE EVENTS FAILED!!!");
						throw x;
					}
					
					return null;
				}
			});
			
			
		} catch(Exception x) {
			x.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("invalid save target"));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}
		
		RedirectResolution resolution = new RedirectResolution(ViewDataAction.class);
		resolution.addParameter("view", getView().getId());
		return resolution;
	}
	
	@Before(stages=LifecycleStage.BindingAndValidation)
	public void saveViewInterceptor() throws AjaxException {		
		if (getContext().getUser() == null)
			throw new AjaxException(this, getContext().getEventName(), "Access Denied");
		
		if (!getContext().getUser().getAdmin())
			throw new AjaxException(this, getContext().getEventName(), "Access Denied");
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = getContext().getRequest().getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info(jb.toString());
		
		try {
			setView( new Gson().fromJson(jb.toString(), View.class) );
			
			log.info("View Deserialized");
		} catch (Exception e) {
			log.info("View Deserialization Failed");
			e.printStackTrace();
			getContext().getValidationErrors().add("view", new SimpleError("Save Failed"));
		}
	}
	
	private void createDirectories() {
		String dirPath = getContext().getServletContext().getInitParameter("ViewFiles");
		
		File viewDir = new File(dirPath+"/"+getAccount().getAlias()+"/"+getView().getAlias());	
		
		try {			
			viewDir.mkdir();
		} catch (Exception x) {
			x.printStackTrace();
			log.error("Error creating view directory", x);
		}
				
		try {
			File thanksFile = new File(viewDir, "thankyou.html");
			thanksFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error creating thankyou.html", e);
		}
		
		try {
			File scriptFile = new File(viewDir, "scripts.js");
			scriptFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error creating scripts.js", e);
		}
				
		try {
			File styleFile = new File(viewDir, "styles.css");
			styleFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error creating styles.css", e);
		}
	}
}

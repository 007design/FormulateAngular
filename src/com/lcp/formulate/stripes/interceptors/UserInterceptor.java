//package com.lcp.formulate.stripes.interceptors;
//
//import java.io.BufferedReader;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import javax.naming.NamingException;
//
//import net.sourceforge.stripes.action.Resolution;
//import net.sourceforge.stripes.controller.ExecutionContext;
//import net.sourceforge.stripes.controller.Interceptor;
//import net.sourceforge.stripes.controller.Intercepts;
//import net.sourceforge.stripes.controller.LifecycleStage;
//import net.sourceforge.stripes.validation.ValidationError;
//
//import org.apache.log4j.Logger;
//
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.dao.DaoManager;
//import com.j256.ormlite.stmt.PreparedQuery;
//import com.j256.ormlite.support.ConnectionSource;
//import com.lcp.formulate.entities.EntityManager;
//import com.lcp.formulate.entities.ormlite.Account;
//import com.lcp.formulate.entities.ormlite.User;
//import com.lcp.formulate.entities.ormlite.UserView;
//import com.lcp.formulate.entities.ormlite.View;
//import com.lcp.formulate.stripes.action.AjaxAction;
//import com.lcp.formulate.stripes.action.operations.SubmitAction;
//import com.lcp.formulate.stripes.action.pages.EditViewAction;
//import com.lcp.formulate.stripes.action.pages.LoginAction;
//import com.lcp.formulate.stripes.action.pages.ManagerAction;
//import com.lcp.formulate.stripes.action.pages.PartialAction;
//import com.lcp.formulate.stripes.action.pages.RegisterAction;
//import com.lcp.formulate.stripes.action.pages.ViewAction;
//import com.lcp.formulate.stripes.exceptions.AjaxException;
//import com.lcp.formulate.stripes.exceptions.LoginException;
//import com.lcp.formulate.stripes.exceptions.RegisterException;
//import com.lcp.formulate.stripes.extensions.MyActionBeanContext;
//import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
//import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;
//
//@Intercepts(value=LifecycleStage.BindingAndValidation)
//public class UserInterceptor implements Interceptor {
//	private static Logger log = org.apache.log4j.Logger.getLogger(UserInterceptor.class);
//
//	@Override
//	public Resolution intercept(ExecutionContext context) throws Exception {
//		log.info("UserInterceptor.intercept()");
//		
//		/**
//		 * Get the action bean
//		 */
//		MyActionBeanContext ctx = (MyActionBeanContext) context.getActionBeanContext();
//		
//		/**
//		 * If the actionBean is a LoginAction
//		 */
//		if (context.getActionBean() instanceof LoginAction) {
//			LoginAction action = (LoginAction)context.getActionBean();
//			log.info("    Action is LoginAction."+action.getContext().getEventName()+" - Doing type conversion");			
//			AccountTypeConverter atc = new AccountTypeConverter();
//						
//			/**
//			 * Do Type Conversion
//			 */
//			try {
//				Account account = atc.convert(
//						ctx.getRequest().getParameter("account"), 
//						Account.class, 
//						new ArrayList<ValidationError>());
//				action.setAccount( account );
//				log.info("    Set account to "+account.getId());
//			} catch (Exception x) {
//				x.printStackTrace();
//				throw x;
//			}
//			
//			Resolution resolution = context.proceed();
//			
//			return resolution;
//		}
//		
//		/**
//		 * Register Action
//		 */
//		else if (context.getActionBean() instanceof RegisterAction) {
//			RegisterAction action = (RegisterAction)context.getActionBean();
//			log.info("    Action is RegisterAction."+action.getContext().getEventName()+" - Doing type conversion");			
//			AccountTypeConverter atc = new AccountTypeConverter();
//						
//			/**
//			 * Do Type Conversion
//			 */
//			try {
//				Account account = atc.convert(
//						ctx.getRequest().getParameter("account"), 
//						Account.class, 
//						new ArrayList<ValidationError>());
//				action.setAccount( account );
//				log.info("    Set account to "+account.getId());
//			} catch (Exception x) {
//				x.printStackTrace();
//				throw x;
//			}
//			
//			try {
//				ViewTypeConverter vtc = new ViewTypeConverter(action.getAccount());
//				View view = vtc.convert(	
//						ctx.getRequest().getParameter("view"), 
//						View.class, 
//						new ArrayList<ValidationError>());
//				action.setView( view );
//				log.info("    Set view to "+view.getId());
//			} catch (Exception x) {
//				log.info("    Exception getting view: "+ctx.getRequest().getParameter("view"));
//			}
//
//			action.setNextPage( ctx.getRequest().getParameter("nextPage"));
//			action.setEmail( ctx.getRequest().getParameter("email"));
//			action.setUsername( ctx.getRequest().getParameter("username"));
//			action.setPassword( ctx.getRequest().getParameter("password"));
//			
//			Resolution resolution = context.proceed();
//			
//			return resolution;
//		}
//		
//		/**
//		 * If the actionBean is a ViewAction subclass:
//		 * Account and View are required
//		 */
//		else if (context.getActionBean() instanceof ViewAction) {
//			log.info("    ViewAction requested");
//			ViewAction action = (ViewAction) context.getActionBean();
//			log.info("    Resolved action, creating AccountTypeConverter");			
//			AccountTypeConverter atc = new AccountTypeConverter();
//			log.info("    AccountTypeConverter created.");
//			
//			log.info("    Doing type conversion");
//			
//			/**
//			 * Do Type Conversion
//			 */
//			try {
//				Account account = atc.convert(
//						ctx.getRequest().getParameter("account"), 
//						Account.class, 
//						new ArrayList<ValidationError>());
//				action.setAccount( account );
//				log.info("    Set account to "+account.getId());
//			} catch (Exception x) {
//				log.info("    Exception getting view: "+ctx.getRequest().getParameter("account"));
//				x.printStackTrace();
//				throw x;
//			}
//
//			try {
//				ViewTypeConverter vtc = new ViewTypeConverter(action.getAccount());
//				View view = vtc.convert(	
//						ctx.getRequest().getParameter("view"), 
//						View.class, 
//						new ArrayList<ValidationError>());
//				action.setView( view );
//				log.info("    Set view to "+view.getId());
//			} catch (Exception x) {
//				log.info("    Exception getting view: "+ctx.getRequest().getParameter("view"));
//			}
//			
//			/**
//			 * Do User Access Validation
//			 */
//			log.info("    Doing access validation");
//			
//			/**
//			 * Editor Views
//			 */
//			if (context.getActionBean() instanceof EditViewAction) {
//				log.info("    action is EditViewAction");
//				if (ctx.getUser() == null)
//					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Login Required");
//				else if (!ctx.getUser().getAccount().getId().equals(action.getAccount().getId()))
//					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Invalid Account");
//				else if (!ctx.getUser().getAdmin())
//					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Insufficient Privileges");
//			}
//			
//			/**
//			 * View requires a user
//			 */
//			else if (action.getView().getRequireUser()) {
//				log.info("    User required");
//
//				ConnectionSource conn = null;
//				try {
//					conn = EntityManager.getConnection();
//					/**
//					 * User is not logged in
//					 */
//					if (ctx.getUser() == null) {
//						log.info("    User not logged in");
//						
//						/**
//						 * View requires a password
//						 * URL and anonymous access is denied on passworded views
//						 * If we're checking for a user it means that they're not already logged in so should be forwarded to the login page
//						 */
//						if (action.getView().getRequirePassword())
//							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Password Required");
//		
//						try {
//							/**
//							 * Check for username in the querystring
//							 */
//							String username = ctx.getRequest().getParameter("username");
//							log.info("Query string username is "+username);							
//							
//							if (username == null)
//								throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Username required");
//			
//							/**
//							 * Try locating the user in the db
//							 */
//							Dao<User, String> userDao = DaoManager.createDao(conn, User.class);
//							User user = userDao.queryForFirst(
//								userDao.queryBuilder().where()
//								.eq("accounts_id", action.getAccount().getId()).and()
//								.eq("users_name", username).prepare() );
//		
//							/**
//							 * If the user exists in the db
//							 */
//							if (user != null) {			
//								log.info("User exists in db");	
//								/**
//								 * Check if they have a userview
//								 */
//								Dao<UserView, String> userViewDao = DaoManager.createDao(conn, UserView.class);
//								UserView userView = userViewDao.queryForFirst( 
//									userViewDao.queryBuilder().where()
//									.eq("users_id", user.getId())
//									.and().eq("views_id", action.getView().getId()).prepare() );
//								
//								/**
//								 * If they don't have a userview and the view has createUser enabled create a userView
//								 */
//								if (userView == null && action.getView().getCreateUser()) {
//									log.info("Creating userView");
//									userView = new UserView();
//									userView.setUser(user);
//									userView.setView(action.getView());
//									userViewDao.create(userView);
//								}
//								
//								/**
//								 * If they don't have a userview and the view does not have createUser enabled
//								 */
//								else if (userView == null && !action.getView().getCreateUser())
//									throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - You do not have permission to access this page.");
//								
//								/**
//								 * Set the user
//								 */
//								ctx.setUser(user);
//								ctx.setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_USER);
//								log.info("User set to "+user.getId());
//							}
//								
//							/**
//							 * If the user doesn't exist and the view does not have createUser enabled
//							 * Throw a LoginException
//							 */
//							else if (user == null && !action.getView().getCreateUser()) {
//								log.info("    Create user not enabled and user "+username+" not found.");
//								throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Userview not found.");
//							}
//			
//							/**
//							 * User doesn't exist but the view DOES have create user enabled
//							 */
//							else if (user == null && action.getView().getCreateUser()) {
//								log.info("    User "+username+" doesn't exist but view has createUser enabled, redirect to RegisterAction");
//								throw new RegisterException(context.getActionBean(), action.getAccount(), action.getView(), "Registration Required");
//							}
//			
//							/**
//							 * User should have been logged in by the block above
//							 */
////							if (ctx.getUser() == null)
////								throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Login Exception");									
//						} catch (SQLException x) {
//							x.printStackTrace();
//							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), x.getMessage());
//						}
//					}
//						
//					/**
//					 * User is already logged in
//					 */
//					else {
//						log.info("User is already logged in");
//						
//						/**
//						 * Check that, if the view requires a password, they logged in with more than just their username
//						 */
//						if (ctx.getAccessLevel() < MyActionBeanContext.ACCESS_LEVEL_PASSWORD && action.getView().getRequirePassword())
//							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Insufficient Access Level ["+ctx.getAccessLevel()+"]");
//						
//						/**
//						 * Check if the user may access this view
//						 * Get their userviews
//						 */
//						Dao<UserView, String> uvDao = DaoManager.createDao(conn, UserView.class);
//						UserView userView = uvDao.queryForFirst( 
//							uvDao.queryBuilder().where()
//							.eq("views_id", action.getView().getId()).and()
//							.eq("users_id", ctx.getUser().getId().toString()).prepare() );
//		
//						/**
//						 * If they don't have a userview and the view has createUser enabled create a userView
//						 */
//						if (userView == null && action.getView().getCreateUser()) {
//							log.info("Creating userView");
//							userView = new UserView();
//							userView.setUser(ctx.getUser());
//							userView.setView(action.getView());
//							uvDao.create(userView);
//						}
//						
//						/**
//						 * If they don't have a userview and the view does not have createUser enabled
//						 */
//						else if (userView == null && !action.getView().getCreateUser())
//							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - You do not have permission to access this page.");								
//					}
//				} catch (NamingException x) {
//					x.printStackTrace();
//					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), x.getMessage());
//				} catch (SQLException e) {
//					e.printStackTrace();
//					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), e.getMessage());
//				} finally {
//					try { conn.close(); } catch (Exception x) {}
//				}
//			}
//			
//			/**
//			 * View does not require a user
//			 */
//			else {
//				log.info("User not required");
//			}
//		}
//		
//		/**
//		 * ActionBean is a SubmitAction
//		 */
//		else if (context.getActionBean() instanceof SubmitAction) {
//			log.debug("[UserInterceptor] - User submitting form");
//			SubmitAction action = (SubmitAction)context.getActionBean();
//			
//			/**
//			 * Do Type Conversion
//			 */		
//			AccountTypeConverter atc = new AccountTypeConverter();
//			try {
//				Account account = atc.convert(
//						ctx.getRequest().getParameter("account"), 
//						Account.class, 
//						new ArrayList<ValidationError>());
//				action.setAccount( account );
//				log.info("    Set account to "+account.getId());
//			} catch (Exception x) {
//				log.info("    Exception getting view: "+ctx.getRequest().getParameter("account"));
//				x.printStackTrace();
//				throw x;
//			}
//
//			try {
//				ViewTypeConverter vtc = new ViewTypeConverter(action.getAccount());
//				View view = vtc.convert(	
//						ctx.getRequest().getParameter("view"), 
//						View.class, 
//						new ArrayList<ValidationError>());
//				action.setView( view );
//				log.info("    Set view to "+view.getId());
//			} catch (Exception x) {
//				log.info("    Exception getting view: "+ctx.getRequest().getParameter("view"));
//			}
//			
//			/**
//			 * Prevent access by the system user
//			 */
//			if (ctx.getUser() != null) 
//				if (ctx.getUser().getSystemUser() != null)
//					throw new AjaxException(context.getActionBean(), ctx.getEventName(), "Invalid User");
//					
//			/**
//			 * View requires login
//			 */
//			if (action.getView().getRequireUser()) {
//				/**
//				 * If the user is not logged in
//				 */
//				if (ctx.getUser() == null)
//					throw new AjaxException(context.getActionBean(), action.getContext().getEventName(), "Login Required");
//
//				ConnectionSource conn = null;
//				try {	
//					conn = EntityManager.getConnection();
//					/**
//					 * Check if the user may access this view
//					 * Get their userviews
//					 */
//					Dao<UserView, String> uvDao = DaoManager.createDao(conn, UserView.class);
//					PreparedQuery<UserView> p = uvDao.queryBuilder().where()
//						.eq("views_id", action.getView().getId()).and()
//						.eq("users_id", ctx.getUser().getId().toString()).prepare();
//					
//					log.debug(p.getStatement());
//					
//					UserView uv = uvDao.queryForFirst( p );
//
//					if (uv == null)
//						throw new AjaxException(context.getActionBean(), action.getContext().getEventName(), "Access Denied");
//					
//				} catch (SQLException x) {
//					x.printStackTrace();
//				} catch (NamingException e) {
//					e.printStackTrace();
//				} finally {
//					try { conn.close(); } catch (Exception x) {}
//				}
//			}
//			
//			/**
//			 * View does not require login, set the submission user to the system user
//			 */
//			else {
//				log.debug("[UserInterceptor] - Setting default user");
//				getSubmission().setUser( 
//						EntityManager.getSystemUser(
//						getView()) );
//			}
//			
//			log.debug("Made it thru SubmitAction.userInterceptor()");
//		}
//
//		/**
//		 * If the actionBean is an AjaxAction subclass:
//		 * Account is required
//		 */
//		else if (context.getActionBean() instanceof AjaxAction) {
//			log.info("actionBean is AjaxAction");
//			
//			AjaxAction action = (AjaxAction) context.getActionBean();
//			AccountTypeConverter atc = new AccountTypeConverter();
//										
//			/**
//			 * Do Type Conversion
//			 */
//			try {
//				action.setAccount( atc.convert(
//					ctx.getRequest().getParameter("account"), 
//					Account.class, 
//					new ArrayList<ValidationError>()) );
//			} catch (Exception x) {
//				x.printStackTrace();
//				throw x;
//			}
//		}	
//		
//		/**
//		 * Handle ManagerAction
//		 */
//		else if (context.getActionBean() instanceof ManagerAction) {
//			ManagerAction action = (ManagerAction)context.getActionBean();
//			if (ctx.getUser() == null)
//				throw new LoginException(context.getActionBean(), action.getAccount(), null, "User Access Denied");
//		}
//		
//		/**
//		 * Handle PartialAction
//		 */
//		else if (context.getActionBean() instanceof PartialAction) {
////			if (ctx.getAccessLevel()<MyActionBeanContext.ACCESS_LEVEL_READ_ONLY)
////				throw new AjaxException(context.getActionBean(), "partial", "User Access Denied");
//		}
//		
//		else {
//			log.info("action is something else: "+context.getActionBean().getClass().getCanonicalName());					
//		}
//
//		return context.proceed();		
//	}		
//}

package com.lcp.formulate.stripes.interceptors;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.UserView;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.pages.ViewAction;
import com.lcp.formulate.stripes.exceptions.LoginException;
import com.lcp.formulate.stripes.exceptions.RegisterException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;

@Intercepts(value=LifecycleStage.BindingAndValidation)
public class ViewInterceptor implements Interceptor {
	private static Logger log = org.apache.log4j.Logger.getLogger(ViewInterceptor.class);

	@Override
	public Resolution intercept(ExecutionContext context) throws Exception {
		if (context.getActionBean() instanceof ViewAction) {	

			log.info("ViewInterceptor.intercept()");
	
			/**
			 * Get the action bean
			 */
			MyActionBeanContext ctx = (MyActionBeanContext) context.getActionBeanContext();

			log.info("    Event is "+ctx.getEventName());
			
			ViewAction action = (ViewAction) context.getActionBean();			
			AccountTypeConverter atc = new AccountTypeConverter();
	
			/**
			 * Do Type Conversion
			 */
			try {
				Account account = atc.convert(
						ctx.getRequest().getParameter("account"), 
						Account.class, 
						new ArrayList<ValidationError>());
				action.setAccount( account );
				log.info("    Set account to "+account.getId());
			} catch (Exception x) {
				log.info("    Exception getting account: "+ctx.getRequest().getParameter("account"));
				x.printStackTrace();
				throw x;
			}
	
			try {
				ViewTypeConverter vtc = new ViewTypeConverter(action.getAccount());
				View view = vtc.convert(	
						ctx.getRequest().getParameter("view"), 
						View.class, 
						new ArrayList<ValidationError>());
				action.setView( view );
				log.info("    Set view to "+view.getId());
			} catch (Exception x) {
				log.info("    Exception getting view: "+ctx.getRequest().getParameter("view"));
				throw x;
			}
	
			/**
			 * Do User Access Validation
			 */
			log.info("    Doing access validation");
	
			/**
			 * View requires a user
			 */
			if (action.getView().getRequireUser()) {
				log.info("    User required");
	
				ConnectionSource conn = null;
				try {
					conn = EntityManager.getConnection();
					/**
					 * User is not logged in
					 */
					if (ctx.getUser() == null) {
						log.info("    User not logged in");
	
						/**
						 * View requires a password
						 * URL and anonymous access is denied on passworded views
						 * If we're checking for a user it means that they're not already logged in so should be forwarded to the login page
						 */
						if (action.getView().getRequirePassword())
							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Password Required");
	
						try {
							/**
							 * Check for username in the querystring
							 */
							String username = ctx.getRequest().getParameter("username");
							log.info("    Query string username is "+username);							
	
							if (username == null)
								throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Username required");
	
							/**
							 * Try locating the user in the db
							 */
							Dao<User, String> userDao = DaoManager.createDao(conn, User.class);
							User user = userDao.queryForFirst(
									userDao.queryBuilder().where()
									.eq("accounts_id", action.getAccount().getId()).and()
									.eq("users_name", username).prepare() );
	
							/**
							 * If the user exists in the db
							 */
							if (user != null) {			
								log.info("User exists in db");	
								/**
								 * Check if they have a userview
								 */
								Dao<UserView, String> userViewDao = DaoManager.createDao(conn, UserView.class);
								UserView userView = userViewDao.queryForFirst( 
										userViewDao.queryBuilder().where()
										.eq("users_id", user.getId())
										.and().eq("views_id", action.getView().getId()).prepare() );
	
								/**
								 * If they don't have a userview and the view has createUser enabled create a userView
								 */
								if (userView == null && action.getView().getCreateUser()) {
									log.info("Creating userView");
									userView = new UserView();
									userView.setUser(user);
									userView.setView(action.getView());
									userViewDao.create(userView);
								}
	
								/**
								 * If they don't have a userview and the view does not have createUser enabled
								 */
								else if (userView == null && !action.getView().getCreateUser())
									throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - You do not have permission to access this page.");
	
								/**
								 * Set the user
								 */
								ctx.setUser(user);
								ctx.setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_USER);
								log.info("User set to "+user.getId());
							}
	
							/**
							 * If the user doesn't exist and the view does not have createUser enabled
							 * Throw a LoginException
							 */
							else if (user == null && !action.getView().getCreateUser()) {
								log.info("    Create user not enabled and user "+username+" not found.");
								throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Userview not found.");
							}
	
							/**
							 * User doesn't exist but the view DOES have create user enabled
							 */
							else if (user == null && action.getView().getCreateUser()) {
								log.info("    User "+username+" doesn't exist but view has createUser enabled, redirect to RegisterAction");
								throw new RegisterException(context.getActionBean(), action.getAccount(), action.getView(), "Registration Required");
							}
	
							/**
							 * User should have been logged in by the block above
							 */
							//					if (ctx.getUser() == null)
							//						throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Login Exception");									
						} catch (SQLException x) {
							x.printStackTrace();
							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), x.getMessage());
						}
					}
	
					/**
					 * User is already logged in
					 */
					else {
						log.info("User is already logged in");
	
						/**
						 * Check that, if the view requires a password, they logged in with more than just their username
						 */
						if (ctx.getAccessLevel() < MyActionBeanContext.ACCESS_LEVEL_PASSWORD && action.getView().getRequirePassword())
							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - Insufficient Access Level ["+ctx.getAccessLevel()+"]");
	
						/**
						 * Check if the user may access this view
						 * Get their userviews
						 */
						Dao<UserView, String> uvDao = DaoManager.createDao(conn, UserView.class);
						UserView userView = uvDao.queryForFirst( 
								uvDao.queryBuilder().where()
								.eq("views_id", action.getView().getId()).and()
								.eq("users_id", ctx.getUser().getId().toString()).prepare() );
	
						/**
						 * If they don't have a userview and the view has createUser enabled create a userView
						 */
						if (userView == null && action.getView().getCreateUser()) {
							log.info("Creating userView");
							userView = new UserView();
							userView.setUser(ctx.getUser());
							userView.setView(action.getView());
							uvDao.create(userView);
						}
	
						/**
						 * If they don't have a userview and the view does not have createUser enabled
						 */
						else if (userView == null && !action.getView().getCreateUser())
							throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), "Access Denied - You do not have permission to access this page.");								
					}
				} catch (NamingException x) {
					x.printStackTrace();
					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), x.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LoginException(context.getActionBean(), action.getAccount(), action.getView(), e.getMessage());
				} finally {
					try { conn.close(); } catch (Exception x) {}
				}
			}
	
			/**
			 * View does not require a user
			 */
			else {
				log.info("User not required");
			}
		}
		
		Resolution resolution = context.proceed();

		return resolution;
	}
}

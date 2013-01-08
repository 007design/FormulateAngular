package com.lcp.formulate.stripes.action.operations;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
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
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.action.pages.ManagerAction;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.exceptions.RegisterException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;

@UrlBinding("/do/register")
public class RegisterAction extends BaseActionBean {
	private static Logger log = org.apache.log4j.Logger.getLogger(RegisterAction.class);
	
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }

	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	private String username;
	public void setUsername(String username) { this.username = username; }
	public String getUsername() { return this.username; }
	
	private String password;	
	public void setPassword(String password) { this.password = password; }
	public String getPassword() { return this.password; }
	
	private String email;
	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	private String nextPage;
	public void setNextPage(String nextPage) { this.nextPage = nextPage; }
	public String getNextPage() { return nextPage; }
	
	public Resolution register() throws AjaxException, RegisterException {
		log.info("RegisterAction.register() - Creating new user / userview");

//		Object nextPage = getContext().getRequest().getSession().getAttribute("nextPage");
		
		ConnectionSource conn = null;
		try {
			conn = EntityManager.getConnection();
			
			/**
			 * Check that the view allows creating users
			 */			
			Dao<View, String> vDao = DaoManager.createDao(conn, View.class);
			View view = vDao.queryForId(getView().getId().toString());
			
			if (!view.getCreateUser()) {
				log.info("    View does not allow creating new users");
				throw new AjaxException(this, "userView", "Create User Disabled");
			}
			
			Dao<UserView, String> uvDao = DaoManager.createDao(conn, UserView.class);			
			Dao<User, String> uDao = DaoManager.createDao(conn, User.class);

			/**
			 * Try to create a user and userview
			 * If this fails there is likely already a user with the same name and account
			 */
			try {			
				log.info("    Creating new user in system: "+getAccount().getAlias());
				
				User user = new User();
				user.setAccount(getAccount());
				user.setEmail(getEmail());
				user.setName(getUsername());
				user.setPassword(getPassword());
				
				uDao.create(user);
				
				log.info("    Creating new userview in for: "+getView().getAlias());
				
				UserView userView = new UserView();
				userView.setUser(user);
				userView.setView(getView());
				
				uvDao.create(userView);
				
				getContext().setUser(user);
				getContext().setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_PASSWORD);
				
				/**
				 * User exists but nextPage was not specified.
				 * Redirect to the manager page
				 */
				if (getContext().getUser() != null && nextPage == null) {
					log.info("    User exists but nextpage is null - redirect to ManagerAction");
					RedirectResolution resolution = new RedirectResolution(ManagerAction.class).includeRequestParameters(true);
					return resolution;
				}

				log.info("    Login successful - redirect to "+nextPage);
				
				RedirectResolution resolution = new RedirectResolution(getNextPage());
				return resolution;
				
			} catch (SQLException x) {
				log.error("RegisterException",x);
				
				return new ErrorResolution(HttpServletResponse.SC_PRECONDITION_FAILED, "{\"result\":\"error\",\"message\":\""+x.getMessage()+"\"}");
			}
		} catch(Exception x) {
			x.printStackTrace();
			
			log.error("RegisterException", x);
			
			getContext().getValidationErrors().add("entity", new SimpleError("create user error"));
			throw new RegisterException(this, getAccount(), getView(), "Exception encountered");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}	
	}
	
	@After(on="register", stages=LifecycleStage.BindingAndValidation)
	public void userViewInterceptor() throws Exception{
		log.info("RegisterInterceptor.intercept()");
		
		AccountTypeConverter atc = new AccountTypeConverter();

		/**
		 * Do Type Conversion
		 */
		try {
			Account account = atc.convert(
					getContext().getRequest().getParameter("account"), 
					Account.class, 
					new ArrayList<ValidationError>());
			setAccount( account );
			log.info("    Set account to "+account.getId());
		} catch (Exception x) {
			x.printStackTrace();
			throw x;
		}

		try {
			ViewTypeConverter vtc = new ViewTypeConverter(getAccount());
			View view = vtc.convert(	
					getContext().getRequest().getParameter("view"), 
					View.class, 
					new ArrayList<ValidationError>());
			setView( view );
			log.info("    Set view to "+view.getId());
		} catch (Exception x) {
			log.info("    Exception getting view: "+getContext().getRequest().getParameter("view"));
			throw x;
		}

		setEmail( getContext().getRequest().getParameter("email"));
		setUsername( getContext().getRequest().getParameter("username"));
		setPassword( getContext().getRequest().getParameter("password"));
	}
}

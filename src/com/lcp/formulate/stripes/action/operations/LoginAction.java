package com.lcp.formulate.stripes.action.operations;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.action.pages.ManagerAction;
import com.lcp.formulate.stripes.action.pages.RegisterView;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;

@UrlBinding("/do/login")
public class LoginAction extends BaseActionBean {

	private static Logger log = org.apache.log4j.Logger.getLogger(LoginAction.class);
	
	private String username;
	public void setUsername(String username) { this.username = username; }
	public String getUsername() { return username; }
	
	private String password;
	public void setPassword(String password) { this.password = password; }
	public String getPassword() { return password; }
	
	private String nextPage;
	public void setNextPage(String nextPage) { this.nextPage = nextPage; }
	public String getNextPage() { return nextPage; }
	
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }

	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	private boolean requirePassword = false;
	public boolean getRequirePassword() { return requirePassword; }
	public void setRequirePassword(boolean requirePassword) { this.requirePassword = requirePassword; }
			
	@DefaultHandler
	public Resolution login() {
		log.info("LoginAction.login() - nextPage: "+getNextPage());
		
		if (getUsername() == null) {
			log.info("    No username supplied");

			getContext().getMessages().add(new SimpleMessage("Login failed - Username required"));
			return getContext().getSourcePageResolution();
		}
		
		for (User u : EntityManager.getUsers(getAccount())) {
			log.info("    Comparing "+u.getName()+" to "+getUsername());
			if (u.getName().equals(getUsername())) {
				log.info("    Found username in account");
				
				log.info("    setting context user and ACCESS_LEVEL_USER");
				getContext().setUser(u);
				getContext().setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_USER);
				
				if (getPassword() != null) {
					if (getPassword().equals(u.getPassword())) {
						log.info("    setting context user and ACCESS_LEVEL_PASSWORD");
						getContext().setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_PASSWORD);
					}
					else {
						log.info("    Passwords don't match - "+getPassword() + " : " + u.getPassword());

						getContext().getMessages().add(new SimpleMessage("Login failed - Invalid username or password"));
						
						return getContext().getSourcePageResolution();
					}
				}
				
				break;
			}
		}
		
		if (getContext().getUser() != null) {
			log.info("    User not null: "+getContext().getUser().getId());
			log.info("    is admin?: "+getContext().getUser().getAdmin());
		}
		
		/**
		 * User exists but nextPage was not specified.
		 * Redirect to the manager page
		 */
		if (getContext().getUser() != null && nextPage == null) {
			log.info("    User exists but nextpage is null - redirect to ManagerAction");
			
			RedirectResolution resolution = new RedirectResolution(ManagerAction.class);
			resolution.addParameter("account", getAccount().getAlias());
			
			return resolution;
		}
		
    	/**
    	 * User doesn't exist but view DOES have createUser enabled
    	 * Redirect to Register
    	 */
		else if (getView() != null) {
			if (getContext().getUser() == null && getView().getCreateUser()) {
				log.info("    User doesn't exist but CreateUser is true");
				
				ForwardResolution resolution = new ForwardResolution(RegisterView.class);
				
				return resolution;
			}				
		}
		
		/**
		 * User doesn't exist
		 */
		if (getContext().getUser() == null) {
			log.info("    Login failed - User "+username+" doesn't exist");

			getContext().getMessages().add(new SimpleMessage("Login failed - Username not found"));
			
			return getContext().getSourcePageResolution();
		}
		
		log.info("    Login successful - redirecting to "+nextPage);
		
		return new RedirectResolution(nextPage);
	}
	
	public Resolution ajaxLogin() {
		log.info("LoginAction.ajaxLogin()");
		
		if (getPassword() == null)
			setPassword("");
		
		for (User u : EntityManager.getUsers(getAccount())) {
			if (u.getName().equals(getUsername())) {
				if (u.getPassword().equals(getPassword())) {
					getContext().setUser(u);
					getContext().setAccessLevel(MyActionBeanContext.ACCESS_LEVEL_PASSWORD);
					return new StreamingResolution("text/json", "{\"result\":\"success\"}");
				} else {
					return new StreamingResolution("text/json", "{\"result\":\"error\", \"message\":\"Invalid password\"}");					
				}
			}
		}

		return new StreamingResolution("text/json", "{\"result\":\"error\", \"message\":\"Invalid username\"}");
	}
}

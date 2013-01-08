package com.lcp.formulate.stripes.action.pages;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.LoginException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;

@UrlBinding("/manager/{account}")
public class ManagerAction extends BaseActionBean {	
	
	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { 
		if (this.account == null)
			if (getContext().getUser() != null)
				this.account = getContext().getUser().getAccount();
		
		return this.account;
	}

	@DefaultHandler
	public Resolution showManager() {
		return new ForwardResolution(ViewAction.MANAGER_VIEW);
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void userInterceptor() throws LoginException {		
		System.err.println("[UserInterceptor] - User requesting manager view");
		
		/**
		 * An existing user is always required for update views
		 * Login via query string is not allowed
		 */
		if (getContext().getUser() == null || getContext().getAccessLevel() < MyActionBeanContext.ACCESS_LEVEL_PASSWORD)
			throw new LoginException(this, getAccount(), null, "Login Required");
		
		/**
		 * Prevent access by the system user
		 */
		if (getContext().getUser() != null) 
			if (getContext().getUser().getSystemUser() != null)
				throw new LoginException(this, getAccount(), null, "Invalid User");
	}
	
}

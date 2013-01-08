package com.lcp.formulate.stripes.action.operations;

import org.apache.log4j.Logger;

import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.action.pages.ViewAction;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/do/logout")
public class LogoutAction extends BaseActionBean {

	private static Logger log = org.apache.log4j.Logger.getLogger(LogoutAction.class);
	
	public Resolution logout(){
		log.info("LoginAction.logout()");
		
		Integer accountId = null;
		if (getContext().getUser() != null)
			accountId = getContext().getUser().getAccount().getId();
		
		getContext().setUser(null);
		getContext().setAccessLevel(null);
		
		if (accountId != null) {
			getContext().getMessages().add(new SimpleMessage("You have been logged out."));
			RedirectResolution resolution = new RedirectResolution(ViewAction.LOGIN_VIEW).addParameter("account", accountId);
			return resolution;
		}
		
		return new RedirectResolution("index.jsp", true);		
	}
}

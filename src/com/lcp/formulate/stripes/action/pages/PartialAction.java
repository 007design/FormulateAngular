package com.lcp.formulate.stripes.action.pages;

import java.util.Arrays;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.apache.log4j.Logger;

import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/partials/{partial}")
public class PartialAction extends BaseActionBean {
	private static Logger log = org.apache.log4j.Logger.getLogger(PartialAction.class);

	private static final String[] adminPartials = {
		"editor-accountUsers", 
		"view-admin", 
		"editor-events", 
		"editor-filters", 
		"editor-form", 
		"editor-notifications", 
		"editor-fields", 
		"editor-viewUsers"};
	
	private String partial;
	public void setPartial(String partial) { this.partial = partial; }
	public String getPartial() { return partial; }
	
	@DefaultHandler
	public Resolution show() {
		log.info("PartialAction.get() - /WEB-INF/partials/"+getPartial()+".html");
		return new ForwardResolution("/WEB-INF/partials/"+getPartial()+".html"); 
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void userInterceptor() throws AjaxException {		
		log.info("[UserInterceptor] - User requesting partial");
		log.info("[UserInterceptor] - Partial: "+getPartial());
		
		/**
		 * User not logged in
		 */
		if (getContext().getUser() == null) {
			if (Arrays.asList(adminPartials).contains(getPartial()))
				throw new AjaxException(this, getContext().getEventName(), "Login Required");
		}
			
			
	}
}

package com.lcp.formulate.stripes.exceptions;

import net.sourceforge.stripes.action.ActionBean;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = 1L;

	private ActionBean actionBean;
	public ActionBean getActionBean() { return actionBean; }
	public void setActionBean(ActionBean actionBean) { this.actionBean = actionBean; }
	
	private String eventName;
	public String getEventName() { return eventName; }
	public void setEventName(String eventName) { this.eventName = eventName; }
	
	private String message;
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }
	
	public InvalidRequestException() {
		super();		
	}
	
	public InvalidRequestException(ActionBean actionBean, String eventName, String message) {
		super();
		setActionBean(actionBean);
		setEventName(eventName);
		setMessage(message);
	}
}

package com.lcp.formulate.stripes.exceptions;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;

import net.sourceforge.stripes.action.ActionBean;


public class LoginException extends Exception {
	private static final long serialVersionUID = 1L;

	private ActionBean actionBean;
	public ActionBean getActionBean() { return actionBean; }
	public void setActionBean(ActionBean actionBean) { this.actionBean = actionBean; }
	
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	private String message;
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }
			
	public LoginException() {
		super();		
	}
	
	public LoginException(ActionBean actionBean, Account account, View view, String message) {
		super();
		this.actionBean = actionBean;	
		this.account = account;
		this.view = view;
		this.message = message;
		
		/**
		 * Kill the context user and access
		 */
		((MyActionBeanContext)actionBean.getContext()).setUser(null);
		((MyActionBeanContext)actionBean.getContext()).setAccessLevel(0);
	}
}

package com.lcp.formulate.stripes.action.pages;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;

@UrlBinding("/form/{account}/{view}")
public class InputAction extends BaseActionBean implements ViewAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(InputAction.class);
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private boolean requirePassword = false;
	public void setRequirePassword(boolean requirePassword) { this.requirePassword = requirePassword; }
	public boolean getRequirePassword() { return requirePassword; }
		
	@DontBind
	@DefaultHandler
	public Resolution show() {
		log.debug("InputAction.show()");
		return new ForwardResolution(ViewAction.INPUT_VIEW);
	}
}

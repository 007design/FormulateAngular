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

@UrlBinding("/dashboard/{account}/{view}")
public class DashboardAction extends BaseActionBean implements ViewAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(DashboardAction.class);

	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return this.view; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
		
	@DontBind
	@DefaultHandler
	public Resolution showDashboard() {
		log.debug("DashboardAction.showDashboard()");
		return new ForwardResolution(ViewAction.DASHBOARD_VIEW);
	}
}

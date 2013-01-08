package com.lcp.formulate.stripes.action.pages;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;


public interface ViewAction {
	public static final String INPUT_VIEW = "/WEB-INF/jsp/input.jsp";
	public static final String UPDATE_VIEW = "/WEB-INF/jsp/update.jsp";
	public static final String DASHBOARD_VIEW = "/WEB-INF/jsp/dashboard.jsp";
	public static final String MANAGER_VIEW = "/WEB-INF/jsp/manager.jsp";
	public static final String LOGIN_VIEW = "/login.jsp";
	public static final String ERROR_VIEW = "/WEB-INF/jsp/error.jsp";
	public static final String EDIT_VIEW = "/WEB-INF/jsp/editor.jsp";
	public static final String REGISTER_VIEW = "/register.jsp";
	
	public Account getAccount();
	public View getView();
	
	public void setAccount(Account account);
	public void setView(View view);
}

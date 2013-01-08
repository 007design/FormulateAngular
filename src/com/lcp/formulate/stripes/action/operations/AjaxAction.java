package com.lcp.formulate.stripes.action.operations;

import com.lcp.formulate.entities.ormlite.Account;

public interface AjaxAction {

	public Account getAccount();
	public void setAccount(Account convert);

}

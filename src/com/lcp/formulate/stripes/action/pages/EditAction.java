package com.lcp.formulate.stripes.action.pages;

import com.lcp.formulate.entities.ormlite.Account;

public interface EditAction {
	public Account getAccount();
	public void setAccount(Account account);
}

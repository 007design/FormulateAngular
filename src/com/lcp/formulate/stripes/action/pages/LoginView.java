	package com.lcp.formulate.stripes.action.pages;

import java.util.ArrayList;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;

@UrlBinding("/login")
public class LoginView extends BaseActionBean {

	private static Logger log = org.apache.log4j.Logger.getLogger(LoginView.class);
	
	@Validate(ignore=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }

	@Validate(ignore=true)
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	private boolean requirePassword = false;
	public boolean getRequirePassword() { return requirePassword; }
	public void setRequirePassword(boolean requirePassword) { this.requirePassword = requirePassword; }
			
	@DefaultHandler
	public Resolution show() {		
		log.info("LoginView.show()");		
		return new ForwardResolution(ViewAction.LOGIN_VIEW);
	}	
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void userInterceptor() throws Exception {		
		/**
		 * Do Type Conversion
		 */	
		AccountTypeConverter atc = new AccountTypeConverter();
		
		try {
			Account account = atc.convert(
					getContext().getRequest().getParameter("account"), 
					Account.class, 
					new ArrayList<ValidationError>());
			setAccount( account );
			log.info("    Set account to "+account.getId());
		} catch (Exception x) {
			log.info("    Exception getting view: "+getContext().getRequest().getParameter("account"));
			x.printStackTrace();
			throw x;
		}

		try {
			ViewTypeConverter vtc = new ViewTypeConverter(getAccount());
			View view = vtc.convert(	
					getContext().getRequest().getParameter("view"), 
					View.class, 
					new ArrayList<ValidationError>());
			setView( view );
			log.info("    Set view to "+view.getId());
		} catch (Exception x) {
			log.info("    Exception getting view: "+getContext().getRequest().getParameter("view"));
		}
	}
}

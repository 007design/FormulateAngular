package com.lcp.formulate.stripes.action.pages;

import java.util.ArrayList;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;

@UrlBinding("/register")
public class RegisterView extends BaseActionBean {
	private static Logger log = org.apache.log4j.Logger.getLogger(RegisterView.class);

	private String username;
	public void setUsername(String username) { this.username = username; }
	public String getUsername() { return username; }

	private String nextPage;
	public void setNextPage(String nextPage) { this.nextPage = nextPage; }
	public String getNextPage() { return nextPage; }
	
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	@DefaultHandler
	public Resolution show(){
		log.info("RegisterAction.show()");		
		return new ForwardResolution(ViewAction.REGISTER_VIEW);
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
			throw x;
		}
	}
}

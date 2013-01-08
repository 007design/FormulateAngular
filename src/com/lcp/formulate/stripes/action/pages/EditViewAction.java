package com.lcp.formulate.stripes.action.pages;

import java.util.ArrayList;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.UserView;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.LoginException;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;
import com.lcp.formulate.stripes.extensions.typeconverters.ViewTypeConverter;

@UrlBinding("/edit/{account}/{view}")
public class EditViewAction extends BaseActionBean implements EditAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(EditViewAction.class);
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }

	@After(stages=LifecycleStage.BindingAndValidation)
	public void intercept() throws Exception {
		log.info("EditorInterceptor.intercept()");
		
		/**
		 * Do Account type conversion
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
			x.printStackTrace();
			throw x;
		}
		
		/**
		 * Not logged in
		 */
		if (getContext().getUser() == null)
			throw new LoginException(this, getAccount(), null, "Login Required");
		
		/**
		 * Not logged in with PASSWORD level access
		 */
		if (getContext().getAccessLevel()<2)
			throw new LoginException(this, getAccount(), null, "Password Required");
		
		/**
		 * User is logged in but to a different account
		 */
		else if (!getContext().getUser().getAccount().getId().equals(getAccount().getId()))
			throw new LoginException(this, getAccount(), null, "Access Denied - Invalid Account");
		
		/**
		 * For EditViewAction's do View type conversion
		 */
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
	
		/**
		 * If they're not an admin, check if they can edit this view
		 */
		if (!getContext().getUser().getAdmin()) {
			log.info("    User is not an admin, checking userViews");
			boolean ok = false;
			for (UserView uv : getContext().getUser().getUserViews()) {
				if (uv.getView().getId().equals(getView().getId())) {
					log.info("    Found userview.  Edit: "+uv.getEdit());
					if (!uv.getEdit())
						throw new LoginException(this, getAccount(), null, "Access Denied - You cannot edit this view");
					ok = true;
					break;
				}
			}
			if (!ok)
				throw new LoginException(this, getAccount(), null, "Access Denied - You cannot access this view");
		}
	}
	
	@DontBind
	@DefaultHandler
	public Resolution show() {
		log.info("EditViewAction.show() account:"+getAccount().getName()+" / view:"+getView());
		return new ForwardResolution(ViewAction.EDIT_VIEW);		
	}	

}

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
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.LoginException;
import com.lcp.formulate.stripes.extensions.typeconverters.SubmissionTypeConverter;

@UrlBinding("/update/{account}/{view}/{submission}")
public class UpdateAction extends BaseActionBean implements ViewAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(UpdateAction.class);
	
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
		
	private Submission submission;
	public void setSubmission(Submission submission) { this.submission = submission; }
	public Submission getSubmission() { return submission; }
	
	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }
	
	private String username;
	public void setUsername(String username) { this.username = username; }
	public String getUsername() { return username; }
		
	@DontBind
	@DefaultHandler
	public Resolution show() {
		log.debug("UpdateView.show()");
		return new ForwardResolution(ViewAction.UPDATE_VIEW);
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void userInterceptor() throws LoginException {	
		/**
		 * Do type conversion for the submission
		 */
		
		log.debug("UpdateAction.userInterceptor() - User requesting update view");
		
		String submissionId = getContext().getRequest().getParameter("submission");

		try {
			SubmissionTypeConverter stc = new SubmissionTypeConverter();
			setSubmission( stc.convert(submissionId, Submission.class, new ArrayList<ValidationError>()) );
		} catch (Exception x) {
			x.printStackTrace();
			log.error(x);
			throw new LoginException(this, getAccount(), getView(), "Invalid Submission - "+submissionId);
		}
		
	}	
}

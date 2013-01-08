package com.lcp.formulate.stripes.action.data;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.exceptions.LoginException;

@UrlBinding("/data/submissionData/{submission}")
public class SubmissionDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SubmissionDataAction.class);

	@Validate(required=true)
	private Submission submission;
	public void setSubmission(Submission submission) { this.submission = submission; }
	public Submission getSubmission() { return submission; }
			
	@DefaultHandler
	public Resolution defaultResolution() throws LoginException, AjaxException {
		log.info("SubmissionData - id: "+getSubmission().getId());
		try {				
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
						.writerWithView(JsonViews.Update.class)
							.writeValueAsString( submission );
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "submissionHistory", "data error");
		}
	}
}

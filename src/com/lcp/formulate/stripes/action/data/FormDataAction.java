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
import com.lcp.formulate.entities.ormlite.Form;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/form/{form}")
public class FormDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(FormDataAction.class);

	@Validate(required=true)
	private Form form;
	public void setForm(Form form) { this.form =form; }
	public Form getForm() { return form; }
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("FormDataAction");
		
		try {
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.Form.class)
				.writeValueAsString( getForm() );
		
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "form", "data error");
		}
	}
}

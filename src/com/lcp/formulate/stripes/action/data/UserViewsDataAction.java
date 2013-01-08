package com.lcp.formulate.stripes.action.data;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/userViews/{user}")
public class UserViewsDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(UserViewsDataAction.class);

	private User user;
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("FormDataAction");
		
		try {
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.UserViews.class)
				.writeValueAsString( getUser().getUserViews() );
		
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "form", "data error");
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
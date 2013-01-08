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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.Form;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/forms/{account}")
public class FormsListDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(FormDataAction.class);

	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("FormDataAction");
		
		ConnectionSource conn = null;
		
		try {
			conn = EntityManager.getConnection();
			Dao<Form, String> dao = DaoManager.createDao(conn, Form.class);
			
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.FormsList.class)
				.writeValueAsString( dao.queryForAll() );
		
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "form", "data error");
		} finally {
			try { conn.close(); } catch (Exception x) {}
		}
	}
}

package com.lcp.formulate.stripes.action.operations;

import java.io.BufferedReader;
import java.sql.SQLException;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.Field;
import com.lcp.formulate.entities.ormlite.FieldOption;
import com.lcp.formulate.entities.ormlite.Form;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.action.data.FormDataAction;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;

@UrlBinding("/save/form/{account}")
public class SaveFormAction extends BaseActionBean implements AjaxAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SaveFormAction.class);
	
	private Form form;
	public void setForm(Form form) { this.form = form; }
	public Form getForm() { return form; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	@DontBind
	@DefaultHandler
	public Resolution save() {
		log.info("SaveFormAction");
		
		ConnectionSource conn = null;
		
		try {
			conn = EntityManager.getConnection();
			Dao<Form, String> formDao = DaoManager.createDao(conn, Form.class);
			Dao<Field, String> fieldDao = DaoManager.createDao(conn, Field.class);
			Dao<FieldOption, String> optDao = DaoManager.createDao(conn, FieldOption.class);
			
			CreateOrUpdateStatus status = formDao.createOrUpdate(getForm());
			
			if (status.isCreated())
				log.info("    New form created");
			else if (status.isUpdated())
				log.info("    Form "+getForm().getId()+" updated");
			
			/**
			 * Delete any old fields
			 */
			for (Field f : fieldDao.queryForEq("forms_id", getForm().getId())) {
				if (!getForm().getFields().contains(f)) {
					f.setDeleted(true);
					fieldDao.update(f);
				}
			}
			
			/**
			 * Save the fields
			 */
			for (Field field : getForm().getFields()) {
				
				field.setForm(getForm());
				status = fieldDao.createOrUpdate(field);
				
				/**
				 * Save the field options
				 */
				/**
				 * Save the options
				 */
				try {
					if (field.getFieldOptions() != null) {
						for (FieldOption oldOpt : optDao.queryForEq("fields_id", field.getId())) {
							if (!field.getFieldOptions().contains(oldOpt))
								optDao.delete(oldOpt);
						}
						for (FieldOption opt : field.getFieldOptions()) {
							opt.setField(field);
							optDao.createOrUpdate(opt);
						}
					}
				} catch (Exception x) {
					log.info("SAVE FIELDOPTIONS FAILED!!!");
					throw x;
				}
				
				if (status.isCreated())
					log.info("    New field created");
				else if (status.isUpdated())
					log.info("    Field "+field.getId()+" updated");
			}
			
		} catch(Exception x) {
			x.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("invalid save target"));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}

		RedirectResolution resolution = new RedirectResolution(FormDataAction.class);
		resolution.addParameter("form", getForm().getId());
		return resolution;
	}
	
	@Before(stages=LifecycleStage.BindingAndValidation)
	public void saveFormInterceptor() throws AjaxException {		
		if (!getContext().getUser().getAdmin())
			throw new AjaxException(this, getContext().getEventName(), "Admin Required");
		if (getContext().getAccessLevel() < MyActionBeanContext.ACCESS_LEVEL_PASSWORD)
			throw new AjaxException(this, getContext().getEventName(), "Password required");
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = getContext().getRequest().getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info(jb.toString());
		
		try {
			setForm( new Gson().fromJson(jb.toString(), Form.class) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.lcp.formulate.stripes.action.operations;

import java.io.BufferedReader;
import java.sql.SQLException;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/save/user/{account}")
public class SaveUserAction extends BaseActionBean implements AjaxAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SaveUserAction.class);
	
	private User user;
	public void setUser(User user) { this.user = user; }
	public User getUser() { return user; }

	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	@DefaultHandler
	public Resolution save() {
		log.info("SaveUserAction.save()");
		
		ConnectionSource conn = null;
		
		try {
			conn = EntityManager.getConnection();
			Dao<User, String> dao = DaoManager.createDao(conn, User.class);
			
			/**
			 * If they didn't supply a new password we need to set the password
			 * to that of the existing user, otherwise it'll bitch that password can't be null
			 */
			if (getUser().getPassword() == null)
				if (getUser().getId() != null) {
					// Get the existing user if there is one
					User oldUser = dao.queryForId(getUser().getId().toString());
					
					if (oldUser != null)
						getUser().setPassword(oldUser.getPassword());
				}			
			
			dao.createOrUpdate(getUser());
		} catch(Exception x) {
			log.info("saveError", x);
			x.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("invalid save target"));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}

		return new StreamingResolution("text/json","{\"success\":true}");
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void saveUserInterceptor() throws AjaxException {
		log.info("SaveUserAction.interceptor()");
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = getContext().getRequest().getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.err.println(jb.toString());
		
		try {
			setUser( new Gson().fromJson(jb.toString(), User.class) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!getContext().getUser().getAdmin())
			if (!getUser().equals(getContext().getUser()))
				throw new AjaxException(this, getContext().getEventName(), "Access Denied");
		
	}	
}

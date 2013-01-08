package com.lcp.formulate.stripes.action.data;

import java.sql.SQLException;

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
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/accountUsers/{account}")
public class AccountUsersDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(AccountUsersDataAction.class);

	@Validate(required=true)
	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private ConnectionSource conn;	
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("AccountUsersDataAction");
		try {
			conn = EntityManager.getConnection();
			Dao<User, String> dao = DaoManager.createDao(conn, User.class);
			
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.UsersList.class)
					.writeValueAsString( dao.query(
						dao.queryBuilder().where().eq("accounts_id", getAccount().getId())
						.and().ne("users_admin", true)
						.and().isNull("users_systemUser")
						.and().ne("users_deleted", true).prepare() 
					) 
				);
		
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "notifications", "data error");
		} finally {
			try { conn.close(); } catch (SQLException e) {}
		}
	}
}

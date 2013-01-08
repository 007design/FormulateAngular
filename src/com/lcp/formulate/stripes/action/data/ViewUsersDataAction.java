package com.lcp.formulate.stripes.action.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.UserView;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/viewUsers/{view}")
public class ViewUsersDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(ViewUsersDataAction.class);

	@Validate(required=true)
	private View view;
	public void setView(View view) { this.view =view; }
	public View getView() { return view; }
	
	private ConnectionSource conn;	
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("ViewUsersDataAction");
		
		try {
			conn = EntityManager.getConnection();
			Dao<UserView, String> dao = DaoManager.createDao(conn, UserView.class);
			
			List<User> users = new ArrayList<User>();
			List<UserView> userViews = dao.queryForEq("views_id", getView().getId());
			for (UserView uv : userViews) {
				if (uv.getUser().getSystemUser() == null && !uv.getUser().getAdmin() && !uv.getUser().getDeleted())
					users.add(uv.getUser());
			}
			
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
				.writerWithView(JsonViews.ViewUsers.class)
				.writeValueAsString(users);
			
			log.info(string);
			
			return new StreamingResolution("text/json", string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "viewUsers", "data error");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}
	}
}

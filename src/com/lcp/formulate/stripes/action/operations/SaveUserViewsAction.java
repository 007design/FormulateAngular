package com.lcp.formulate.stripes.action.operations;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.UserView;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/save/users/{account}/{view}")
public class SaveUserViewsAction extends BaseActionBean implements AjaxAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(SaveUserViewsAction.class);

	private View view;
	public void setView(View view) { this.view = view; }
	public View getView() { return view; }

	private Account account;
	public void setAccount(Account account) { this.account = account; }
	public Account getAccount() { return account; }
	
	private List<User> usersList;
	public void setUsersList(List<User> usersList) { this.usersList = usersList; }
	public List<User> getUsersList() { return usersList; }

	@DefaultHandler
	public Resolution save() {
		log.info("SaveUserViewsAction");
		
		JdbcConnectionSource connectionSource = null;
		try {
			connectionSource = EntityManager.getConnection();
			Dao<UserView, String> dao = DaoManager.createDao(connectionSource, UserView.class);

			/**
			 *  Delete any userview for this view whose user is not in the upload
			 */
			DeleteBuilder<UserView, String> deleteBuilder = dao.deleteBuilder();
			
			Where<UserView, String> where = deleteBuilder.where();
			where.eq("views_id", getView().getId());
			for (User user : getUsersList()) {
				where.and().ne("users_id", user.getId());
			}
			
			dao.delete(deleteBuilder.prepare());
			
			/**
			 * Now update all the uploaded vals
			 */
			List<UserView> existing = dao.queryForEq("views_id", getView().getId());
			
			// Examine each uploaded user
			for (int a=0; a<getUsersList().size(); a++) {
				// Compare the uploaded user to the list of userviews
				for (UserView uv : existing) {
					if (uv.getUser().equals(getUsersList().get(a))) {
						getUsersList().remove(a);
						a--;
						break;
					}
				}
			}
			
			// Save the users remaining in the list
			for (User u : getUsersList()) {
				UserView newUv = new UserView();
				newUv.setUser(u);
				newUv.setView(getView());
				newUv.setEdit(false);
				dao.createOrUpdate(newUv);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			try {
				connectionSource.close();
			} catch (SQLException e) {}
		}	
		
		return new StreamingResolution("text/json","{\"success\":true}");
		
//		RedirectResolution resolution = new RedirectResolution(ViewDataAction.class);
//		resolution.addParameter("id", getView().getId());
//		return resolution;	
	}
	
	@After(stages=LifecycleStage.BindingAndValidation)
	public void saveInterceptor() throws AjaxException {		
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
			Type collectionType = new TypeToken<List<User>>(){}.getType();
			List<User> users = new Gson().fromJson(jb.toString(), collectionType);
			
			setUsersList(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (getContext().getUser() == null)
			throw new AjaxException(this, getContext().getEventName(), "Access Denied");
		
		if (getContext().getUser().getAdmin())
			return;
		
		else		
			for (UserView uv : getContext().getUser().getUserViews())
				if (uv.getView().equals(getView()))
					return;
		
		throw new AjaxException(this, getContext().getEventName(), "Access Denied");
	}
}

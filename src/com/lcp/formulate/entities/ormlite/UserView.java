package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class UserView.
 */
@DatabaseTable (tableName="userviews")
public class UserView implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The edit. */
	@JsonView({
		JsonViews.User.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="userviews_edit", canBeNull=false, defaultValue="0")
	private boolean edit = false;

	/** The id. */
	@JsonView({
		JsonViews.User.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="userviews_id", generatedId=true)
	private Integer id;

	/** The user. */
	@JsonBackReference
	@DatabaseField (columnName="users_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private User user;

	/** The view. */
	@JsonView({
		JsonViews.User.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private View view; 

	/**
	 * Gets the edits the.
	 *
	 * @return the edits the
	 */
	public boolean getEdit() {
		return edit;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * Sets the edits the.
	 *
	 * @param edit the new edits the
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) {
		this.view = view;
	}
}

package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class View.
 */
@DatabaseTable (tableName="views")
public class View implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The account. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,

		JsonViews.FormsList.class,
		JsonViews.UserViews.class,
		JsonViews.ViewsList.class})
	@DatabaseField (columnName="accounts_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Account account;
	
	/** The alias. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,

		JsonViews.FormsList.class,
		JsonViews.ViewsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_alias", canBeNull=false, unique=true)
	private String alias;
	
	/** The create user. */
	@JsonView({		
		JsonViews.Editor.class})
	@DatabaseField (columnName="views_createUser", canBeNull=false, defaultValue="0")
	private boolean createUser = false;

	/** The deleted. */
	@DatabaseField (columnName="views_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted = false;
	
	/** The enable dashboard. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.FormsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_enableDashboard", canBeNull=false, defaultValue="0")
	private boolean enableDashboard = false;
	
	/** The enable input. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.FormsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_enableInput", canBeNull=false, defaultValue="1")
	private boolean enableInput = true;

	/** The enable update. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.FormsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_enableUpdate", canBeNull=false, defaultValue="0")
	private boolean enableUpdate = false;
	
	/** The events. */
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="view", eager=true)
	private Collection<ViewEvent> events;
	
	/** The filters. */
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="view", eager=true)
	private Collection<Filter> filters;

	/** The form. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class})
	@DatabaseField (columnName="forms_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Form form;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		
		JsonViews.FormsList.class,
		JsonViews.ViewsList.class,
		JsonViews.Notifications.class,
		JsonViews.Events.class,
		JsonViews.User.class,
		JsonViews.UserViews.class,
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="views_id", generatedId=true)
	private Integer id;
	
	/** The name. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,

		JsonViews.FormsList.class,
		JsonViews.ViewsList.class,
		JsonViews.User.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="views_name", canBeNull=false)
	private String name;

	/** The notifications. */
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="view", eager=true)
	private Collection<Notification> notifications;
	
	/** The require password. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class})
	@DatabaseField (columnName="views_requirePassword", canBeNull=false, defaultValue="1")
	private boolean requirePassword = true;
	
	/** The require user. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class})
	@DatabaseField (columnName="views_requireUser", canBeNull=false, defaultValue="1")
	private boolean requireUser = true;

	/** The submit text. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class})
	@DatabaseField (columnName="views_submitText", canBeNull=false, defaultValue="Submit")
	private String submitText;
	
	/** The view fields. */
	@JsonView({
		JsonViews.View.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="view")
	private Collection<ViewField> viewFields;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return ((View)obj).getId() == getId();
	}

	/**
	 * Gets the account.
	 *
	 * @return the account
	 */
	public Account getAccount() { return account; }
	
	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias() { return alias; }
	
	/**
	 * Gets the creates the user.
	 *
	 * @return the creates the user
	 */
	public boolean getCreateUser() { return createUser; }

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() { return deleted; }
	
	/**
	 * Gets the enable dashboard.
	 *
	 * @return the enable dashboard
	 */
	public boolean getEnableDashboard() { return enableDashboard; }
	
	/**
	 * Gets the enable input.
	 *
	 * @return the enable input
	 */
	public boolean getEnableInput() { return enableInput; }

	/**
	 * Gets the enable update.
	 *
	 * @return the enable update
	 */
	public boolean getEnableUpdate() { return enableUpdate; }
	
	/**
	 * Gets the events.
	 *
	 * @return the events
	 */
	public Collection<ViewEvent> getEvents() { return this.events; }
	
	/**
	 * Gets the filters.
	 *
	 * @return the filters
	 */
	public Collection<Filter> getFilters() { return filters; }
	
	/**
	 * Gets the form.
	 *
	 * @return the form
	 */
	public Form getForm() { return form; }
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { return name; }

	/**
	 * Gets the notifications.
	 *
	 * @return the notifications
	 */
	public Collection<Notification> getNotifications() { return notifications; }
	
	/**
	 * Gets the require password.
	 *
	 * @return the require password
	 */
	public boolean getRequirePassword() { return requirePassword; }
	
	/**
	 * Gets the require user.
	 *
	 * @return the require user
	 */
	public boolean getRequireUser() { return requireUser; }
	
	/**
	 * Gets the submit text.
	 *
	 * @return the submit text
	 */
	public String getSubmitText() { return submitText; }
	
	/**
	 * Gets the view fields.
	 *
	 * @return the view fields
	 */
	public Collection<ViewField> getViewFields() { return this.viewFields; }
	
	/**
	 * Sets the account.
	 *
	 * @param account the new account
	 */
	public void setAccount(Account account) { this.account = account; }

	/**
	 * Sets the alias.
	 *
	 * @param alias the new alias
	 */
	public void setAlias(String alias) { this.alias = alias; }
	
	/**
	 * Sets the creates the user.
	 *
	 * @param createUser the new creates the user
	 */
	public void setCreateUser(boolean createUser) { this.createUser = createUser; }
	
	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) { this.deleted = deleted; }
	
	/**
	 * Sets the enable dashboard.
	 *
	 * @param enableDashboard the new enable dashboard
	 */
	public void setEnableDashboard(boolean enableDashboard) { this.enableDashboard = enableDashboard; }
	
	/**
	 * Sets the enable input.
	 *
	 * @param enableInput the new enable input
	 */
	public void setEnableInput(boolean enableInput) { this.enableInput = enableInput; }
	
	/**
	 * Sets the enable update.
	 *
	 * @param enableUpdate the new enable update
	 */
	public void setEnableUpdate(boolean enableUpdate) { this.enableUpdate = enableUpdate; }
	
	/**
	 * Sets the events.
	 *
	 * @param events the new events
	 */
	public void setEvents(Collection<ViewEvent> events) { this.events = events; }
	
	/**
	 * Sets the filters.
	 *
	 * @param filters the new filters
	 */
	public void setFilters(Collection<Filter> filters) { this.filters = filters; }
	
	/**
	 * Sets the form.
	 *
	 * @param form the new form
	 */
	public void setForm(Form form) { this.form = form; }
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) { this.name = name; }
	
	/**
	 * Sets the notifications.
	 *
	 * @param notifications the new notifications
	 */
	public void setNotifications(Collection<Notification> notifications) { this.notifications = notifications; }
	
	/**
	 * Sets the require password.
	 *
	 * @param requirePassword the new require password
	 */
	public void setRequirePassword(boolean requirePassword) { this.requirePassword = requirePassword; }
	
	/**
	 * Sets the require user.
	 *
	 * @param requireUser the new require user
	 */
	public void setRequireUser(boolean requireUser) { this.requireUser = requireUser; }
	
	/**
	 * Sets the submit text.
	 *
	 * @param submitText the new submit text
	 */
	public void setSubmitText(String submitText) { this.submitText = submitText; }
	

	/**
	 * Sets the view fields.
	 *
	 * @param viewFields the new view fields
	 */
	public void setViewFields(Collection<ViewField> viewFields) { this.viewFields = viewFields; }
}

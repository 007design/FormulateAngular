package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class Notification.
 */
@DatabaseTable (tableName="notifications")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant BOTH. */
	public static final Integer BOTH = 2;
	
	/** The Constant HTML. */
	public static final Integer HTML = 2;
	
	/** The Constant INITIAL. */
	public static final Integer INITIAL = 0;
	
	/** The Constant TEXT. */
	public static final Integer TEXT = 1;
	
	/** The Constant UPDATE. */
	public static final Integer UPDATE = 1;

	/** The body. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_body", canBeNull=false)
	private String body;
	
	/** The deleted. */
	@DatabaseField (columnName="notifications_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;

	/** The email user. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_emailUser", canBeNull=false, defaultValue="0")
	private boolean emailUser;
	
	/** The format. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_format", canBeNull=false)
	private Integer format;
	
	/** The id. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_id", generatedId=true)
	private Integer id;

	/** The initial. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_initial", canBeNull=false, defaultValue="0")
	private Integer initial;
	
	/** The notification conditions. */
	@JsonView({
		JsonViews.Notifications.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="notification", eager=true)
	private Collection<NotificationCondition> notificationConditions;

	/** The recipients. */
	@JsonView({
		JsonViews.Notifications.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="notification", eager=true)
	private Collection<Recipient> recipients;

	/** The subject. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notifications_subject", canBeNull=false)
	private String subject;
	
	/** The view. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private View view;

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		return ((Notification)o).getId().equals(getId());
	}


	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() {
		return deleted;
	}
	
	/**
	 * Gets the email user.
	 *
	 * @return the email user
	 */
	public boolean getEmailUser() {
		return emailUser;
	}

	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public Integer getFormat() {
		return format;
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
	 * Gets the initial.
	 *
	 * @return the initial
	 */
	public Integer getInitial() {
		return initial;
	}

	/**
	 * Gets the notification conditions.
	 *
	 * @return the notification conditions
	 */
	public Collection<NotificationCondition> getNotificationConditions() { return this.notificationConditions; }

	/**
	 * Gets the recipients.
	 *
	 * @return the recipients
	 */
	public Collection<Recipient> getRecipients() { return this.recipients; }

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() { return view; }

	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Sets the email user.
	 *
	 * @param emailUser the new email user
	 */
	public void setEmailUser(boolean emailUser) {
		this.emailUser = emailUser;
	}

	/**
	 * Sets the format.
	 *
	 * @param format the new format
	 */
	public void setFormat(Integer format) {
		this.format = format;
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
	 * Sets the initial.
	 *
	 * @param initial the new initial
	 */
	public void setInitial(Integer initial) {
		this.initial = initial;
	}
	
	/**
	 * Sets the notification conditions.
	 *
	 * @param notificationConditions the new notification conditions
	 */
	public void setNotificationConditions(Collection<NotificationCondition> notificationConditions) { this.notificationConditions = notificationConditions; }
	
	/**
	 * Sets the recipients.
	 *
	 * @param recipients the new recipients
	 */
	public void setRecipients(List<Recipient> recipients) { this.recipients = recipients; }
	
	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) { this.view = view; }
}

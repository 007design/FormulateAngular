package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class Recipient.
 */
@DatabaseTable (tableName="recipients")
public class Recipient implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The email. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="recipients_email", canBeNull=false)
	private String email;

	/** The id. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="recipients_id", generatedId=true)
	private Integer id;
	
	/** The notification. */
	@JsonBackReference
	@DatabaseField (columnName="notifications_id", canBeNull=false, foreign=true)
	private Notification notification;
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
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
	 * Gets the notification.
	 *
	 * @return the notification
	 */
	public Notification getNotification() { return notification; }

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * Sets the notification.
	 *
	 * @param notification the new notification
	 */
	public void setNotification(Notification notification) { this.notification = notification; }
}

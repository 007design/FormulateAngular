package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationCondition.
 */
@DatabaseTable (tableName="notificationconditions")
public class NotificationCondition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The comparison. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notificationconditions_comparison", canBeNull=false, defaultValue="0")
	private Integer comparison;

	/** The id. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notificationconditions_id", generatedId=true)
	private Integer id;
	
	/** The notification. */
	@JsonBackReference
	@DatabaseField (columnName="notifications_id", canBeNull=false, foreign=true)
	private Notification notification;
	
	/** The target. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notificationconditions_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Field target;

	/** The value. */
	@JsonView({
		JsonViews.Notifications.class})
	@DatabaseField (columnName="notificationconditions_value", canBeNull=false)
	private String value;
	
	/**
	 * Gets the comparison.
	 *
	 * @return the comparison
	 */
	public Integer getComparison() {
		return comparison;
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
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Field getTarget() { return target; }
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the comparison.
	 *
	 * @param comparison the new comparison
	 */
	public void setComparison(Integer comparison) {
		this.comparison = comparison;
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
	
	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(Field target) { this.target = target; }
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}

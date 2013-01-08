package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class EventCondition.
 */
@DatabaseTable (tableName="eventconditions")
public class EventCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The comparison. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="eventconditions_comparison", canBeNull=false, defaultValue="0")
	private Integer comparison;

	/** The id. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="eventconditions_id", generatedId=true)
	private Integer id;
	
	/** The target. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="eventconditions_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Field target;
	
	/** The value. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="eventconditions_value", canBeNull=false)
	private String value;

	/** The view event. */
	@JsonBackReference
	@DatabaseField (columnName="viewevents_id", canBeNull=false, foreign=true)
	private ViewEvent viewEvent;
	
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
	 * Gets the view event.
	 *
	 * @return the view event
	 */
	public ViewEvent getViewEvent() { return viewEvent; }
	
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
	
	/**
	 * Sets the view event.
	 *
	 * @param viewEvent the new view event
	 */
	public void setViewEvent(ViewEvent viewEvent) { this.viewEvent = viewEvent; }
}

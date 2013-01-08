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
 * The Class ViewEvent.
 */
@DatabaseTable (tableName="viewevents")
public class ViewEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant TYPE_INITIAL. */
	public static final int TYPE_INITIAL = 1;
	
	/** The Constant TYPE_UPDATE. */
	public static final int TYPE_UPDATE = 2;

	/** The event conditions. */
	@JsonView({
		JsonViews.Events.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="viewEvent", eager=true)
	private Collection<EventCondition> eventConditions;

	/** The id. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="viewevents_id", generatedId=true)
	private Integer id;
	
	/** The initial. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="viewevents_initial", canBeNull=false, defaultValue="0")
	private Integer initial;
	
	/** The target. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="viewevents_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Field target;
	
	/** The value. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="viewevents_value", canBeNull=false)
	private String value;

	/** The view. */
	@JsonView({
		JsonViews.Events.class})
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true)
	private View view;
	
	/**
	 * Gets the event conditions.
	 *
	 * @return the event conditions
	 */
	public Collection<EventCondition> getEventConditions() { return this.eventConditions; }
	
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
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() { return view; }

	/**
	 * Sets the event conditions.
	 *
	 * @param eventConditions the new event conditions
	 */
	public void setEventConditions(Collection<EventCondition> eventConditions) { this.eventConditions = eventConditions; }

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
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) { this.view = view; }
	
}

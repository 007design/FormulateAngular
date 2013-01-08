package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.util.TargetSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class Trigger.
 */
@DatabaseTable (tableName="triggers")
public class Trigger implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggers_id", generatedId=true)
	private Integer id;
	
	/** The target. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggers_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	@JsonSerialize(using=TargetSerializer.class)
	private ViewField target;
	
	/** The trigger conditions. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="trigger", eager=true)
	private Collection<TriggerCondition> triggerConditions;

	/** The trigger values. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="trigger", eager=true)
	private Collection<TriggerValue> triggerValues;
	
	/** The type. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggertypes_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private TriggerType type;
	
	/** The view field. */
	@JsonBackReference
	@DatabaseField (columnName="viewfields_id", canBeNull=false, foreign=true)
	private ViewField viewField;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }
	
	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public ViewField getTarget() { return target; }
	
	/**
	 * Gets the trigger conditions.
	 *
	 * @return the trigger conditions
	 */
	public Collection<TriggerCondition> getTriggerConditions() { return this.triggerConditions; }

	/**
	 * Gets the trigger values.
	 *
	 * @return the trigger values
	 */
	public Collection<TriggerValue> getTriggerValues() { return this.triggerValues; }
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public TriggerType getType() { return type; }
	
	/**
	 * Gets the view field.
	 *
	 * @return the view field
	 */
	public ViewField getViewField() { return viewField; }

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(ViewField target) { this.target = target; }
	
	/**
	 * Sets the trigger conditions.
	 *
	 * @param triggerConditions the new trigger conditions
	 */
	public void setTriggerConditions(Collection<TriggerCondition> triggerConditions) { this.triggerConditions = triggerConditions; }

	/**
	 * Sets the trigger values.
	 *
	 * @param triggerValues the new trigger values
	 */
	public void setTriggerValues(Collection<TriggerValue> triggerValues) { this.triggerValues = triggerValues; }
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(TriggerType type) { this.type = type; }
	
	/**
	 * Sets the view field.
	 *
	 * @param viewField the new view field
	 */
	public void setViewField(ViewField viewField) { this.viewField = viewField; }
	
}

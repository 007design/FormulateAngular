package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.util.TargetSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class TriggerCondition.
 */
@DatabaseTable (tableName="triggerconditions")
public class TriggerCondition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant EQUAL_TO. */
	public static final Integer EQUAL_TO =		1;
	
	/** The Constant GREATER_THAN. */
	public static final Integer GREATER_THAN =	3;
	
	/** The Constant LESS_THAN. */
	public static final Integer LESS_THAN =		4;
	
	/** The Constant NOT_EQUAL. */
	public static final Integer NOT_EQUAL =		2;

	/** The comparison. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggerconditions_comparison", canBeNull=false, defaultValue="0")
	private Integer comparison;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggerconditions_id", generatedId=true)
	private Integer id;
	
	/** The target. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggerconditions_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	@JsonSerialize(using=TargetSerializer.class)
	private ViewField target;

	/** The trigger. */
	@JsonBackReference
	@DatabaseField (columnName="triggers_id", canBeNull=false, foreign=true)
	private Trigger trigger;
	
	/** The value. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggerconditions_value", canBeNull=false)
	private String value;
	
	/**
	 * Gets the comparison.
	 *
	 * @return the comparison
	 */
	public Integer getComparison() { return comparison; }

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
	 * Gets the trigger.
	 *
	 * @return the trigger
	 */
	public Trigger getTrigger() { return trigger; }

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() { return value; }
	
	/**
	 * Sets the comparison.
	 *
	 * @param comparison the new comparison
	 */
	public void setComparison(Integer comparison) { this.comparison = comparison; }
	
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
	 * Sets the trigger.
	 *
	 * @param trigger the new trigger
	 */
	public void setTrigger(Trigger trigger) { this.trigger = trigger; }
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) { this.value = value; }
		
}

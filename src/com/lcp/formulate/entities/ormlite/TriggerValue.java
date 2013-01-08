package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class TriggerValue.
 */
@DatabaseTable (tableName="triggervalues")
public class TriggerValue implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggervalues_id", generatedId=true)
	private Integer id;
	
	/** The sequence. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggervalues_sequence", canBeNull=false, defaultValue="1")
	private Integer sequence;
	
	/** The trigger. */
	@JsonBackReference
	@DatabaseField (columnName="triggers_id", canBeNull=false, foreign=true)
	private Trigger trigger;

	/** The value. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@DatabaseField (columnName="triggervalues_value", canBeNull=false)
	private String value;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }
	
	/**
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public Integer getSequence() { return sequence; }

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
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }

	/**
	 * Sets the sequence.
	 *
	 * @param sequence the new sequence
	 */
	public void setSequence(Integer sequence) { this.sequence = sequence; }
	
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() { return this.getValue(); }
		
}

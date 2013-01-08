package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class FieldOption.
 */
@DatabaseTable (tableName="fieldoptions")
public class FieldOption implements Option, Serializable {
	private static final long serialVersionUID = 1L;

	/** The default value. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.View.class})
	@DatabaseField (columnName="fieldoptions_default", canBeNull=false, defaultValue="0")
	private boolean defaultValue;

	/** The field. */
	@JsonBackReference
	@DatabaseField (columnName="fields_id", canBeNull=false, foreign=true)
	private Field field;
	
	/** The id. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.View.class})
	@DatabaseField (columnName="fieldoptions_id", generatedId=true)
	private Integer id;
	
	/** The sequence. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.View.class})
	@DatabaseField (columnName="fieldoptions_sequence", canBeNull=false, defaultValue="1")
	private Integer sequence;

	/** The value. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.View.class})
	@DatabaseField (columnName="fieldoptions_value", canBeNull=false)
	private String value;

	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getDefaultValue()
	 */
	public boolean getDefaultValue() { return defaultValue; }

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public Field getField() { return field; }

	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getId()
	 */
	public Integer getId() { return id; }
	
	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getSequence()
	 */
	public Integer getSequence() { return sequence; }

	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getValue()
	 */
	public String getValue() { return value; }
	
	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(boolean defaultValue) { this.defaultValue = defaultValue; }
	
	/**
	 * Sets the field.
	 *
	 * @param field the new field
	 */
	public void setField(Field field) { this.field = field; }
	
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
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) { this.value = value; }	
}

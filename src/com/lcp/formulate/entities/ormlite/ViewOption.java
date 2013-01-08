package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewOption.
 */
@DatabaseTable (tableName="viewoptions")
public class ViewOption implements Option, Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The default value. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewOptions.class})
	@DatabaseField (columnName="viewoptions_default", canBeNull=false, defaultValue="0")
	private boolean defaultValue;

	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewOptions.class})
	@DatabaseField (columnName="viewoptions_id", generatedId=true)
	private Integer id;

	/** The sequence. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewOptions.class})
	@DatabaseField (columnName="viewoptions_sequence", canBeNull=false, defaultValue="1")
	private Integer sequence;
	
	/** The value. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewOptions.class})
	@DatabaseField (columnName="viewoptions_value", canBeNull=false)
	private String value;
	
	/** The view field. */
	@JsonBackReference
	@DatabaseField (columnName="viewfields_id", canBeNull=false, foreign=true)
	private ViewField viewField;


	/**
	 * Instantiates a new view option.
	 */
	public ViewOption(){}


	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getDefaultValue()
	 */
	public boolean getDefaultValue() {
		return defaultValue;
	}


	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getId()
	 */
	public Integer getId() {
		return id;
	}
			

	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getSequence()
	 */
	public Integer getSequence() {
		return sequence;
	}

	/* (non-Javadoc)
	 * @see com.lcp.formulate.entities.ormlite.Option#getValue()
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the view field.
	 *
	 * @return the view field
	 */
	public ViewField getViewField() { return viewField; }

	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
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
	 * Sets the sequence.
	 *
	 * @param sequence the new sequence
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Sets the view field.
	 *
	 * @param viewField the new view field
	 */
	public void setViewField(ViewField viewField) { this.viewField = viewField; }
}

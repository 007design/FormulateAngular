package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class FieldType.
 */
@DatabaseTable (tableName="fieldtypes")
public class FieldType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant CHECKBOX. */
	public static final Integer CHECKBOX = 	4;
	
	/** The Constant DATE. */
	public static final Integer DATE = 	7;
	
	/** The Constant FILE. */
	public static final Integer FILE = 		6;
	
	/** The Constant RADIO. */
	public static final Integer RADIO = 	3;
	
	/** The Constant SELECT. */
	public static final Integer SELECT = 	2;
	
	/** The Constant TEXTAREA. */
	public static final Integer TEXTAREA = 	5;
	
	/** The Constant TEXTFIELD. */
	public static final Integer TEXTFIELD = 1;

	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="fieldtypes_id", generatedId=true)
	private Integer id;

	/** The name. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="fieldtypes_name", canBeNull=false)
	private String name;
	
	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
}

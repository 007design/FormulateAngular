package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class TriggerType.
 */
@DatabaseTable (tableName="triggertypes")
public class TriggerType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant URL. */
	public static final Integer URL = 		 3;
	
	/** The Constant VALUE. */
	public static final Integer VALUE = 	 1;
	
	/** The Constant VALUESET. */
	public static final Integer VALUESET =	 2;

	/** The id. */
	@JsonView({
		JsonViews.View.class})
	@DatabaseField (columnName="triggertypes_id", generatedId=true)
	private Integer id;
	
	/** The name. */
	@JsonView({
		JsonViews.View.class})
	@DatabaseField (columnName="triggertypes_name", canBeNull=false)
	private String name;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) { this.name = name; }	
	
}

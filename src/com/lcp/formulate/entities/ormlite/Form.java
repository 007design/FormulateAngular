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
 * The Class Form.
 */
@DatabaseTable (tableName="forms")
public class Form implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The deleted. */
	@DatabaseField (columnName="forms_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;
	
	/** The fields. */
	@JsonView({
		JsonViews.Form.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="form", eager=true)
	private Collection<Field> fields;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,

		JsonViews.Form.class,
		JsonViews.FormsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="forms_id", generatedId=true)
	private Integer id;

	/** The name. */
	@JsonView({
		JsonViews.FormsList.class,
		JsonViews.Form.class})
	@DatabaseField (columnName="forms_name", canBeNull=false)
	private String name;
	
	/** The views. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.FormsList.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="form", eager=true)
	private Collection<View> views;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getId().equals(((Form)obj).getId());
	}

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() { return deleted; }
	
	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public Collection<Field> getFields() { return this.fields; }
	
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
	 * Gets the views.
	 *
	 * @return the views
	 */
	public Collection<View> getViews() { return this.views; }
	
	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) { this.deleted = deleted; }

	/**
	 * Sets the fields.
	 *
	 * @param fields the new fields
	 */
	public void setFields(Collection<Field> fields) { this.fields = fields; }
	
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
	

	/**
	 * Sets the views.
	 *
	 * @param views the new views
	 */
	public void setViews(Collection<View> views) { this.views = views; }

}

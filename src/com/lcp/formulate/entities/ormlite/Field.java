package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class Field.
 */
@DatabaseTable (tableName="fields")
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The deleted. */
	@DatabaseField (columnName="fields_deleted", canBeNull=false, defaultValue="0")
	private Boolean deleted;
	
	/** The field options. */
	@JsonView({
		JsonViews.Form.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="field", eager=true)
	private Collection<FieldOption> fieldOptions;
	
	/** The form. */
	@JsonBackReference
	@DatabaseField (columnName="forms_id", canBeNull=false, foreign=true)
	private Form form;

	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		JsonViews.ViewFields.class,
		
		JsonViews.Form.class,
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class,
		JsonViews.Filters.class,
		JsonViews.Notifications.class,
		JsonViews.Events.class})
	@DatabaseField (columnName="fields_id", generatedId=true)
	private Integer id;
	
	/** The name. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		
		JsonViews.Form.class,
		JsonViews.Filters.class,
		JsonViews.Notifications.class,
		JsonViews.Events.class})
	@DatabaseField (columnName="fields_name", canBeNull=false)
	private String name;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getId().equals(((Field)obj).getId());
	}

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public Boolean getDeleted() { return deleted; }	
	
	/**
	 * Gets the field options.
	 *
	 * @return the field options
	 */
	public Collection<FieldOption> getFieldOptions() { return this.fieldOptions; }
	
	/**
	 * Gets the form.
	 *
	 * @return the form
	 */
	public Form getForm() { return form; }

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
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(Boolean deleted) { this.deleted = deleted; }
	
	/**
	 * Sets the field options.
	 *
	 * @param fieldOptions the new field options
	 */
	public void setFieldOptions(Collection<FieldOption> fieldOptions) { this.fieldOptions = fieldOptions; }
	
	/**
	 * Sets the form.
	 *
	 * @param form the new form
	 */
	public void setForm(Form form) { this.form = form; }
	
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

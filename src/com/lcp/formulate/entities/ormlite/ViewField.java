package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.util.ViewFieldDao;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewField.
 */
@DatabaseTable (tableName="viewfields", daoClass=ViewFieldDao.class)
public class ViewField implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The custom. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_custom", canBeNull=false, defaultValue="0")
	private boolean custom;;

	/** The deleted. */
	@DatabaseField (columnName="viewfields_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;
	
	/** The description. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class,
		
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="viewfields_description", canBeNull=true)
	private String description;
	
	/** The editable. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_editable", canBeNull=false, defaultValue="1")
	private boolean editable;
	
	/** The field. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class,
		
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="fields_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Field field;

	/** The field type. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="fieldtypes_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private FieldType fieldType;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class,
		JsonViews.Triggers.class,
		
		JsonViews.Filters.class,
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="viewfields_id", generatedId=true)
	private Integer id;
	
	/** The label. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class,

		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="viewfields_label", canBeNull=true)
	private String label;

	/** The reg ex. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_regex", canBeNull=false, defaultValue=".+")
	private String regEx;
	
	/** The required. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_required", canBeNull=false, defaultValue="0")
	private boolean required;
	
	/** The script. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_script", canBeNull=true)
	private String script;

	/** The sequence. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_sequence", canBeNull=false, defaultValue="1")
	private Integer sequence;

	/** The triggers. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Triggers.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="viewField", eager=true)
	private Collection<Trigger> triggers;


	/** The value. */
	@JsonView({
		JsonViews.View.class})
	private String value;


	/** The view. */
	@JsonBackReference
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private View view;


	/** The view options. */
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="viewField", eager=true)
	private Collection<ViewOption> viewOptions;

	/** The visibility. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.ViewFields.class})
	@DatabaseField (columnName="viewfields_visibility", canBeNull=false, defaultValue="1")
	private boolean visibility;

	/** The visibility conditions. */
	@JsonView({
		JsonViews.View.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="viewField", eager=true)
	private Collection<VisibilityCondition> visibilityConditions;


	/**
	 * Instantiates a new view field.
	 */
	public ViewField(){}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return ((ViewField)obj).getId() == getId();
	}
	
	/**
	 * Gets the custom.
	 *
	 * @return the custom
	 */
	public boolean getCustom() { return custom; }
	
	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() { return deleted; }
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the editable.
	 *
	 * @return the editable
	 */
	public boolean getEditable() { return editable; }

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public Field getField() { return field; }
	
	/**
	 * Gets the field type.
	 *
	 * @return the field type
	 */
	public FieldType getFieldType() { return fieldType; }
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() { return label; }
	
	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	@JsonView({
		JsonViews.View.class})
	public List<Option> getOptions() {
		List<Option> options = new ArrayList<Option>();
		options.addAll( getField().getFieldOptions() );
		options.addAll( getViewOptions() );
		return options;
	}
	
	/**
	 * Gets the reg ex.
	 *
	 * @return the reg ex
	 */
	public String getRegEx() {
		return regEx;
	}
	
	/**
	 * Gets the required.
	 *
	 * @return the required
	 */
	public boolean getRequired() { return required; }

	/**
	 * Gets the script.
	 *
	 * @return the script
	 */
	public String getScript() { return script; }
	
	/**
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public Integer getSequence() { return sequence; }
	
	/**
	 * Gets the triggers.
	 *
	 * @return the triggers
	 */
	public Collection<Trigger> getTriggers() { return this.triggers; }

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() { 
		if (this.value == null) {
			for (Option o : getOptions())
				if (o.getDefaultValue())
					this.value = o.getValue();
		}
		return this.value; 
	}
	
	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() { return view; }

	/**
	 * Gets the view options.
	 *
	 * @return the view options
	 */
	public Collection<ViewOption> getViewOptions() { return this.viewOptions; }
	
	/**
	 * Gets the visibility.
	 *
	 * @return the visibility
	 */
	public boolean getVisibility() { return visibility; }

	/**
	 * Gets the visibility conditions.
	 *
	 * @return the visibility conditions
	 */
	public Collection<VisibilityCondition> getVisibilityConditions() { return this.visibilityConditions; }
	
	/**
	 * Sets the custom.
	 *
	 * @param custom the new custom
	 */
	public void setCustom(boolean custom) { this.custom = custom; }

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) { this.deleted = deleted; }
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the editable.
	 *
	 * @param editable the new editable
	 */
	public void setEditable(boolean editable) { this.editable = editable; }
	
	/**
	 * Sets the field.
	 *
	 * @param field the new field
	 */
	public void setField(Field field) { this.field = field; }
	
	/**
	 * Sets the field type.
	 *
	 * @param fieldType the new field type
	 */
	public void setFieldType(FieldType fieldType) { this.fieldType = fieldType; }
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	public void setLabel(String label) { this.label = label; }
	
	/**
	 * Sets the reg ex.
	 *
	 * @param regEx the new reg ex
	 */
	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}
	
	/**
	 * Sets the required.
	 *
	 * @param required the new required
	 */
	public void setRequired(boolean required) { this.required = required; }
	
	/**
	 * Sets the script.
	 *
	 * @param script the new script
	 */
	public void setScript(String script) { this.script = script; }
	
	/**
	 * Sets the sequence.
	 *
	 * @param sequence the new sequence
	 */
	public void setSequence(Integer sequence) { this.sequence = sequence; }
	
	/**
	 * Sets the triggers.
	 *
	 * @param triggers the new triggers
	 */
	public void setTriggers(Collection<Trigger> triggers) { this.triggers = triggers; }
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) { this.value = value; }
	
	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) { this.view = view; }
	
	/**
	 * Sets the view options.
	 *
	 * @param viewOptions the new view options
	 */
	public void setViewOptions(Collection<ViewOption> viewOptions) { this.viewOptions = viewOptions; }
	
	/**
	 * Sets the visibility.
	 *
	 * @param visibility the new visibility
	 */
	public void setVisibility(boolean visibility) { this.visibility = visibility; }
	
	/**
	 * Sets the visibility conditions.
	 *
	 * @param visibilityConditions the new visibility conditions
	 */
	public void setVisibilityConditions(Collection<VisibilityCondition> visibilityConditions) { this.visibilityConditions = visibilityConditions; }
	
}

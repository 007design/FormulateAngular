package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.util.ValueDao;

// TODO: Auto-generated Javadoc
/**
 * The Class Value.
 */
@DatabaseTable (tableName="current_values", daoClass=ValueDao.class)
public class Value implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The deleted. */
	@DatabaseField (columnName="values_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;

	/** The field. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="fields_id", canBeNull=true, foreign=true, foreignAutoRefresh=true)
	private Field field;
	
	/** The id. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="values_id", generatedId=true)
	private Integer id;
	
	/** The submission. */
	@JsonBackReference
	@DatabaseField (columnName="submissions_id", canBeNull=false, foreign=true)
	private Submission submission;

	/** The timestamp. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="values_timestamp", canBeNull=false)
	private Date timestamp;
	
	/** The value. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="values_value", canBeNull=false)
	private String value;
	
	/** The value set. */
	@JsonView({
		JsonViews.Submissions.class})
	@DatabaseField (columnName="valuesets_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	@JsonBackReference
	private ValueSet valueSet;

	/** The view field. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="viewfields_id", canBeNull=true, foreign=true, foreignAutoRefresh=true)
	private ViewField viewField;
	
	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() {
		return deleted;
	}
	
	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public Field getField() {  
		if (this.field == null)
			return getViewField().getField();
		return field; 
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Gets the submission.
	 *
	 * @return the submission
	 */
	public Submission getSubmission() { return submission; }
	
	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Gets the value set.
	 *
	 * @return the value set
	 */
	public ValueSet getValueSet() { return valueSet; }

	/**
	 * Gets the view field.
	 *
	 * @return the view field
	 */
	public ViewField getViewField() { return viewField; }

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

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
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the submission.
	 *
	 * @param submission the new submission
	 */
	public void setSubmission(Submission submission) { this.submission = submission; }

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
	 * Sets the value set.
	 *
	 * @param valueSet the new value set
	 */
	public void setValueSet(ValueSet valueSet) { this.valueSet = valueSet; }
	
	/**
	 * Sets the view field.
	 *
	 * @param viewField the new view field
	 */
	public void setViewField(ViewField viewField) { this.viewField = viewField; }
}


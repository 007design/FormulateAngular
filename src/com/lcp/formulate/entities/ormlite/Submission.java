package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class Submission.
 */
@DatabaseTable (tableName="submissions_list")
public class Submission implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The deleted. */
	@DatabaseField (columnName="submissions_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;

	/** The form. */
	@DatabaseField (columnName="forms_id", foreign=true, foreignAutoRefresh=true, persisted=false)
	private Form form;
	
	/** The id. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="submissions_id", generatedId=true)
	private Integer id;
	
	/** The timestamp. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="submissions_timestamp", canBeNull=false)
	private Date timestamp;

	/** The user. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="users_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private User user;
	
	/**
	 * A submission will have a list of values when it is submitted.
	 */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="submission", eager=true)
	private Collection<Value> values;
	
	/** All the valuesets for a submission. */
	@JsonView({
		JsonViews.Submission.class})
	@ForeignCollectionField(foreignFieldName="submission", eager=true, orderColumnName="valuesets_id")
	private Collection<ValueSet> valueSets;
	
	/** The view. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private View view;
	
	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() {
		return deleted;
	}
	
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
	public Integer getId() {
		return id;
	}
	
	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() { return user; }
	
	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Collection<Value> getValues() { return this.values; }	
	
	/**
	 * Gets the value sets.
	 *
	 * @return the value sets
	 */
	public Collection<ValueSet> getValueSets() { return this.valueSets; }

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() { return view; }
	
	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
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
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) { this.user = user; }

	/**
	 * Sets the values.
	 *
	 * @param values the new values
	 */
	public void setValues(Collection<Value> values) { this.values = values; }
	
	/**
	 * Sets the value sets.
	 *
	 * @param valueSets the new value sets
	 */
	public void setValueSets(Collection<ValueSet> valueSets) { this.valueSets = valueSets; }
	
	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) { this.view = view; }
}

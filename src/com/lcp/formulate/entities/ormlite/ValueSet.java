package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class ValueSet.
 */
@DatabaseTable (tableName="valuesets")
public class ValueSet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="valuesets_id", generatedId=true)
	private Integer id;
	
	/** The submission. */
	@JsonBackReference
	@DatabaseField (columnName="submissions_id", canBeNull=false, foreign=true)
	private Submission submission;
	
	/** The timestamp. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="valuesets_timestamp", canBeNull=false)
	private Date timestamp;

	/** The user. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="users_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private User user;
	
	/** The values. */
	@JsonView({
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="valueSet", eager=true)
	private Collection<Value> values;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }

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
	public Date getTimestamp() { return timestamp; }
	
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
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
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
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
	
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
	
	
}

package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.util.TargetSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class VisibilityCondition.
 */
@DatabaseTable(tableName="visibilityconditions")
public class VisibilityCondition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant EQUAL_TO. */
	public static final Integer EQUAL_TO =		1;
	
	/** The Constant GREATER_THAN. */
	public static final Integer GREATER_THAN =	3;
	
	/** The Constant JOIN_AND. */
	public static final Integer JOIN_AND =		0;
	
	/** The Constant JOIN_OR. */
	public static final Integer JOIN_OR =		1;
	
	/** The Constant LESS_THAN. */
	public static final Integer LESS_THAN =		4;
	
	/** The Constant NOT_EQUAL. */
	public static final Integer NOT_EQUAL =		2;
	
	/** The comparison. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.VisibilityConditions.class})
	@DatabaseField (columnName="visibilityconditions_comparison", canBeNull=false, defaultValue="0")
	private Integer comparison;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.VisibilityConditions.class})
	@DatabaseField (columnName="visibilityconditions_id", generatedId=true)
	private Integer id;
	
	/** The join. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.VisibilityConditions.class})
	@DatabaseField (columnName="visibilityconditions_join", canBeNull=false, defaultValue="1")
	private int join = 0;

	/** The target. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.VisibilityConditions.class})
	@DatabaseField (columnName="visibilityconditions_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	@JsonSerialize(using=TargetSerializer.class)
	private ViewField target;
	
	/** The value. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.VisibilityConditions.class})
	@DatabaseField (columnName="visibilityconditions_value", canBeNull=false)
	private String value;
	
	/** The view field. */
	@JsonBackReference
	@DatabaseField (columnName="viewfields_id", canBeNull=false, foreign=true)
	private ViewField viewField;

	/**
	 * Gets the comparison.
	 *
	 * @return the comparison
	 */
	public Integer getComparison() { return comparison; }
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() { return id; }
	
	/**
	 * Gets the join.
	 *
	 * @return the join
	 */
	public int getJoin() { return join; }

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public ViewField getTarget() { return target; }
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() { return value; }
	
	/**
	 * Gets the view field.
	 *
	 * @return the view field
	 */
	public ViewField getViewField() { return viewField; }

	/**
	 * Sets the comparison.
	 *
	 * @param comparison the new comparison
	 */
	public void setComparison(Integer comparison) { this.comparison = comparison; }
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * Sets the join.
	 *
	 * @param join the new join
	 */
	public void setJoin(int join) { this.join = join; }

	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(ViewField target) { this.target = target; }
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) { this.value = value; }
	
	/**
	 * Sets the view field.
	 *
	 * @param viewField the new view field
	 */
	public void setViewField(ViewField viewField) { this.viewField = viewField; }
}

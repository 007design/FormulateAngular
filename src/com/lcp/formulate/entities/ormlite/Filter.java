package com.lcp.formulate.entities.ormlite;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lcp.formulate.entities.JsonViews;

// TODO: Auto-generated Javadoc
/**
 * The Class Filter.
 */
@DatabaseTable (tableName="filters")
public class Filter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** The Constant EQUAL_TO. */
	public static final Integer EQUAL_TO =		1;
	
	/** The Constant GREATER_THAN. */
	public static final Integer GREATER_THAN =	3;
	
	/** The Constant LESS_THAN. */
	public static final Integer LESS_THAN =		4;
	
	/** The Constant NOT_EQUAL. */
	public static final Integer NOT_EQUAL =		2;

	/** The comparison. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_comparison", canBeNull=false)
	private Integer comparison;

	/** The id. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_id", generatedId=true)
	private Integer id;
	
	/** The label. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_label", canBeNull=false)
	private String label;
	
	/** The static flag. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_static", canBeNull=false, defaultValue="0")
	private boolean staticFlag;

	/** The target. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_target", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Field target;
	
	/** The value. */
	@JsonView({
		JsonViews.Filters.class})
	@DatabaseField (columnName="filters_value", canBeNull=false)
	private String value;
	
	/** The view. */
	@JsonBackReference
	@DatabaseField (columnName="views_id", canBeNull=false, foreign=true)
	private View view;

	/**
	 * Gets the comparison.
	 *
	 * @return the comparison
	 */
	public Integer getComparison() {
		return comparison;
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
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Gets the static flag.
	 *
	 * @return the static flag
	 */
	public boolean getStaticFlag() {
		return staticFlag;
	}
	
	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Field getTarget() { return target; }

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() { return view; }

	/**
	 * Sets the comparison.
	 *
	 * @param comparison the new comparison
	 */
	public void setComparison(Integer comparison) {
		this.comparison = comparison;
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
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Sets the static flag.
	 *
	 * @param staticFlag the new static flag
	 */
	public void setStaticFlag(boolean staticFlag) {
		this.staticFlag = staticFlag;
	}

	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(Field target) { this.target = target; }
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(View view) { this.view = view; }
}

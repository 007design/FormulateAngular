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
 * The Class Account.
 */
@DatabaseTable (tableName="accounts")
public class Account implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The alias. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.FormsList.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="accounts_alias", canBeNull=false)
	private String alias;
	
	/** The deleted. */
	@DatabaseField (columnName="accounts_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;
	
	/** The id. */
	@JsonView({
		JsonViews.View.class,
		JsonViews.Editor.class,
		
		JsonViews.Form.class,
		JsonViews.User.class,
		JsonViews.UsersList.class,
		JsonViews.FormsList.class,
		JsonViews.ViewsList.class,
		JsonViews.View.class,
		JsonViews.UserViews.class})
	@DatabaseField (columnName="accounts_id", generatedId=true)
	private Integer id;
	
	/** The name. */
	@JsonView({
		JsonViews.Form.class,
		JsonViews.FormsList.class})
	@DatabaseField (columnName="accounts_name", canBeNull=false)
	private String name;
	
	/** The users. */
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="account", eager=true)
	private Collection<User> users;
	
	/**
	 * Instantiates a new account.
	 */
	public Account() {}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getId().equals(((Account)obj).getId());
	}

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias() { return alias; }
	
	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() { return deleted; }
	
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
	 * Gets the users.
	 *
	 * @return the users
	 */
	public Collection<User> getUsers() { return this.users; }
	
	/**
	 * Sets the alias.
	 *
	 * @param alias the new alias
	 */
	public void setAlias(String alias) { this.alias = alias; }

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) { this.deleted = deleted; }
	
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
	 * Sets the users.
	 *
	 * @param users the new users
	 */
	public void setUsers(Collection<User> users) { this.users = users; }	
}

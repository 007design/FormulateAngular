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
 * The Class User.
 */
@DatabaseTable (tableName="users")
public class User implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The account. */
	@JsonView({
		JsonViews.User.class,
		JsonViews.UsersList.class})
	@DatabaseField (columnName="accounts_id", canBeNull=false, foreign=true, foreignAutoRefresh=true)
	private Account account;
	
	/** The admin. */
	@JsonView({
		JsonViews.UsersList.class,
		
		JsonViews.User.class})
	@DatabaseField (columnName="users_admin", canBeNull=false, defaultValue="0")
	private boolean admin = false;
	
	/** The deleted. */
	@DatabaseField (columnName="users_deleted", canBeNull=false, defaultValue="0")
	private boolean deleted;

	/** The email. */
	@JsonView({
		JsonViews.User.class,
		JsonViews.UsersList.class,
		
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="users_email", canBeNull=false)
	private String email = "";
	
	/** The id. */
	@JsonView({
		JsonViews.UsersList.class,
		JsonViews.ViewUsers.class,
		
		JsonViews.User.class,
		JsonViews.Submissions.class,
		JsonViews.Submission.class,
		JsonViews.Update.class})
	@DatabaseField (columnName="users_id", generatedId=true)
	private Integer id;
	
	/** The name. */
	@JsonView({
		JsonViews.UsersList.class,
		JsonViews.ViewUsers.class,
		
		JsonViews.User.class,
		JsonViews.Submissions.class,
		JsonViews.Submission.class})
	@DatabaseField (columnName="users_name", canBeNull=false)
	private String name;

	/** The password. */
	@DatabaseField (columnName="users_password", canBeNull=false, defaultValue="")
	private String password = "";
	
	/** The system user. */
	@DatabaseField (columnName="users_systemUser", canBeNull=true)
	private Boolean systemUser;
	
	/** The user views. */
	@JsonView({
		JsonViews.User.class})
	@JsonManagedReference
	@ForeignCollectionField(foreignFieldName="user", eager=true)
	private Collection<UserView> userViews;

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getId().equals(((User)obj).getId());
	}
	
	/**
	 * Gets the account.
	 *
	 * @return the account
	 */
	public Account getAccount() { return account; }
	
	/**
	 * Gets the admin.
	 *
	 * @return the admin
	 */
	public boolean getAdmin() { return admin; }

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public boolean getDeleted() { return deleted; }
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() { return email; }
	
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
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() { return password; }
	
	/**
	 * Gets the system user.
	 *
	 * @return the system user
	 */
	public Boolean getSystemUser() { return systemUser; }

	/**
	 * Gets the user views.
	 *
	 * @return the user views
	 */
	public Collection<UserView> getUserViews() { return this.userViews; }
	
	/**
	 * Sets the account.
	 *
	 * @param account the new account
	 */
	public void setAccount(Account account) { this.account = account; }
	
	/**
	 * Sets the admin.
	 *
	 * @param admin the new admin
	 */
	public void setAdmin(boolean admin) { this.admin = admin; }
	
	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(boolean deleted) { this.deleted = deleted; }
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) { this.email = email; }
	
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
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) { this.password = password; }
	
	/**
	 * Sets the system user.
	 *
	 * @param systemUser the new system user
	 */
	public void setSystemUser(Boolean systemUser) { this.systemUser = systemUser; }

	/**
	 * Sets the user views.
	 *
	 * @param userViews the new user views
	 */
	public void setUserViews(Collection<UserView> userViews) { this.userViews = userViews; }

}

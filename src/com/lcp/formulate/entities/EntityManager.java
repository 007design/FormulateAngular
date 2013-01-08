package com.lcp.formulate.entities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sourceforge.stripes.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.Submission;
import com.lcp.formulate.entities.ormlite.User;
import com.lcp.formulate.entities.ormlite.Value;
import com.lcp.formulate.entities.ormlite.ValueSet;
import com.lcp.formulate.entities.ormlite.View;

public class EntityManager {
	private static final Log log = Log.getInstance(EntityManager.class);
	
	public static JdbcConnectionSource getConnection() throws SQLException, NamingException {		
//		ResourceBundle rb=ResourceBundle.getBundle("connection_config");
		
//		String serverName=rb.getString("server.name");
//		String port=rb.getString("port.no");
//		String databaseName=rb.getString("database.name");
//		String userName=rb.getString("user.name");
//		String password=rb.getString("user.password");

		Context env = (Context)new InitialContext().lookup("java:comp/env");
		
		String serverName = (String)env.lookup("DBServerName");
		String port = (String)env.lookup("DBServerPort");
		String databaseName = (String)env.lookup("DBServerDatabase");
		String userName = (String)env.lookup("DBServerUser");
		String password = (String)env.lookup("DBServerPassword");
		
//		String serverName="localhost";
//		String port="3306";
//		String databaseName="formulate";
//		String userName="formulate";
//		String password="formulate";
		
		return new JdbcConnectionSource("jdbc:mysql://"+serverName+":"+port+"/"+databaseName, userName, password);
	}

	public static User getSystemUser(View view) {
		ConnectionSource conn = null;
		try {	
			conn = EntityManager.getConnection();
			/**
			 * Check if the user may access this view
			 * Get their userviews
			 */
			Dao<User, String> dao = DaoManager.createDao(conn, User.class);
			User user = dao.queryForFirst( 
					dao.queryBuilder().where()
//					.eq("accounts_id", view.getForm().getAccount().getId()).and()
					.eq("users_systemUser", true).prepare() );

			return user;
		} catch (SQLException x) {
			x.printStackTrace();
			return null;
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try { conn.close(); } catch (Exception x) {}
		}
		return null;
	}
		
	public static List<Submission> getSubmissions(Account account, View view, String search) {
		// Populate each submission's values list
		ConnectionSource connectionSource = null;
		try {				
			connectionSource = EntityManager.getConnection();
			Dao<Submission, String> subDao = DaoManager.createDao(connectionSource, Submission.class);

			// Get all the submissions for the view's form
			QueryBuilder<Submission, String> qb = subDao.queryBuilder();
			qb.where().eq("submissions_deleted", 0);
			qb.orderBy("submissions_id", true);
			
			List<Submission> allSubmissions = subDao.query( qb.prepare() );
			
			log.info("All submissions retreived: "+allSubmissions.size());
			
			List<Submission> filteredSubmissions = new ArrayList<Submission>();
			
			/**
			 * Process each submission and match against the account and search terms
			 */
			for (Submission submission : allSubmissions) {
				/**
				 * If the user is not in the admin account and their account
				 * doesn't match the account of the view used on this submission
				 * Skip this record
				 */
				if (account.getId() > 1)
					if (!submission.getView().getAccount().equals(account))
						continue;
				
				// If a search was supplied, match the value against the submission's values
				if (search == null) {
					log.info("No search supplied");
					filteredSubmissions.add(submission);
				} else {
					log.info("Applying search");
					for (Value v : submission.getValues()) {
												
						if (v.getValue().matches("(?i).*"+search+".*")) {
							filteredSubmissions.add(submission);
							log.info("    "+v.getValue() + ":" + search);
						}
					}
				}
			}
			
			return filteredSubmissions;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			try { connectionSource.close(); } catch (SQLException e) {}
		}	
		return null;
	}

	public static List<User> getUsers(Account account) {
		ConnectionSource conn = null;
		try {	
			conn = EntityManager.getConnection();
			/**
			 * Check if the user may access this view
			 * Get their userviews
			 */
			Dao<User, String> dao = DaoManager.createDao(conn, User.class);
			return dao.queryForEq("accounts_id", account.getId());
		} catch (SQLException x) {
			x.printStackTrace();
			return null;
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try { conn.close(); } catch (Exception x) {}
		}
		return null;
	}

	public static List<Value> getValues(ValueSet valSet) {
		ConnectionSource conn = null;
		try {	
			conn = EntityManager.getConnection();
			/**
			 * Check if the user may access this view
			 * Get their userviews
			 */
			Dao<Value, String> dao = DaoManager.createDao(conn, Value.class);
			List<Value> values = dao.queryForEq("valuesets_id", valSet.getId());
			
			System.err.println("Values");
			for (Value v : values) {
				System.err.println(v.getValue());
			}
			
			return values;
		} catch (SQLException x) {
			x.printStackTrace();
			return null;
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			try { conn.close(); } catch (Exception x) {}
		}
		return null;
	}
}
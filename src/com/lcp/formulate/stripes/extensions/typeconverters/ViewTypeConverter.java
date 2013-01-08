package com.lcp.formulate.stripes.extensions.typeconverters;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import javax.naming.NamingException;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.entities.ormlite.View;

public class ViewTypeConverter implements TypeConverter<View> {
	private static Logger log = org.apache.log4j.Logger.getLogger(ViewTypeConverter.class);
	
	private Account account;
//	public void setAccount(Account account) { this.account = account; }
//	public Account getAccount() { return account; }
	
	public ViewTypeConverter() {}
	public ViewTypeConverter(Account account) {
		this.account = account;
	}
	
	private JdbcConnectionSource conn;
	
	@Override
	public View convert(String arg0, Class<? extends View> type, Collection<ValidationError> errors) {
		
		Dao<View, String> vDao = null;
		try {
			log.info("    Looking up view: "+arg0);
			conn = EntityManager.getConnection();
			vDao = DaoManager.createDao(conn, View.class);
			
			if (account == null)
				return vDao.queryForId(arg0);
			
			PreparedQuery<View> p2 = vDao.queryBuilder().where().idEq(arg0).and().eq("accounts_id", account.getId()).prepare();
			log.info("    "+p2.getStatement());
			return vDao.queryForFirst( p2 );
		} catch (SQLException noId) {
			log.info("    View not found");
			try {
				if (account == null) {
					log.info("Tried to convert view "+arg0+" with no account.");
					return null;
				}
				
				PreparedQuery<View> p3 = vDao.queryBuilder().where().eq("views_alias", arg0).and().eq("accounts_id", account.getId()).prepare();
				log.info("    "+p3.getStatement());
				return vDao.queryForFirst( p3 ); 
			} catch (Exception noAlias) {
				log.info("    View not found, validation error");
				errors.add(new SimpleError("The id {1} is not valid."));
			}
		} catch (NamingException x) {
			x.printStackTrace();
		} finally {
			try { conn.close(); } catch (SQLException e) {}
		}

		log.info("    View not found");
		return null;
	}

	@Override
	public void setLocale(Locale arg0) { }


}
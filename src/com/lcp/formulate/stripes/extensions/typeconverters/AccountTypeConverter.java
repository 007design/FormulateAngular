package com.lcp.formulate.stripes.extensions.typeconverters;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import javax.naming.NamingException;

import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
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

@Intercepts(LifecycleStage.BindingAndValidation)
public class AccountTypeConverter implements TypeConverter<Account> {
	private static Logger log = org.apache.log4j.Logger.getLogger(AccountTypeConverter.class);
	
	@Override
	public Account convert(String arg0, Class<? extends Account> type,
			Collection<ValidationError> errors) {
		JdbcConnectionSource conn = null;
		Dao<Account, String> dao = null;
		try {
			log.debug("AccountTypeConverter.convert()");
			conn = EntityManager.getConnection();
			dao = DaoManager.createDao(conn, Account.class);
			PreparedQuery<Account> p = dao.queryBuilder().where().idEq(arg0).prepare();
			log.debug("    "+p.getStatement());
			return dao.queryForFirst( p );
		} catch (SQLException x) {
			try {
				return dao.queryForFirst( dao.queryBuilder().where().eq("accounts_alias", arg0).prepare() );
			} catch (SQLException x2) {
				errors.add(new SimpleError("The id {1} is not valid."));
			}
		} catch (NamingException e) {
			log.debug("Naming Exception on AccountTypeConverter");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}
		return null;
	}

	@Override
	public void setLocale(Locale arg0) { }

}

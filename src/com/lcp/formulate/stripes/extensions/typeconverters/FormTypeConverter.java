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
import com.lcp.formulate.entities.ormlite.Form;


public class FormTypeConverter implements TypeConverter<Form> {
	private static Logger log = org.apache.log4j.Logger.getLogger(FormTypeConverter.class);
	@Override
	public Form convert(String arg0, Class<? extends Form> type,
			Collection<ValidationError> errors) {
		JdbcConnectionSource conn = null;
		Dao<Form, String> dao = null;
		try {
			conn = EntityManager.getConnection();
			dao = DaoManager.createDao(conn, Form.class);
			PreparedQuery<Form> p = dao.queryBuilder().where().idEq(arg0).prepare();
			log.debug("    "+p.getStatement());			
			return dao.queryForFirst( p );
		} catch (SQLException nfe) {
			try {
				return dao.queryForFirst( dao.queryBuilder().where().eq("forms_alias", arg0).prepare() );
			} catch (SQLException x2) {
				errors.add(new SimpleError("The id {1} is not valid."));
				return null;	
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {}
		}
	}

	@Override
	public void setLocale(Locale arg0) { }

}
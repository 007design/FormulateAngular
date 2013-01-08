package com.lcp.formulate.stripes.extensions.typeconverters;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Submission;

public class SubmissionTypeConverter implements TypeConverter<Submission> {

	@Override
	public Submission convert(String arg0, Class<? extends Submission> type,
			Collection<ValidationError> errors) {

		JdbcConnectionSource conn = null;
		try {
			conn = EntityManager.getConnection();
			Dao<Submission, String> dao = DaoManager.createDao(conn, Submission.class);
						
			return dao.queryForId( arg0 );
		} catch (Exception nfe) {
			errors.add(new SimpleError("The id {1} is not valid."));
			return null;
		}
	}

	@Override
	public void setLocale(Locale arg0) { }

}

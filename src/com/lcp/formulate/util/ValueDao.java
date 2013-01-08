package com.lcp.formulate.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.ormlite.Value;

public class ValueDao extends BaseDaoImpl<Value, String> {
	public ValueDao(ConnectionSource connectionSource,
			Class<Value> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	@Override
	public int create(Value data) throws SQLException {
		try {
			Dao<Value, String> dao = DaoManager.createDao(connectionSource, Value.class);
			
			return dao.executeRaw("INSERT INTO `values` (" +
					"`submissions_id` ,`viewfields_id` ,`fields_id` ,`valuesets_id` ," +
					"`values_value` ) VALUES (?,?,?,?,?)",
					data.getSubmission().getId().toString(),
					
					(data.getViewField() == null) ? null : data.getViewField().getId().toString(),
					
					data.getField().getId().toString(),
					data.getValueSet().getId().toString(),
					data.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}

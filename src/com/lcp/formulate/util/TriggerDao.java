package com.lcp.formulate.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.ormlite.Trigger;
import com.lcp.formulate.entities.ormlite.TriggerCondition;
import com.lcp.formulate.entities.ormlite.TriggerValue;

public class TriggerDao extends BaseDaoImpl<Trigger, String> {
	public TriggerDao(ConnectionSource connectionSource,
			Class<Trigger> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	@Override
	public int delete(Trigger data) throws SQLException {
		try {
			Dao<TriggerValue, String> tvDao = DaoManager.createDao(connectionSource, TriggerValue.class);
			for (TriggerValue opt : data.getTriggerValues())
				tvDao.delete(opt);
			
			Dao<TriggerCondition, String> tcDao = DaoManager.createDao(connectionSource, TriggerCondition.class);
			for (TriggerCondition t : data.getTriggerConditions())
				tcDao.delete(t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.delete(data);
	}
}

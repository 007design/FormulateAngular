package com.lcp.formulate.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.ormlite.Trigger;
import com.lcp.formulate.entities.ormlite.ViewField;
import com.lcp.formulate.entities.ormlite.ViewOption;

public class ViewFieldDao extends BaseDaoImpl<ViewField, String> {

	public ViewFieldDao(ConnectionSource connectionSource,
			Class<ViewField> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	@Override
	public int delete(ViewField data) throws SQLException {
		try {
			Dao<ViewOption, String> voDao = DaoManager.createDao(connectionSource, ViewOption.class);
			for (ViewOption opt : data.getViewOptions())
				voDao.delete(opt);
			
			Dao<Trigger, String> tDao = DaoManager.createDao(connectionSource, Trigger.class);
			for (Trigger t : data.getTriggers())
				tDao.delete(t);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data.setDeleted(true);
		Dao<ViewField, String> dao = DaoManager.createDao(connectionSource, ViewField.class);
		return dao.update(data);
	}

	
	
}

package com.lcp.formulate.stripes.action.data;

import java.sql.SQLException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.JsonViews;
import com.lcp.formulate.entities.ormlite.View;
import com.lcp.formulate.entities.ormlite.ViewEvent;
import com.lcp.formulate.stripes.action.BaseActionBean;
import com.lcp.formulate.stripes.exceptions.AjaxException;

@UrlBinding("/data/events/{view}")
public class EventsDataAction extends BaseActionBean implements DataAction {
	private static Logger log = org.apache.log4j.Logger.getLogger(EventsDataAction.class);

	@Validate(required=true)
	private View view;
	public void setView(View view) { this.view =view; }
	public View getView() { return view; }
	
	private ConnectionSource conn;	
	
	@DefaultHandler
	public Resolution defaultResolution() throws AjaxException {
		log.info("EventsDataAction");
		try {
			conn = EntityManager.getConnection();
			Dao<ViewEvent, String> dao = DaoManager.createDao(conn, ViewEvent.class);
			
			String string = new ObjectMapper().configure(Feature.DEFAULT_VIEW_INCLUSION, false)
						.writerWithView(JsonViews.Events.class)
							.writeValueAsString( dao.queryForEq("views_id", getView().getId()) );
		
			log.info(string);
			return new StreamingResolution("text/json",string);
		} catch (Exception e) {
			e.printStackTrace();
			getContext().getValidationErrors().add("entity", new SimpleError("unknown exception ["+e.getMessage()+"]"));
			throw new AjaxException(this, "events", "data error");
		} finally {
			try { conn.close(); } catch (SQLException e) {}
		}
	}
}

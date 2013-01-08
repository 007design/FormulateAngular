package com.lcp.formulate.stripes.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.Log;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lcp.formulate.entities.EntityManager;
import com.lcp.formulate.entities.ormlite.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadAction.
 */
@UrlBinding("/download/{uid}")
public class DownloadAction extends BaseActionBean {
	
	/** The Constant log. */
	private static final Log log = Log.getInstance(DownloadAction.class);
	
	/** The uid. */
	private String uid;
	
	/**
	 * Sets the uid.
	 *
	 * @param uid the new uid
	 */
	public void setUid(String uid) { this.uid = uid; }
	
	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	public String getUid() { return uid; }
	
	/**
	 * Download.
	 *
	 * @return the resolution
	 */
	@DefaultHandler
	public Resolution download() {
		log.info("DownloadAction.download");

		String savePath = getContext().getServletContext().getInitParameter("SubmissionUploadLocation");
		String filename = null;
		Integer submissionId = null; 
		
		JdbcConnectionSource connectionSource = null;
		
		try {
			connectionSource = EntityManager.getConnection();
			Dao<Value, String> dao = DaoManager.createDao(connectionSource, Value.class);

			QueryBuilder<Value, String> qb = dao.queryBuilder();
			qb.where().like("values_value", "%"+getUid()+"%");
			
			Value v = dao.queryForFirst(qb.prepare());
			
			if (v == null)
				log.info("    query for "+getUid()+" returned no records");
			else {				
				submissionId = v.getSubmission().getId();
				
				log.info("    value: "+v.getValue());
			
				Type mapType = new TypeToken<Map<String,String>>() {}.getType();
				Map<String,String> files = new Gson().fromJson(v.getValue(), mapType);
				
				log.info("    parsed value into map");
				
				for (String s : files.keySet()) {
					if (s.equals(getUid())) {
						log.info("    found key. filename: "+files.get(s));
						filename = files.get(s);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new StreamingResolution("text/plain","Exception retrieving file");
		} finally {			
			try {
				connectionSource.close();
			} catch (SQLException e) {}
		}		

		log.debug("    Trying to get file at: "+savePath+"/"+submissionId+"/"+getUid()+"."+FilenameUtils.getExtension(filename));
		
		File download = new File(savePath+"/"+submissionId, getUid()+"."+FilenameUtils.getExtension(filename));
		
		// Stream the file
		try {
			
			return new StreamingResolution(new MimetypesFileTypeMap().getContentType(download),
					new FileInputStream(download)).setFilename(filename);
		} catch (FileNotFoundException e) {
			log.debug("FileNotFoundException trying to stream the file");
			e.printStackTrace();
			return new StreamingResolution("text/plain","File not found");
		}
	}
}

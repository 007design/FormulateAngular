package com.lcp.formulate.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static Logger log = org.apache.log4j.Logger.getLogger(UploadServlet.class);
	private static final long serialVersionUID = 1L;
	
	private String uid;
	public String getUid() { return uid; }
	public void setUid(String uid) { this.uid = uid; }

	private String filename;
	public String getFilename() { return filename; }
	public void setFilename(String filename) { this.filename = filename; }
	
	private String ext;
	public String getExt() { return ext; }
	public void setExt(String ext) { this.ext = ext; }
	
	@SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("UploadServlet.doPost()");
		
        InputStream is = null;
        FileOutputStream fos = null;
        
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);

        /**
         * Create unique id
         */
    	UUID fileId = UUID.randomUUID();
        setUid(fileId.toString());
        
        log.info("    created new UID: "+fileId);

        /**
         * Handle multipart/form requests
         * Older browsers will use this method
         */
        if (isMultiPart) {
        	log.info("    handling multipart request - most likely from IE");
        	
            // Create a factory for disk-based file items
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

            /**
             * Set the file size limit in bytes.
             * This is the threshold at which the file will be temporarily stored on the disk rather than in memory
             */
            diskFileItemFactory.setSizeThreshold(1024 * 1024 * 10); // 10 Mb
         
            /**
             * Create a new file upload handler and parse the request
             */
            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);

            List<FileItem> items = null;

            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException ex) {
                ex.printStackTrace();
            }
            
            log.debug("    request parsed, processing fileitems");

            /**
             * Process the fileitems
             */
            ListIterator<FileItem> li = items.listIterator();

            while (li.hasNext()) {
                FileItem fileItem = (FileItem) li.next();
                if (!fileItem.isFormField()) {
                	// Set the file name
                	setFilename( FilenameUtils.getName(fileItem.getName()) );
                	// Set the file extension
                	setExt( FilenameUtils.getExtension(fileItem.getName()) );
                	
                	 try {
                		 // Write the file to disk
                		 fileItem.write(new File(getServletContext().getInitParameter("UploadTempLocation"), fileId.toString()+"."+getExt()));
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                }
            }
            
            log.debug("    finished writing file to disk");
        }

        /**
         * Handle async uploads from newer browsers which support it
         */
        if ("application/octet-stream".equals(request.getContentType())) {
        	log.info("    handling async upload");
        	
        	// Get the filename
            String filename = request.getHeader("X-File-Name");

        	// Set the file name
        	setFilename( FilenameUtils.getName(filename) );
            // Set the file extension
            setExt( FilenameUtils.getExtension(filename) );

            // Write the file to disk
            try {
                is = request.getInputStream();
    	        fos = new FileOutputStream(new File(getServletContext().getInitParameter("UploadTempLocation"), fileId.toString()+"."+getExt()));
                IOUtils.copy(is, fos);
            } catch (FileNotFoundException ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ex.printStackTrace();
            } catch (IOException ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ex.printStackTrace();
            } finally {
                try {
                    fos.close();
                    is.close();
                } catch (IOException ignored) {
                }
            }
            
            log.debug("    finished writing file to disk");
        }
        
        log.info("{\"success\": \"true\", \"filename\":\""+getFilename()+"\", \"uid\":\""+getUid()+"\"}");
        
        response.setContentType("text/plain");
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": \"true\", \"filename\":\""+getFilename()+"\", \"uid\":\""+getUid()+"\"}");
        out.close();
        
	}
}

package com.lcp.formulate.stripes.extensions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;
import net.sourceforge.stripes.util.HttpUtil;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.log4j.Logger;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.lcp.formulate.stripes.action.pages.EditViewAction;
import com.lcp.formulate.stripes.action.pages.LoginView;
import com.lcp.formulate.stripes.action.pages.ManagerAction;
import com.lcp.formulate.stripes.action.pages.RegisterView;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.exceptions.InvalidRequestException;
import com.lcp.formulate.stripes.exceptions.LoginException;
import com.lcp.formulate.stripes.exceptions.RegisterException;

public class MyExceptionHandler extends DefaultExceptionHandler {
	private static Logger log = org.apache.log4j.Logger.getLogger(MyExceptionHandler.class);
		
	public Resolution catchAll(Throwable exc, HttpServletRequest req, HttpServletResponse resp) {
        log.error("Exception", exc);
		exc.printStackTrace();
        
        ErrorResolution resolution = new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
        resolution.setErrorMessage("Please check your URL.");	
		return resolution;	
    }
	
	/**
	 * Attempt to forward the user to the login page
	 * throw a 500 error if something goes wrong
	 */
	public Resolution catchLoginException(LoginException x, HttpServletRequest request, HttpServletResponse response) {
        log.error("[MyExceptionHandler] LoginException\n  action: "+x.getActionBean().getClass().getName()
        		+"\n  event: "+x.getActionBean().getContext().getEventName()
        		+"\n  message: "+x.getMessage()); 
		        
//        log.info("LoginException", x);
        
        ForwardResolution resolution;
        
        try {
        	resolution = new ForwardResolution(LoginView.class);
        	resolution.addParameter("nextPage", HttpUtil.getRequestedPath(request));

	        if (x.getActionBean() instanceof EditViewAction || x.getActionBean() instanceof ManagerAction)
	        	resolution.addParameter("requirePassword", true);
	        else
	        	resolution.addParameter("requirePassword", false);
        } catch (Exception e) {
        	log.error(x);
        	return new ErrorResolution(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
		if (x.getMessage() != null)
			x.getActionBean().getContext().getMessages().add(new SimpleMessage(x.getMessage()));		
		
		return resolution;		
	}
	
	public Resolution catchRegisterException(RegisterException x, HttpServletRequest request, HttpServletResponse response) {
        log.error("[MyExceptionHandler] RegisterException\n  action: "+x.getActionBean().getClass().getName()
        		+"\n  event: "+x.getActionBean().getContext().getEventName()
        		+"\n  message: "+x.getMessage()); 

        log.error("RegisterException", x);
        
        ForwardResolution resolution;
        
		 try {
	        	resolution = new ForwardResolution(RegisterView.class);
//	        	resolution.addParameter("nextPage", HttpUtil.getRequestedPath(request));

    			resolution.addParameter("username", request.getParameter("username"));
	        } catch (Exception e) {
	        	log.error("  -Exception: "+x.getCause());
	        	log.error("  --: "+x.getMessage());
	        	return new ErrorResolution(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	        
			if (x.getMessage() != null)
				x.getActionBean().getContext().getMessages().add(new SimpleMessage(x.getMessage()));		
			
			return resolution;		
	}

	/**
	 * return a 412 error
	 */
	public Resolution catchAjaxException(AjaxException x, HttpServletRequest request, HttpServletResponse response) {
        log.error("[MyExceptionHandler] AjaxException\n  action: "+x.getActionBean().getClass().getName()
        		+"\n  event: "+x.getEventName()
        		+"\n  message: "+x.getMessage());  

        log.error("AjaxException", x);
        
		ObjectNode json = new ObjectNode(JsonNodeFactory.instance);
		json.put("result", "error");
		json.put("message", x.getMessage());
		
        return new ErrorResolution(HttpServletResponse.SC_PRECONDITION_FAILED, json.toString());
	}
	
	/**
	 * return a 404 error
	 */
	public Resolution catchInvalidRequestException(InvalidRequestException x, HttpServletRequest request, HttpServletResponse response) {
        log.error("[MyExceptionHandler] LoginException\n  action: "+x.getActionBean().getClass().getName()
        		+"\n  event: "+x.getEventName()
        		+"\n  message: "+x.getMessage()); 

        log.error("InvalidRequestException", x);
        
        x.getActionBean().getContext().getValidationErrors().add("error", new SimpleError(x.getMessage()));
		return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
		
	}
}
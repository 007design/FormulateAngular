package com.lcp.formulate.stripes.interceptors;

import java.util.ArrayList;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.log4j.Logger;

import com.lcp.formulate.entities.ormlite.Account;
import com.lcp.formulate.stripes.action.operations.AjaxAction;
import com.lcp.formulate.stripes.exceptions.AjaxException;
import com.lcp.formulate.stripes.extensions.MyActionBeanContext;
import com.lcp.formulate.stripes.extensions.typeconverters.AccountTypeConverter;

@Intercepts(value=LifecycleStage.BindingAndValidation)
public class AjaxInterceptor implements Interceptor {
	private static Logger log = org.apache.log4j.Logger.getLogger(AjaxInterceptor.class);

	@Override
	public Resolution intercept(ExecutionContext context) throws Exception {
		if (context.getActionBean() instanceof AjaxAction) {
			log.info("AjaxInterceptor.intercept()");
	
			/**
			 * Get the action bean
			 */
			MyActionBeanContext ctx = (MyActionBeanContext) context.getActionBeanContext();
			log.info("    Event is "+ctx.getEventName());
			log.info("    account param: "+ctx.getRequest().getParameter("account"));
			
			AjaxAction action = (AjaxAction) context.getActionBean();
			AccountTypeConverter atc = new AccountTypeConverter();
										
			/**
			 * Do Type Conversion
			 */
			try {
				action.setAccount( atc.convert(
					ctx.getRequest().getParameter("account"), 
					Account.class, 
					new ArrayList<ValidationError>()) );
			} catch (Exception x) {
				x.printStackTrace();
				throw x;
			}
			
			/**
			 * User must be logged in to the same account
			 */			
			if (ctx.getUser() == null)
				throw new AjaxException(context.getActionBean(), ctx.getEventName(), "Login Required");
			if (!ctx.getUser().getAccount().equals(action.getAccount()))
				throw new AjaxException(context.getActionBean(), ctx.getEventName(), "Invalid Account: "+action.getAccount().getId());
		}
		else {
			log.info("Skipping AjaxInterceptor");
			log.info(context.getActionBean().getClass());
		}
		
		Resolution resolution = context.proceed();

		return resolution;
	}
}

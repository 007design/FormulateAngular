package com.lcp.formulate.stripes.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

import com.lcp.formulate.stripes.extensions.MyActionBeanContext;

/**
 * 
 * <p>ActionBean of which all other ActionBeans are subclasses</p>
 * Avoids having to include the methods required by the ActionBean interface in every Action Bean
 * 
 */

public abstract class BaseActionBean implements ActionBean {
	private MyActionBeanContext actionBeanContext;

	@Override
	public MyActionBeanContext getContext() {
		return this.actionBeanContext;
	}

	@Override
	public void setContext(ActionBeanContext actionBeanContext) {
		this.actionBeanContext = (MyActionBeanContext) actionBeanContext;
	}
}

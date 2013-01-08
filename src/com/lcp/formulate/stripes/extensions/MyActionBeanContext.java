package com.lcp.formulate.stripes.extensions;

import com.lcp.formulate.entities.ormlite.User;

import net.sourceforge.stripes.action.ActionBeanContext;

public class MyActionBeanContext extends ActionBeanContext {
	public static final Integer ACCESS_LEVEL_PASSWORD = 2;
	public static final Integer ACCESS_LEVEL_USER = 1;
	
	public void setUser(User user) { 
		getRequest().getSession().setAttribute("user", user);
	}
	public User getUser() { 
		return (User)getRequest().getSession().getAttribute("user");
	}
	
	
	public void setAccessLevel(Integer a) { getRequest().getSession().setAttribute("accessLevel", a); } 
	public Integer getAccessLevel() {
		Integer level = (Integer)getRequest().getSession().getAttribute("accessLevel");
		if (level==null)
			return 0;
		return level;
	}
}

package com.lcp.formulate.stripes.extensions;

import net.sourceforge.stripes.controller.NameBasedActionResolver;

public class MyActionResolver extends NameBasedActionResolver {
	@Override
	protected String getBindingSuffix() {
		return "";
	}
	
	@Override
	protected String getUrlBinding(String arg0) {
		return super.getUrlBinding(arg0);
	}
}

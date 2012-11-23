package org.celstec.ariadne.app;

import org.purl.sword.base.Deposit;

public class SpiDeposit extends Deposit {

	private String schema;
	private String xIdentifier;
	private String contextPath;


	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getXIdentifier() {
		return xIdentifier;
	}

	public void setXIdentifier(String xIdentifier) {
		this.xIdentifier = xIdentifier;
	} 
}

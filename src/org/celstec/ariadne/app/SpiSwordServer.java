package org.celstec.ariadne.app;

import org.purl.sword.base.Deposit;
import org.purl.sword.base.SWORDAuthenticationException;
import org.purl.sword.base.SWORDErrorException;
import org.purl.sword.base.SWORDException;
import org.purl.sword.server.SWORDServer;

public interface SpiSwordServer  extends SWORDServer{
	
	

	public void doPut(Deposit deposit)
		throws SWORDAuthenticationException, SWORDErrorException, SWORDException;
	
	
	
}

package org.ariadne_eu.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

import be.cenorm.www.CreateAnonymousSession;
import be.cenorm.www.CreateAnonymousSessionResponse;
import be.cenorm.www.CreateSession;
import be.cenorm.www.CreateSessionResponse;
import be.cenorm.www.DestroySession;
import be.cenorm.www.FaultCodeType;
import be.cenorm.www.SessionExpiredException;
import be.cenorm.www.SqiSessionManagementSkeleton;
import be.cenorm.www.Ticket;
import be.cenorm.www._SQIFault;
import be.cenorm.www._SQIFaultException;

/**
 * SqiSessionManagementBindingServiceSkeleton java skeleton for the axisService
 */
public class SqiSessionManagementImplementation extends SqiSessionManagementSkeleton {
	private static Logger log = Logger.getLogger(SqiSessionManagementImplementation.class);

	public CreateSessionResponse createSession(CreateSession createSession)
	throws _SQIFaultException {
		log.info("createSession:username="+createSession.getUserID());
		try {
			String username = createSession.getUserID();
			String password = createSession.getPassword();
			if (username.equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME)) && password.equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD))) {
				Ticket t = Ticket.newTicket("http://www.ariadne-eu.org/metadatastore/");
				t.setParameter("username", username);
				t.setParameter("password", password);
				t.setParameter("queryExpireTime", "1");
				CreateSessionResponse response = new CreateSessionResponse();
				response.setCreateSessionReturn(t.toString());
				log.info("createSession:username="+createSession.getUserID()+",sessionID="+response.getCreateSessionReturn());
				return response;
			} else {
				log.debug("createSession:username="+createSession.getUserID()+": invalid combination of username and password");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00015);
				fault.setMessage("Session creation failure: invalid combination of username and password");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				throw exception;
			}
		} catch (Throwable e) {
			log.error("createSession: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00001);
			fault.setMessage("Session creation failure: "+e.getMessage());
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public void destroySession(DestroySession destroySession)
	throws _SQIFaultException {
		log.info("destroySession:sessionID="+destroySession.getSessionID());
		try {
			String sessionID = destroySession.getSessionID();
			Ticket t = Ticket.getTicket(sessionID);
			//            QueryResults qr = (QueryResults) t.getParameterAsObject("QueryResults");
			//            qr.destroy();
			Ticket.destroy(t);
		} catch (SessionExpiredException e) {
			log.debug("destroySession: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("No such session "+e.getMessage());
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}

	}


	public CreateAnonymousSessionResponse createAnonymousSession(CreateAnonymousSession anonymousSession)
	throws _SQIFaultException {
		String fIP = ((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
		String oIP = remoteAddr(((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)));
		log.info("createAnonymousSession"+":Forwarding IP="+fIP+":Original IP="+oIP );
		try {
			Ticket t = Ticket.newTicket("http://www.ariadne-eu.org/metadatastore/");
			CreateAnonymousSessionResponse response = new CreateAnonymousSessionResponse();
			response.setCreateAnonymousSessionReturn(t.toString());
			log.info("createAnonymousSession:sessionID="+response.getCreateAnonymousSessionReturn());
			return response;
		} catch (Throwable e) {
			log.error("createAnonymousSession: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00001);
			fault.setMessage("Session creation failure: "+e.getMessage());
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}


private String remoteAddr(HttpServletRequest request) {
	String remoteAddr = request.getRemoteAddr();
	String x;
	if ((x = request.getHeader(RepositoryConstants.getInstance().HEADER_X_FORWARDED_FOR)) != null) {
		remoteAddr = x;
		int idx = remoteAddr.indexOf(',');
		if (idx > -1) {
			remoteAddr = remoteAddr.substring(0, idx);
		}
	}
	return remoteAddr;
}


}

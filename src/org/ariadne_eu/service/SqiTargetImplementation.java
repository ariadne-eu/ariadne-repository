package org.ariadne_eu.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.TransportHeaders;
import org.apache.log4j.Logger;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.metadata.resultsformat.TranslateResultsformat;
import org.ariadne_eu.utils.config.RepositoryConstants;

import be.cenorm.www.AsynchronousQuery;
import be.cenorm.www.FaultCodeType;
import be.cenorm.www.GetTotalResultsCount;
import be.cenorm.www.GetTotalResultsCountResponse;
import be.cenorm.www.SessionExpiredException;
import be.cenorm.www.SetMaxDuration;
import be.cenorm.www.SetMaxQueryResults;
import be.cenorm.www.SetQueryLanguage;
import be.cenorm.www.SetResultsFormat;
import be.cenorm.www.SetResultsSetSize;
import be.cenorm.www.SetSourceLocation;
import be.cenorm.www.SqiTargetSkeleton;
import be.cenorm.www.SynchronousQuery;
import be.cenorm.www.SynchronousQueryResponse;
import be.cenorm.www.Ticket;
import be.cenorm.www._SQIFault;
import be.cenorm.www._SQIFaultException;

/**
 * Created by ben
 * Date: 6-jan-2007
 * Time: 23:50:48
 * To change this template use File | Settings | File Templates.
 */
@Path("/sqitarget")
public class SqiTargetImplementation extends SqiTargetSkeleton {
	private static Logger log = Logger.getLogger(SqiTargetImplementation.class);

	public GetTotalResultsCountResponse getTotalResultsCount(GetTotalResultsCount getTotalResultsCount)
	throws _SQIFaultException{
		int queryLanguage = -1;
		String fIP = ((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
		String oIP = remoteAddr(((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)));
		TransportHeaders th = (TransportHeaders)(MessageContext.getCurrentMessageContext().getProperty("TRANSPORT_HEADERS"));
		String userAgent = (String) th.get("user-agent");
		String host = (String) th.get("host");
		log.info("GetTotalResultsCountResponse:query="+getTotalResultsCount.getQueryStatement()+",sessionID="+getTotalResultsCount.getTargetSessionID()+",Forwarding IP="+fIP+",Original IP="+oIP+",User-Agent="+userAgent+",Host="+host);

		Ticket ticket = null;
		try {
			//        	if (getTotalResultsCount.getTargetSessionID().equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_STATICKEY))) {
			//        		ticket = Ticket.newTicket("http://www.ariadne-eu.org/metadatastore/");
			//        		queryLanguage = getQueryLanguage(ticket.toString());
			//        	} else {
			ticket = Ticket.getTicket(getTotalResultsCount.getTargetSessionID());
			queryLanguage = getQueryLanguage(getTotalResultsCount.getTargetSessionID());
			//        	}
		} catch (SessionExpiredException e) {
			log.error("GetTotalResultsCountResponse: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}

		if (queryLanguage != TranslateLanguage.UNDEFINED)
			return getTotalResultsCount(getTotalResultsCount, queryLanguage);

		//        try {
		//            Ticket.getTicket(getTotalResultsCount.getTargetSessionID()); //throws exception if no valid ticket exists
		//        } catch (SessionExpiredException e) {
		//            log.error("GetTotalResultsCountResponse: ", e);
		//            _SQIFault fault = new _SQIFault();
		//            fault.setSqiFaultCode(FaultCodeType.SQI_00013);
		//            fault.setMessage("The given session ID is invalid");
		//            _SQIFaultException exception = new _SQIFaultException();
		//            exception.setFaultMessage(fault);
		//            throw exception;
		//        }

		log.error("GetTotalResultsCountResponse:query="+getTotalResultsCount.getQueryStatement()+",sessionID="+getTotalResultsCount.getTargetSessionID()+",queryLanguage="+getQueryLanguage(getTotalResultsCount.getTargetSessionID()));
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00001);
		fault.setMessage("Query has not been executed");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	private GetTotalResultsCountResponse getTotalResultsCount(GetTotalResultsCount getTotalResultsCount, int queryLanguage) throws _SQIFaultException {
		try {

			int count = QueryMetadataFactory.getQueryImpl(queryLanguage).count(getTotalResultsCount.getQueryStatement());
			GetTotalResultsCountResponse response = new GetTotalResultsCountResponse();
			response.setGetTotalResultsCountReturn(count);
			return response;
		} catch (Exception e) {
			log.error("GetTotalResultsCountResponse:query="+getTotalResultsCount.getQueryStatement()+",sessionID="+getTotalResultsCount.getTargetSessionID()+",queryLanguage="+queryLanguage, e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00001);
			fault.setMessage("Database exception");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	@GET
	@Produces({"application/xml", "application/json"})
	public String synchronousQuery(@QueryParam("query") String query, @QueryParam("start") String start, @QueryParam("size") String size, @QueryParam("lang") String queryLanguage, @QueryParam("format") String resultFormat) {
		Ticket ticket = null;
		ticket = Ticket.newTicket("http://www.ariadne-eu.org/metadatastore/");
		
		log.info("synchronousQuery:query="+query+",sessionID="+ticket.toString());
		

		int queryLang = getQueryLanguage(ticket.toString());
		if(queryLanguage != null) {
			queryLang = TranslateLanguage.getQueryLanguage(queryLanguage);
			if (queryLang == TranslateLanguage.UNDEFINED){
				log.debug("setQueryLanguage:queryLanguageID not supported");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00011);
				fault.setMessage("Query Language Not Supported");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				Ticket.destroy(ticket);
				return fault.getMessage();
			}
		}

		int resultsFormat = getResultsFormat(ticket.toString());
		if(resultFormat != null) {
			resultsFormat = TranslateResultsformat.getResultsFormat(resultFormat);
			if (resultsFormat == TranslateResultsformat.UNDEFINED){
				log.debug("setResultsFormat:Invalid Results format");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00010);
				fault.setMessage("Invalid Results format");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				Ticket.destroy(ticket);
				return fault.getMessage();
			}
		}

		int startResult = 1;
		int nbResults = 12;

		if (start != null) { 
			try {
				startResult = Integer.parseInt(start);
				if(startResult < 1) throw new Exception();
			} catch (Exception e) {
				log.debug("setStartResult:Invalid Start Result");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00003);
				fault.setMessage("Invalid Start Result");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				Ticket.destroy(ticket);
				return fault.getMessage();
			}
		}
		
		if (size != null) { 
			try {
				nbResults = Integer.parseInt(size);
				if(nbResults < 1) throw new Exception();
			} catch (Exception e) {
				log.debug("setNumberOfResults:Invalid Results Set Size");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00005);
				fault.setMessage("Invalid Results Set Size");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				Ticket.destroy(ticket);
				return fault.getMessage();
			}
		}

		SynchronousQuery synchronousQuery = new SynchronousQuery();
		synchronousQuery.setQueryStatement(query);
		synchronousQuery.setTargetSessionID(ticket.toString());
		try {
			SynchronousQueryResponse qReturn = synchronousQuery(synchronousQuery, queryLang, resultsFormat, startResult, nbResults);
			Ticket.destroy(ticket);
			return qReturn.getSynchronousQueryReturn();
		} catch (_SQIFaultException e) {
			log.error("synchronousQuery:sessionID=" + ticket.toString());
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00001);
			fault.setMessage("Query has not been executed");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			Ticket.destroy(ticket);
			return fault.getMessage();
		}
	}

	public  SynchronousQueryResponse synchronousQuery(SynchronousQuery synchronousQuery)
	throws _SQIFaultException{
		int queryLanguage = -1, resultsFormat = -1;
		String fIP = ((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
		String oIP = remoteAddr(((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)));
		TransportHeaders th = (TransportHeaders)(MessageContext.getCurrentMessageContext().getProperty("TRANSPORT_HEADERS"));
		String userAgent = (String) th.get("user-agent");
		String host = (String) th.get("host");

		log.info("synchronousQuery:query="+synchronousQuery.getQueryStatement()+",sessionID="+synchronousQuery.getTargetSessionID()+",Forwarding IP="+fIP+",Original IP="+oIP+",User-Agent="+userAgent+",Host="+host);

		Ticket ticket = null;
		try {
			//        	if (synchronousQuery.getTargetSessionID().equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_STATICKEY))) {
			//        		ticket = Ticket.newTicket("http://www.ariadne-eu.org/metadatastore/");
			//        		queryLanguage = getQueryLanguage(ticket.toString());
			//                resultsFormat = getResultsFormat(ticket.toString());
			//        	} else {
			ticket = Ticket.getTicket(synchronousQuery.getTargetSessionID());
			queryLanguage = getQueryLanguage(synchronousQuery.getTargetSessionID());
			resultsFormat = getResultsFormat(synchronousQuery.getTargetSessionID());
			//        	}
		} catch (SessionExpiredException e) {
			log.error("synchronousQuery:sessionID="+synchronousQuery.getTargetSessionID());
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}


		if (queryLanguage != TranslateLanguage.UNDEFINED) {
			int startResult = synchronousQuery.getStartResult();
			int nbResults = 25;
			if (ticket != null)
				nbResults = Integer.parseInt(ticket.getParameter("resultsSetSize"));
			return synchronousQuery(synchronousQuery, queryLanguage, resultsFormat, startResult, nbResults);
		}

		if (ticket == null) {
			try {
				Ticket.getTicket(synchronousQuery.getTargetSessionID());
				log.debug("synchronousQuery:ticket=null");
			} catch (SessionExpiredException e) {
				log.error("synchronousQuery: ", e);
			}
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}

		log.error("synchronousQuery:sessionID="+synchronousQuery.getTargetSessionID());
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00001);
		fault.setMessage("Query has not been executed");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	public SynchronousQueryResponse synchronousQuery(SynchronousQuery synchronousQuery, int queryLanguage, int resultsFormat, int startResult, int nbResults) throws _SQIFaultException {
		try {
			String result = QueryMetadataFactory.getQueryImpl(queryLanguage).query(synchronousQuery.getQueryStatement(), startResult, nbResults, resultsFormat);
			result = TranslateResultsformat.processResults(result, getResultsFormatString(synchronousQuery.getTargetSessionID()), getQueryLanguageString(synchronousQuery.getTargetSessionID()), synchronousQuery.getQueryStatement());
			SynchronousQueryResponse response = new SynchronousQueryResponse();
			response.setSynchronousQueryReturn(result);
			return response;
		} catch (Exception e) {
			log.error("synchronousQuery:query="+synchronousQuery.getQueryStatement()+",sessionID="+synchronousQuery.getTargetSessionID()+",queryLanguage="+queryLanguage+",startResult="+startResult+",nbResults="+nbResults, e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00001);
			fault.setMessage("Database exception");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public void setMaxDuration(SetMaxDuration setMaxDuration)
	throws _SQIFaultException{
		log.info("setMaxDuration:maxDuration="+setMaxDuration.getMaxDuration()+",sessionID="+setMaxDuration.getTargetSessionID());
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00012);
		fault.setMessage("Method not supported: setMaxDuration");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	public  void setResultsFormat(SetResultsFormat setResultsFormat) throws _SQIFaultException{
		try {
			String rf = setResultsFormat.getResultsFormat();
			log.info("setResultsFormat:resultsFormat="+ rf +",sessionID="+setResultsFormat.getTargetSessionID());
			Ticket ticket = Ticket.getTicket(setResultsFormat.getTargetSessionID()); //throws exception if no valid ticket exists
			if (TranslateResultsformat.getResultsFormat(rf) != TranslateResultsformat.UNDEFINED)
				ticket.setParameter("resultsFormat", rf);
			else {
				log.debug("setResultsFormat:resultsFormat not supported");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00010);
				fault.setMessage("Results format Not Supported");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				throw exception;
			}
		} catch (SessionExpiredException e) {
			log.debug("setResultsFormat: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public  void asynchronousQuery(AsynchronousQuery asynchronousQuery)
	throws _SQIFaultException{
		log.info("asynchronousQuery:query="+asynchronousQuery.getQueryStatement()+",sessionID="+asynchronousQuery.getTargetSessionID());
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00012);
		fault.setMessage("Method not supported: asynchronousQuery");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	public  void setResultsSetSize(SetResultsSetSize setResultsSetSize) throws _SQIFaultException{
		try {
			log.info("setResultsSetSize:resultsSetSize="+setResultsSetSize.getResultsSetSize()+",sessionID="+setResultsSetSize.getTargetSessionID());
			Ticket ticket = Ticket.getTicket(setResultsSetSize.getTargetSessionID()); //throws exception if no valid ticket exists
			if (setResultsSetSize.getResultsSetSize() >= 0)
				ticket.setParameter("resultsSetSize", ""+setResultsSetSize.getResultsSetSize());
			else {
				log.debug("setResultsSetSize:resultsSetSize not supported");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00005);
				fault.setMessage("Invalid results set size");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				throw exception;
			}
		} catch (SessionExpiredException e) {
			log.debug("setResultsSetSize: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public  void setMaxQueryResults(SetMaxQueryResults setMaxQueryResults)
	throws _SQIFaultException{
		log.info("setMaxQueryResults:maxQueryResults="+setMaxQueryResults.getMaxQueryResults()+",sessionID="+setMaxQueryResults.getTargetSessionID());
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00012);
		fault.setMessage("Method not supported: setMaxQueryResults");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	public  void setQueryLanguage(SetQueryLanguage setQueryLanguage) throws _SQIFaultException{
		try {
			log.info("setQueryLanguage:queryLanguageID="+setQueryLanguage.getQueryLanguageID()+",sessionID="+setQueryLanguage.getTargetSessionID());
			Ticket ticket = Ticket.getTicket(setQueryLanguage.getTargetSessionID()); //throws exception if no valid ticket exists
			if (getQueryLanguage(setQueryLanguage.getTargetSessionID()) != TranslateLanguage.UNDEFINED)
				ticket.setParameter("queryLanguage", setQueryLanguage.getQueryLanguageID());
			else {
				log.debug("setQueryLanguage:queryLanguageID not supported");
				_SQIFault fault = new _SQIFault();
				fault.setSqiFaultCode(FaultCodeType.SQI_00011);
				fault.setMessage("Query Language Not Supported");
				_SQIFaultException exception = new _SQIFaultException();
				exception.setFaultMessage(fault);
				throw exception;
			}
		} catch (SessionExpiredException e) {
			log.debug("setQueryLanguage: ", e);
			_SQIFault fault = new _SQIFault();
			fault.setSqiFaultCode(FaultCodeType.SQI_00013);
			fault.setMessage("The given session ID is invalid");
			_SQIFaultException exception = new _SQIFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public  void setSourceLocation(SetSourceLocation setSourceLocation)
	throws _SQIFaultException{
		log.info("setSourceLocation:sourceLocation="+setSourceLocation.getSourceLocation()+",sessionID="+setSourceLocation.getTargetSessionID());
		_SQIFault fault = new _SQIFault();
		fault.setSqiFaultCode(FaultCodeType.SQI_00012);
		fault.setMessage("Method not supported: setSourceLocation");
		_SQIFaultException exception = new _SQIFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	private int getQueryLanguage(String targetSessionID) {
		String queryLanguage = getQueryLanguageString(targetSessionID);
		return TranslateLanguage.getQueryLanguage(queryLanguage);
	}

	private String getQueryLanguageString(String targetSessionID) {
		String queryLanguage;
		try {
			Ticket ticket = Ticket.getTicket(targetSessionID); //throws exception if no valid ticket exists
			queryLanguage = ticket.getParameter("queryLanguage");
		} catch (SessionExpiredException e) {
			queryLanguage = targetSessionID;
		}
		return queryLanguage;
	}

	private String getStartResult(String targetSessionID) {
		String startResult;
		try {
			Ticket ticket = Ticket.getTicket(targetSessionID); //throws exception if no valid ticket exists
			startResult = ticket.getParameter("startResult");
		} catch (SessionExpiredException e) {
			startResult = targetSessionID;
		}
		return startResult;
	}

	private int getResultsFormat(String targetSessionID) {
		String resultsFormat = getResultsFormatString(targetSessionID);
		return TranslateResultsformat.getResultsFormat(resultsFormat);
	}

	private String getResultsFormatString(String targetSessionID) {
		String resultsFormat;
		try {
			Ticket ticket = Ticket.getTicket(targetSessionID); //throws exception if no valid ticket exists
			resultsFormat = ticket.getParameter("resultsFormat");
		} catch (SessionExpiredException e) {
			resultsFormat = targetSessionID;
		}
		return resultsFormat;
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

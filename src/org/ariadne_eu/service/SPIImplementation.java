package org.ariadne_eu.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.TransportHeaders;
import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.content.insert.InsertContentFactory;
import org.ariadne_eu.metadata.delete.DeleteMetadataFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataException;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.spi.CreateIdentifier;
import org.ariadne_eu.spi.CreateIdentifierResponse;
import org.ariadne_eu.spi.DeleteMetadataRecord;
import org.ariadne_eu.spi.DeleteResource;
import org.ariadne_eu.spi.FaultCodeType;
import org.ariadne_eu.spi.SPISkeleton;
import org.ariadne_eu.spi.SpiFault;
import org.ariadne_eu.spi.SpiFaultException;
import org.ariadne_eu.spi.SubmitMetadataRecord;
import org.ariadne_eu.spi.SubmitResource;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.safehaus.uuid.EthernetAddress;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import be.cenorm.www.SessionExpiredException;
import be.cenorm.www.Ticket;

/**
 * Created by ben
 * Date: 6-jan-2007
 * Time: 17:18:57
 * To change this template use File | Settings | File Templates.
 */
public class SPIImplementation extends SPISkeleton {
    private static Logger log = Logger.getLogger(SPIImplementation.class);

    public void deleteResource(DeleteResource deleteResource)
            throws SpiFaultException {
        log.info("deleteResource:identifier="+deleteResource.getGlobalIdentifier()+",sessionID="+deleteResource.getTargetSessionID());
        SpiFault fault = new SpiFault();
//        fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
        fault.setSpiFaultCode(FaultCodeType.SPI_00000);
        fault.setMessage("Method not supported: deleteResource");
        SpiFaultException exception = new SpiFaultException();
        exception.setFaultMessage(fault);
        throw exception;
    }

    public void deleteMetadataRecord(DeleteMetadataRecord deleteMetadataRecord) throws SpiFaultException {
		try {
			log.info("deleteMetadataRecord:identifier=" + deleteMetadataRecord.getGlobalIdentifier() + ",sessionID=" + deleteMetadataRecord.getTargetSessionID());
			Ticket ticket = Ticket.getTicket(deleteMetadataRecord.getTargetSessionID());
			checkValidTicket(ticket);
			DeleteMetadataFactory.deleteMetadata(deleteMetadataRecord.getGlobalIdentifier());
		} catch (SessionExpiredException e) {
			log.error("deleteMetadataRecord:identifier=" + deleteMetadataRecord.getGlobalIdentifier() + ",sessionID=" + deleteMetadataRecord.getTargetSessionID());
			SpiFault fault = new SpiFault();
			fault.setSpiFaultCode(FaultCodeType.SPI_00000);
			fault.setMessage("The given session ID is invalid");
			SpiFaultException exception = new SpiFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

    public CreateIdentifierResponse createIdentifier(CreateIdentifier createIdentifier) throws SpiFaultException {
        try {
            log.info("createIdentifier:sessionID="+createIdentifier.getTargetSessionID());
            Ticket ticket = Ticket.getTicket(createIdentifier.getTargetSessionID()); //throws exception if no valid ticket exists
            checkValidTicket(ticket);
            UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
            EthernetAddress ethernetAddress = uuidGenerator.getDummyAddress();
            UUID uuid = uuidGenerator.generateTimeBasedUUID(ethernetAddress);

            CreateIdentifierResponse response = new CreateIdentifierResponse();
            response.setLocalIdentifier(uuid.toString());
            log.info("createIdentifier:identifier="+response.getLocalIdentifier()+",sessionID="+createIdentifier.getTargetSessionID());
            return response;
        } catch (SessionExpiredException e) {
            log.error("createIdentifier: ", e);
            SpiFault fault = new SpiFault();
//            fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
            fault.setSpiFaultCode(FaultCodeType.SPI_00000);
            fault.setMessage("The given session ID is invalid");
            SpiFaultException exception = new SpiFaultException();
            exception.setFaultMessage(fault);
            throw exception;
        }
    }

    public void submitResource(SubmitResource submitResource) throws SpiFaultException {
        try {
        	String fIP = ((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
        	String oIP = remoteAddr(((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)));
        	TransportHeaders th = (TransportHeaders)(MessageContext.getCurrentMessageContext().getProperty("TRANSPORT_HEADERS"));
        	String userAgent = (String) th.get("user-agent");
        	String host = (String) th.get("host");        	
        	
            log.info("submitResource:identifier="+submitResource.getGlobalIdentifier()+",sessionID="+submitResource.getTargetSessionID()+",Forwarding IP="+fIP+",Original IP="+oIP+",User-Agent="+userAgent+",Host="+host);
            Ticket ticket = Ticket.getTicket(submitResource.getTargetSessionID()); //throws exception if no valid ticket exists
            checkValidTicket(ticket);

            /*boolean success = */InsertContentFactory.insertContent(submitResource.getGlobalIdentifier(), submitResource.getBinaryData().getBase64Binary(), "", "");
//            if (!success) {
//                log.warn("submitResource:identifier="+submitResource.getGlobalIdentifier()+",sessionID="+submitResource.getTargetSessionID()+ " submit failed");
//                SpiFault fault = new SpiFault();
//                fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
//                fault.setMessage("Method not supported: submitResource");
//                SpiFaultException exception = new SpiFaultException();
//                exception.setFaultMessage(fault);
//                throw exception;
//            }
        } catch (SessionExpiredException e) {
            log.error("submitResource: ", e);
            SpiFault fault = new SpiFault();
//            fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
            fault.setSpiFaultCode(FaultCodeType.SPI_00000);
            fault.setMessage("The given session ID is invalid");
            SpiFaultException exception = new SpiFaultException();
            exception.setFaultMessage(fault);
            throw exception;
        }
    }

    public void submitMetadataRecord(SubmitMetadataRecord submitMetadataRecord) throws SpiFaultException {
        try {
        	String fIP = ((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
        	String oIP = remoteAddr(((HttpServletRequest)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)));
        	TransportHeaders th = (TransportHeaders)(MessageContext.getCurrentMessageContext().getProperty("TRANSPORT_HEADERS"));
        	String userAgent = (String) th.get("user-agent");
        	String host = (String) th.get("host");   
        	
            log.info("submitMetadataRecord:identifier="+submitMetadataRecord.getGlobalIdentifier()+",sessionID="+submitMetadataRecord.getTargetSessionID()+",Forwarding IP="+fIP+",Original IP="+oIP+",User-Agent="+userAgent+",Host="+host);
            Ticket ticket = Ticket.getTicket(submitMetadataRecord.getTargetSessionID()); //throws exception if no valid ticket exists
            checkValidTicket(ticket);
            log.debug("submitMetadataRecord:metadata="+submitMetadataRecord.getMetadata());
            InsertMetadataFactory.insertMetadata(submitMetadataRecord.getGlobalIdentifier(), submitMetadataRecord.getMetadata(),"ARIADNE");
        } catch (SessionExpiredException e) {
            log.error("submitMetadataRecord: ", e);
            SpiFault fault = new SpiFault();
            fault.setSpiFaultCode(FaultCodeType.SPI_00000);
            fault.setMessage("The given session ID is invalid");
            SpiFaultException exception = new SpiFaultException();
            exception.setFaultMessage(fault);
            throw exception;
        } catch (InsertMetadataException e) {
        	log.error("submitMetadataRecord: ", e);
            SpiFault fault = new SpiFault();
            fault.setSpiFaultCode(FaultCodeType.SPI_00000);
            fault.setMessage("Insertion has not been executed");
            SpiFaultException exception = new SpiFaultException();
            exception.setFaultMessage(fault);
            throw exception;
        }
    }
    
    public void setDataFormat(java.lang.String targetSessionID, java.lang.String dataFormatID) throws SpiFaultException {
    	
    }

    private void checkValidTicket(Ticket ticket) throws SpiFaultException {
        if (ticket.getParameter("username") == null ||
            !ticket.getParameter("username").equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME)) ||
            ticket.getParameter("password") == null ||
            !ticket.getParameter("password").equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD))) {
            SpiFault fault = new SpiFault();
//            fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
            fault.setSpiFaultCode(FaultCodeType.SPI_00000);
            fault.setMessage("The given session ID is invalid");
            SpiFaultException exception = new SpiFaultException();
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

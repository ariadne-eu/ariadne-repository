package org.ariadne_eu.service;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.content.insert.InsertContentFactory;
import org.ariadne_eu.metadata.delete.DeleteMetadataFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataException;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.spi.SpiFault;
import org.ariadne_eu.spi.SpiFaultException;
import org.ariadne_eu.spidev.CreateIdentifier;
import org.ariadne_eu.spidev.CreateIdentifierResponse;
import org.ariadne_eu.spidev.DeleteMetadataRecord;
import org.ariadne_eu.spidev.DeleteResource;
import org.ariadne_eu.spidev.FaultCodeType;
import org.ariadne_eu.spidev.SPIDevSkeleton;
import org.ariadne_eu.spidev.SpiDevFault;
import org.ariadne_eu.spidev.SpiDevFaultException;
import org.ariadne_eu.spidev.SubmitMetadataRecord;
import org.ariadne_eu.spidev.SubmitResource;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.safehaus.uuid.EthernetAddress;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import be.cenorm.www.SessionExpiredException;
import be.cenorm.www.Ticket;

/**
 * Created by ben Date: 6-jan-2007 Time: 17:18:57 To change this template use
 * File | Settings | File Templates.
 */
public class SPIDevImplementation extends SPIDevSkeleton {
	private static Logger log = Logger.getLogger(SPIDevImplementation.class);

	public void deleteResource(DeleteResource deleteResource) throws SpiDevFaultException {
		log.info("deleteResource:identifier=" + deleteResource.getGlobalIdentifier() + ",sessionID=" + deleteResource.getTargetSessionID());
		SpiDevFault fault = new SpiDevFault();
		fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
		fault.setMessage("Method not supported: deleteResource");
		SpiDevFaultException exception = new SpiDevFaultException();
		exception.setFaultMessage(fault);
		throw exception;
	}

	public void deleteMetadataRecord(DeleteMetadataRecord deleteMetadataRecord) throws SpiDevFaultException {
		try {
			log.info("deleteMetadataRecord:identifier=" + deleteMetadataRecord.getGlobalIdentifier() + ",sessionID=" + deleteMetadataRecord.getTargetSessionID());
			Ticket ticket = Ticket.getTicket(deleteMetadataRecord.getTargetSessionID());
			checkValidTicket(ticket);
			DeleteMetadataFactory.deleteMetadata(deleteMetadataRecord.getGlobalIdentifier());
		} catch (SessionExpiredException e) {
			log.error("deleteMetadataRecord:identifier=" + deleteMetadataRecord.getGlobalIdentifier() + ",sessionID=" + deleteMetadataRecord.getTargetSessionID());
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("The given session ID is invalid");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public CreateIdentifierResponse createIdentifier(CreateIdentifier createIdentifier) throws SpiDevFaultException {
		try {
			log.info("createIdentifier:sessionID=" + createIdentifier.getTargetSessionID());
			// throws exception if no valid ticket exists
			Ticket ticket = Ticket.getTicket(createIdentifier.getTargetSessionID()); 
			checkValidTicket(ticket);
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			EthernetAddress ethernetAddress = uuidGenerator.getDummyAddress();
			UUID uuid = uuidGenerator.generateTimeBasedUUID(ethernetAddress);

			CreateIdentifierResponse response = new CreateIdentifierResponse();
			response.setLocalIdentifier(uuid.toString());
			log.info("createIdentifier:identifier=" + response.getLocalIdentifier() + ",sessionID=" + createIdentifier.getTargetSessionID());
			return response;
		} catch (SessionExpiredException e) {
			log.debug("createIdentifier: ", e);
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("The given session ID is invalid");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public void submitResource(SubmitResource submitResource) throws SpiDevFaultException {
		try {
			log.info("submitResource:identifier=" + submitResource.getGlobalIdentifier() + ",sessionID=" + submitResource.getTargetSessionID());
			//throws exception if no valid ticket exists
			Ticket ticket = Ticket.getTicket(submitResource.getTargetSessionID()); 
			checkValidTicket(ticket);

			/* boolean success = */InsertContentFactory.insertContent(submitResource.getGlobalIdentifier(), submitResource.getBinaryData().getBase64Binary(), submitResource.getFileName(),
					submitResource.getFileType());
			// if (!success) {
			// log.warn("submitResource:identifier="+submitResource.getGlobalIdentifier()+",sessionID="+submitResource.getTargetSessionID()+
			// " submit failed");
			// SpiFault fault = new SpiFault();
			// fault.setSpiFaultCode(SpiFaultCodeType.SPI_00000);
			// fault.setMessage("Method not supported: submitResource");
			// SpiFaultException exception = new SpiFaultException();
			// exception.setFaultMessage(fault);
			// throw exception;
			// }
		} catch (SessionExpiredException e) {
			log.debug("submitResource: ", e);
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("The given session ID is invalid");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public void submitMetadataRecord(SubmitMetadataRecord submitMetadataRecord) throws SpiDevFaultException {
		try {
			log.info("submitMetadataRecord:identifier=" + submitMetadataRecord.getGlobalIdentifier() + ",sessionID=" + submitMetadataRecord.getTargetSessionID());
			// throws exception if no valid ticket exists
			Ticket ticket = Ticket.getTicket(submitMetadataRecord.getTargetSessionID()); 
			checkValidTicket(ticket);
			InsertMetadataFactory.insertMetadata(submitMetadataRecord.getGlobalIdentifier(), submitMetadataRecord.getMetadata(), submitMetadataRecord.getCollection());
			// } catch (XMLDBException e) {
			// log.error("submitMetadataRecord: ", e);
		} catch (SessionExpiredException e) {
			log.debug("submitMetadataRecord: ", e);
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("The given session ID is invalid");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		} catch (InsertMetadataException e) {
			log.error("submitMetadataRecord: ", e);
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("Insertion has not been executed");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

	public void setDataFormat(java.lang.String targetSessionID, java.lang.String dataFormatID) throws SpiDevFaultException {

	}

	private void checkValidTicket(Ticket ticket) throws SpiDevFaultException {
		if (ticket.getParameter("username") == null || !ticket.getParameter("username").equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME))
				|| ticket.getParameter("password") == null || !ticket.getParameter("password").equalsIgnoreCase(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD))) {
			SpiDevFault fault = new SpiDevFault();
			fault.setSpiDevFaultCode(FaultCodeType.SPIDev_00000);
			fault.setMessage("The given session ID is invalid");
			SpiDevFaultException exception = new SpiDevFaultException();
			exception.setFaultMessage(fault);
			throw exception;
		}
	}

}

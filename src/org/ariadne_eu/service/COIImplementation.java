package org.ariadne_eu.service;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne_eu.coi.COIFaultException;
import org.ariadne_eu.coi.COISkeleton;
import org.ariadne_eu.coi.GetResource;
import org.ariadne_eu.coi.GetResourceExtension;
import org.ariadne_eu.coi.GetResourceExtensionResponse;
import org.ariadne_eu.coi.GetResourceNameResponse;
import org.ariadne_eu.coi.GetResourceResponse;
import org.ariadne_eu.content.retrieve.RetrieveContentFactory;
import org.w3.www._2005._05.xmlmime.Base64Binary;

/**
 * Created by ben
 * Date: 3-mrt-2007
 * Time: 19:05:27
 * To change this template use File | Settings | File Templates.
 */

public class COIImplementation extends COISkeleton {
    private static Logger log = Logger.getLogger(COIImplementation.class);
    

    public GetResourceResponse getResource(GetResource getResource) throws COIFaultException {
        log.info("getResource:identifier="+getResource.getIdentifier()+",sessionID="+getResource.getTargetSessionID());
        DataHandler dataHandler = RetrieveContentFactory.retrieveContent(getResource.getIdentifier());
        GetResourceResponse getResourceResponse = new GetResourceResponse();
        Base64Binary base64Binary = new Base64Binary();
        base64Binary.setBase64Binary(dataHandler);
        getResourceResponse.setBinaryData(base64Binary);
        return getResourceResponse;
    }
    
    
    public GetResourceNameResponse getResourceName(org.ariadne_eu.coi.GetResourceName getResourceName) throws COIFaultException {
        log.info("getResourceName:identifier="+getResourceName.getIdentifier()+",sessionID="+getResourceName.getTargetSessionID());
        String fileName = RetrieveContentFactory.retrieveFileName(getResourceName.getIdentifier());
        GetResourceNameResponse getResourceNameResponse = new GetResourceNameResponse();
        getResourceNameResponse.setName(fileName);
        return getResourceNameResponse;
    }
    
    public GetResourceExtensionResponse getResourceExtension(GetResourceExtension getResourceExtension) throws COIFaultException {
        log.info("getResourceExtension:identifier="+getResourceExtension.getIdentifier()+",sessionID="+getResourceExtension.getTargetSessionID());
        String fileExtension = RetrieveContentFactory.retrieveFileType(getResourceExtension.getIdentifier());
        GetResourceExtensionResponse getResourceExtensionResponse = new GetResourceExtensionResponse();
        getResourceExtensionResponse.setExtension(fileExtension);
        return getResourceExtensionResponse;
    }
}

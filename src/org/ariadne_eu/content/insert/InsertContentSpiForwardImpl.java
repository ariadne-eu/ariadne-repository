package org.ariadne_eu.content.insert;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.spidev.SPIDevStub;
import org.ariadne_eu.spidev.SubmitResource;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.w3.www._2005._05.xmlmime.Base64Binary;

import be.cenorm.www.CreateSession;
import be.cenorm.www.CreateSessionResponse;
import be.cenorm.www.SqiSessionManagementStub;

/**
 * Created by ben
 * Date: 13-sep-2007
 * Time: 22:07:40
 * To change this template use File | Settings | File Templates.
 */
public class InsertContentSpiForwardImpl extends InsertContentImpl {
    private static Logger log = Logger.getLogger(InsertContentSpiForwardImpl.class);

    private String smURI;
    private String spiURI;
    private String username;
    private String password;
//    private String catalog;

    void initialize() {
        super.initialize();

        smURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_URL + "." + getNumber());
        if (smURI == null)
            smURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_URL);
        spiURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SPI_URL + "." + getNumber());
        if (spiURI == null)
            spiURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SPI_URL);
        username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_USERNAME + "." + getNumber());
        if (username == null)
            username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_USERNAME);
        password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_PASSWORD + "." + getNumber());
        if (password == null)
            password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_SPIFWD_SM_PASSWORD);
//        catalog = PropertiesManager.getInstance().getProperty("spiforward.content.spi.catalog."+getNumber());
//        if (catalog == null)
//            catalog = PropertiesManager.getInstance().getProperty("spiforward.content.spi.catalog");
    }


//    public void insertContent(String identifier, DataHandler dataHandler) {
//        try {
//            SqiSessionManagementBindingServiceStub sm = new SqiSessionManagementBindingServiceStub(smURI);
//            CreateSession createSession = new CreateSession();
//            createSession.setUserID(username);
//            createSession.setPassword(password);
//            CreateSessionResponse sessionM = sm.createSession(createSession);
//
//            SPIStub spi = new SPIStub(spiURI);
////            CreateIdentifier createIdentifier = new CreateIdentifier();
////            createIdentifier.setCatalog(catalog);
////            createIdentifier.setTargetSessionID(sessionM.getCreateSessionReturn());
////            CreateIdentifierResponse spiIdentifier = spi.createIdentifier(createIdentifier);
//
//            SubmitResource resource = new SubmitResource();
//            resource.setGlobalIdentifier(identifier);
//            resource.setTargetSessionID(sessionM.getCreateSessionReturn());
//            Base64Binary binary = new Base64Binary();
//            binary.setBase64Binary(dataHandler);
//            resource.setBinaryData(binary);
//            spi.submitResource(resource);
//        } catch (Exception e) {
//            log.error("insertContent failed, identifier: \""+identifier+"\"", e);
//        }
//    }
    
    public synchronized void insertContent(String identifier, DataHandler dataHandler, String fileName, String fileType) {
        try {
            SqiSessionManagementStub sm = new SqiSessionManagementStub(smURI);
            CreateSession createSession = new CreateSession();
            createSession.setUserID(username);
            createSession.setPassword(password);
            CreateSessionResponse sessionM = sm.createSession(createSession);

            SPIDevStub spi = new SPIDevStub(spiURI);

            SubmitResource resource = new SubmitResource();
            resource.setGlobalIdentifier(identifier);
            resource.setTargetSessionID(sessionM.getCreateSessionReturn());
            Base64Binary binary = new Base64Binary();
            binary.setBase64Binary(dataHandler);
            resource.setBinaryData(binary);
            resource.setFileName(fileName);
            resource.setFileType(fileType);
            spi.submitResource(resource);
        } catch (Exception e) {
            log.error("insertContent failed, identifier: \""+identifier+"\"", e);
        }
    }
}

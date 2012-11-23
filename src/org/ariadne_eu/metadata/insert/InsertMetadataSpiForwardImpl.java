package org.ariadne_eu.metadata.insert;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.spi.SPIStub;
import org.ariadne_eu.spi.SubmitMetadataRecord;
import org.ariadne_eu.utils.config.RepositoryConstants;

import be.cenorm.www.CreateSession;
import be.cenorm.www.CreateSessionResponse;
import be.cenorm.www.DestroySession;
import be.cenorm.www.SqiSessionManagementStub;

/**
 * Created by ben
 * Date: 13-sep-2007
 * Time: 21:41:54
 * To change this template use File | Settings | File Templates.
 */
public class InsertMetadataSpiForwardImpl extends InsertMetadataImpl {
    private static Logger log = Logger.getLogger(InsertMetadataExistDbImpl.class);

    private String smURI;
    private String spiURI;
    private String username;
    private String password;

    void initialize() {
        super.initialize();

        smURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_URL + "." + getLanguage());
        if (smURI == null)
            smURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_URL);
        spiURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SPI_URL + "." + getLanguage());
        if (spiURI == null)
            spiURI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SPI_URL);
        username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_USERNAME + "." + getLanguage());
        if (username == null)
            username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_USERNAME);
        password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_PASSWORD + "." + getLanguage());
        if (password == null)
            password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFWD_SM_PASSWORD);
    }

    /*
     * NOTE: Collection is not implemented!
     * 
     * */
    public synchronized void insertMetadata(String identifier, String metadata, String collection) throws InsertMetadataException{
        try {
        	SqiSessionManagementStub sm = new SqiSessionManagementStub(smURI);
            CreateSession createSession = new CreateSession();
            createSession.setUserID(username);
            createSession.setPassword(password);
            CreateSessionResponse sessionM = sm.createSession(createSession);

            SPIStub spi = new SPIStub(spiURI);

            SubmitMetadataRecord submitMetadataRecord = new SubmitMetadataRecord();
            submitMetadataRecord.setGlobalIdentifier(identifier);
            submitMetadataRecord.setTargetSessionID(sessionM.getCreateSessionReturn());
            submitMetadataRecord.setMetadata(metadata);
            spi.submitMetadataRecord(submitMetadataRecord);
            
            if(!sessionM.getCreateSessionReturn().equals("")) {
            	DestroySession destroySession = new DestroySession();
    	        destroySession.setSessionID(sessionM.getCreateSessionReturn());
    	        sm.destroySession(destroySession);
            }
            
        } catch (Exception e) {
            log.error("insertMetadata failed, identifier: \""+identifier+"\", metadata: \""+metadata+"\"", e);
            throw new InsertMetadataException(e);
        } 
    }
}

package org.ariadne_eu.metadata.delete;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:05:44
 * To change this template use File | Settings | File Templates.
 */
public class DeleteMetadataExistDbImpl extends DeleteMetadataImpl {
    private static Logger log = Logger.getLogger(DeleteMetadataExistDbImpl.class);

    private Collection collection;

    public DeleteMetadataExistDbImpl() {
        initialize();
    }

    public DeleteMetadataExistDbImpl(int implementation) {
        setImplementation(implementation);
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
            String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI + "." + getImplementation());
            if (URI == null)
                URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI);
            try {
            	Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
                Database database = (Database)cl.newInstance();
                DatabaseManager.registerDatabase(database);

                String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME + "." + getImplementation());
                if (username == null)
                    username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME);
                String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD + "." + getImplementation());
                if (password == null)
                    password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD);

                collection = DatabaseManager.getCollection(URI, username, password);
            } catch (ClassNotFoundException e) {
                log.error("initialize: ", e);
            } catch (InstantiationException e) {
                log.error("initialize: ", e);
            } catch (IllegalAccessException e) {
                log.error("initialize: ", e);
            } catch (XMLDBException e) {
            }
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    public synchronized void deleteMetadata(String identifier) {
        try {
        	identifier = identifier.replaceAll(":", "_");
            XMLResource document = (XMLResource)collection.createResource(identifier, "XMLResource");
//            document.setContent(metadata);
//            collection.storeResource(document);
            log.info("deleteMetadata:identifier:\""+identifier+"\"");
        } catch (XMLDBException e) {
            log.error("deleteMetadata:identifier:\""+identifier+"\" ", e);
        }
    }
}

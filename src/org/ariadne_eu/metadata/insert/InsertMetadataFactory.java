package org.ariadne_eu.metadata.insert;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:02:22
 * To change this template use File | Settings | File Templates.
 */
public class InsertMetadataFactory {

    private static Logger log = Logger.getLogger(InsertMetadataFactory.class);

    private static Vector cachedImplementations = new Vector();

    static {
        initialize();
    }

    public static void initialize() {
        cachedImplementations = new Vector();

        for (int language = -1; language < 10; language++) {
            String implementation;
            if (language >= 0) {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_IMPLEMENTATION + "." + language);
            } else {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_IMPLEMENTATION);
            }
            if (implementation != null) {
                try {
                    Class implClass = Class.forName(implementation);
                    InsertMetadataImpl insertMetadata = (InsertMetadataImpl) implClass.newInstance();
                    insertMetadata.setLanguage(language);
                    insertMetadata.initialize();
                    cachedImplementations.add(insertMetadata);
                } catch (Exception e) {
                    log.error("Error while initializing insertMetadata class", e);
                }
            }
        }
    }

    public static InsertMetadataImpl[] getInsertImpl() {
        return (InsertMetadataImpl[]) cachedImplementations.toArray(new InsertMetadataImpl[cachedImplementations.size()]);
    }

    public static void insertMetadata(String identifier, String metadata, String collection) throws InsertMetadataException{
        InsertMetadataImpl[] impls = getInsertImpl();
        for (int i = 0; i < impls.length; i++) {
            InsertMetadataImpl impl = impls[i];
            try {
				impl.insertMetadata(identifier, metadata, collection);
			} catch (InsertMetadataException e) {
				throw e;
			}
        }
    }
}

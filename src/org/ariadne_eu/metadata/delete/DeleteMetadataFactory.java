package org.ariadne_eu.metadata.delete;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;


public class DeleteMetadataFactory {

    private static Logger log = Logger.getLogger(DeleteMetadataFactory.class);

    private static Vector cachedImplementations = new Vector();

    static {
        initialize();
    }

    public static void initialize() {
        cachedImplementations = new Vector();

        for (int implementation = -1; implementation < 10; implementation++) {
            String className;
            if (implementation >= 0) {
            	className = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DELETE_IMPLEMENTATION + "." + implementation);
            } else {
            	className = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DELETE_IMPLEMENTATION);
            }
            if (className != null) {
                try {
                    Class implClass = Class.forName(className);
                    DeleteMetadataImpl insertMetadata = (DeleteMetadataImpl) implClass.newInstance();
                    insertMetadata.setImplementation(implementation);
                    insertMetadata.initialize();
                    cachedImplementations.add(insertMetadata);
                } catch (Exception e) {
                    log.error("Error while initializing deleteMetadata class", e);
                }
            }
        }
    }

    public static DeleteMetadataImpl[] getInsertImpl() {
        return (DeleteMetadataImpl[]) cachedImplementations.toArray(new DeleteMetadataImpl[cachedImplementations.size()]);
    }

    public static void deleteMetadata(String identifier) {
        DeleteMetadataImpl[] impls = getInsertImpl();
        for (int i = 0; i < impls.length; i++) {
            DeleteMetadataImpl impl = impls[i];
            impl.deleteMetadata(identifier);
        }
    }
}

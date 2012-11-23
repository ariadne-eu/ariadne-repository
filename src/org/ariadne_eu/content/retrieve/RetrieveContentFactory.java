package org.ariadne_eu.content.retrieve;

import java.util.Vector;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 11-sep-2007
 * Time: 21:20:17
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveContentFactory {

    private static Logger log = Logger.getLogger(RetrieveContentFactory.class);

    private static Vector cachedImplementations = new Vector();

    static {
        initialize();
    }

    public static void initialize() {
        cachedImplementations = new Vector();

        for (int nb = -1; nb < 0; nb++) {
            String implementation;
            if (nb >= 0) {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_RETREIVE_IMPLEMENTATION + "." + nb);
            } else {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_RETREIVE_IMPLEMENTATION);
            }
            if (implementation != null) {
                try {
                    Class implClass = Class.forName(implementation);
                    RetrieveContentImpl retrieveContent = (RetrieveContentImpl) implClass.newInstance();
//                    insertContent.setNumber(nb);
                    retrieveContent.initialize();
                    cachedImplementations.add(retrieveContent);
                } catch (Exception e) {
                    log.error("Error while initializing retrieveContent class", e);
                }
            }
        }
    }

    public static RetrieveContentImpl[] getRetrieveImpl() {
        return (RetrieveContentImpl[]) cachedImplementations.toArray(new RetrieveContentImpl[cachedImplementations.size()]);
    }

    public static DataHandler retrieveContent(String identifier) {
        RetrieveContentImpl[] impls = getRetrieveImpl();
        for (int i = 0; i < impls.length; i++) {
            RetrieveContentImpl impl = impls[i];
            return impl.retrieveContent(identifier);
        }
        return null;
    }
    
    public static String retrieveFileName(String identifier) {
        RetrieveContentImpl[] impls = getRetrieveImpl();
        for (int i = 0; i < impls.length; i++) {
            RetrieveContentImpl impl = impls[i];
            return impl.retrieveFileName(identifier);
        }
        return null;
    }
    
    public static String retrieveFileType(String identifier) {
        RetrieveContentImpl[] impls = getRetrieveImpl();
        for (int i = 0; i < impls.length; i++) {
            RetrieveContentImpl impl = impls[i];
            return impl.retrieveFileType(identifier);
        }
        return null;
    }
}

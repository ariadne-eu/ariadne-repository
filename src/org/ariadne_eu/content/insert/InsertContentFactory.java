package org.ariadne_eu.content.insert;

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
public class InsertContentFactory {

    private static Logger log = Logger.getLogger(InsertContentFactory.class);

    private static Vector cachedImplementations = new Vector();

    static {
        initialize();
    }

    public static void initialize() {
        cachedImplementations = new Vector();

        for (int nb = -1; nb < 10; nb++) {
            String implementation;
            if (nb >= 0) {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_INSERT_IMPLEMENTATION + "." + nb);
            } else {
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_INSERT_IMPLEMENTATION);
            }
            if (implementation != null) {
                try {
                    Class implClass = Class.forName(implementation);
                    InsertContentImpl insertContent = (InsertContentImpl) implClass.newInstance();
                    insertContent.setNumber(nb);
                    insertContent.initialize();
                    cachedImplementations.add(insertContent);
                } catch (Exception e) {
                    log.error("Error while initializing insertContent class", e);
                }
            }
        }
    }

    public static InsertContentImpl[] getInsertImpl() {
        return (InsertContentImpl[]) cachedImplementations.toArray(new InsertContentImpl[cachedImplementations.size()]);
    }

    public static void insertContent(String identifier, DataHandler dataHandler, String fileName, String fileType) {
        InsertContentImpl[] impls = getInsertImpl();
        for (int i = 0; i < impls.length; i++) {
            InsertContentImpl impl = impls[i];
            impl.insertContent(identifier, dataHandler, fileName, fileType);
        }
    }
}

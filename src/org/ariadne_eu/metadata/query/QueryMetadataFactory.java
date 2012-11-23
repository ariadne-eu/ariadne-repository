package org.ariadne_eu.metadata.query;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 16:55:38
 * To change this template use File | Settings | File Templates.
 */
public class QueryMetadataFactory {

    private static Logger log = Logger.getLogger(QueryMetadataFactory.class);

    private static HashMap cachedImplementations = new HashMap();

    public static void initialize() {
        cachedImplementations = new HashMap();
    }

    public static QueryMetadataImpl getQueryImpl(int language) {
        if (cachedImplementations.get(new Integer(language)) == null) {
            String implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_QUERY_IMPLEMENTATION + "." + language);
            if (implementation == null)
                implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_QUERY_IMPLEMENTATION);
            try {
                Class implClass = Class.forName(implementation);
                QueryMetadataImpl query = (QueryMetadataImpl) implClass.newInstance();
                query.setLanguage(language);
                query.initialize();
                cachedImplementations.put(new Integer(language), query);
            } catch (Exception e) {
                log.error("Error while initializing query class", e);
            }
        }
        return (QueryMetadataImpl) cachedImplementations.get(new Integer(language));
    }

}

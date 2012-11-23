package org.ariadne_eu.metadata.query;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 16:37:14
 * To change this template use File | Settings | File Templates.
 */
public class QueryMetadataExistDbImpl extends QueryMetadataImpl {

    private static Logger log = Logger.getLogger(QueryMetadataExistDbImpl.class);

    public QueryMetadataExistDbImpl() {
    }

    QueryMetadataExistDbImpl(int language) {
        setLanguage(language);
        initialize();
    }

    private Collection collection;

    void initialize() {
        super.initialize();
        try {
            String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI + "." + getLanguage());
            if (URI == null)
                URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI);

            try {
//                String driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER + "." + getLanguage());
//                if (driver == null)
//                    driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER);
//                Class cl = Class.forName(driver);
            	Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
                Database database = (Database)cl.newInstance();
                DatabaseManager.registerDatabase(database);

                String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME + "." + getLanguage());
                if (username == null)
                    username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME);
                String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD + "."+getLanguage());
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
                e.printStackTrace();
            }
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    public String xQuery(String xQuery) throws QueryMetadataException {
        try {
            XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");
            service.setProperty("indent", "yes");
            ResourceSet result = service.query(xQuery);

            ResourceIterator i = result.getIterator();
            if (i.hasMoreResources()) {
                Resource r = i.nextResource();
                return(String)r.getContent();
            }
        } catch (XMLDBException e) {
            log.error("xQuery:xQuery="+xQuery, e);
            throw new QueryMetadataException(e);
        }
        log.error("xQuery:method didn't return answer, xQuery="+xQuery);
        return null;
    }
    
    public String query(String query, int start, int max, int resultsFormat) throws QueryTranslationException, QueryMetadataException {
      String xQuery = TranslateLanguage.translateToQuery(query, getLanguage(), TranslateLanguage.XQUERY, start, max, resultsFormat);
      return xQuery(xQuery);
  }

  public int count(String query) throws QueryTranslationException, QueryMetadataException {
      String xQuery = TranslateLanguage.translateToCount(query, getLanguage(), TranslateLanguage.XQUERY);
      return Integer.parseInt(xQuery(xQuery));
  }
}

package org.ariadne_eu.metadata.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.utils.config.RepositoryConstants;

import com.ibm.db2.jcc.DB2Xml;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:30:21
 * To change this template use File | Settings | File Templates.
 */
public class QueryMetadataIBMDB2DbImpl extends QueryMetadataImpl {

    private static Logger log = Logger.getLogger(QueryMetadataIBMDB2DbImpl.class);

    public QueryMetadataIBMDB2DbImpl() {
    }

    QueryMetadataIBMDB2DbImpl(int language) {
        setLanguage(language);
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
            try {
//                String driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER + "." + getLanguage());
//                if (driver == null)
//                    driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER);
//                Class.forName(driver);
            	Class.forName("com.ibm.db2.jcc.DB2Driver");
            } catch (ClassNotFoundException e) {
                log.error("initialize: ", e);
            }
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    private Connection getConnection() throws SQLException {
        String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI + "." + getLanguage());
        if (URI == null)
            URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI);
        String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME + "." + getLanguage());
        if (username == null)
            username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME);
        String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD + "." + getLanguage());
        if (password == null)
            password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD);
        return DriverManager.getConnection(URI,username, password);
    }
    

    public String xQuery(String xQuery) throws QueryMetadataException {
        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = (Statement) con.createStatement();
            
            ResultSet rs = stmt.executeQuery(xQuery.replaceAll("version \"1.0\";", "")); //TODO: why doesn't version work for IBM DB2
            String result = "";
            while (rs.next()) {
                DB2Xml xml = (DB2Xml) rs.getObject(1);
                result = result + xml.getDB2String();
                //return xml.getDB2String();
            }
            if (result.equals("")){
            	log.error("xQuery:method didn't return answer, xQuery=" + xQuery);
                return null;
            }
            return result;
        } catch (SQLException e) {
            log.error("xQuery:xQuery=" + xQuery, e);
            throw new QueryMetadataException(e);
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                log.error("xQuery:xQuery=" + xQuery, e);
            }
        }
        
        //log.error("xQuery:method didn't return answer, xQuery=" + xQuery);
        //return null;
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

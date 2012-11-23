package org.ariadne_eu.metadata.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleResultSet;
import oracle.xdb.XMLType;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:30:21
 * To change this template use File | Settings | File Templates.
 */
public class QueryMetadataOracleDbImpl extends QueryMetadataImpl {

    private static Logger log = Logger.getLogger(QueryMetadataOracleDbImpl.class);

    public QueryMetadataOracleDbImpl() {
    }

    QueryMetadataOracleDbImpl(int language) {
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
            	Class.forName("oracle.jdbc.driver.OracleDriver");
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
    
    public int xQueryCount(String xQuery) throws QueryMetadataException {
        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = (Statement) con.createStatement();
            
            String column_xml = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_COLUMNNAME);
            String table = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_TABLENAME);
            
            xQuery = xQuery.replaceAll("\"\"", "\"");
            xQuery = xQuery.replaceAll("count\\(for","\\(for");
            
    		String query = "SELECT COUNT(*) " +
    		"FROM " + table + " " +
    		", XMLTable(' " + xQuery + "' passing "+ column_xml +")";
    		
    		log.debug(query);
            ResultSet rs = stmt.executeQuery(query); //TODO: why doesn't version work for IBM DB2
            String result = "";
            if (rs.next()) {
				return ((OracleResultSet)rs).getInt(1);
            }
            if (result.equals("")){
            	log.error("xQuery:method didn't return answer, xQuery=" + xQuery);
               return -2;
            }
            return -2;
          //GAP end
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
    

    public String xQuery(String xQuery, int start, int max) throws QueryMetadataException {
        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = (Statement) con.createStatement();
            
            String column_xml = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_COLUMNNAME);
            String table = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_TABLENAME);
            
            xQuery = xQuery.replaceAll("\"\"", "\"");
            xQuery = xQuery.replaceAll("\\<results\\>\\{for \\$x at \\$y in \\(","");
            xQuery = xQuery.replaceAll("/lom:lom","/lom:lom//\\*");
            
            int temp = start+max;
            xQuery = xQuery.replaceAll("\\) where \\$y \\>= "+start+" and \\$y \\< "+temp+" return \\$x \\}\\</results\\>", "");
            
    		String query = "FROM ( " +
    		"SELECT "+column_xml+" " +
    		"FROM " + table + " " +
    		", XMLTable(' " + xQuery + "' passing "+ column_xml +"))";
            
			String select =  "SELECT "+column_xml+" from ( select /*+ FIRST_ROWS(n) */ a.*, ROWNUM rnum ";
			String limitQuery = " a where ROWNUM <= "+ (start+max) +") where rnum  > " + start ;
    		
			log.debug(select+query+limitQuery);
            ResultSet rs = stmt.executeQuery(select+query+limitQuery); //TODO: why doesn't version work for IBM DB2
    	    StringBuilder result = new StringBuilder();
    	    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    				 		"<results>\n");

            while (rs.next()) {
				XMLType poxml = XMLType.createXML(((OracleResultSet)rs).getOPAQUE(1));
                String lomxml = poxml.getStringVal().replaceFirst("\\<\\?xml version=\"1.0\" encoding=\".*\"\\?>\\n","");
				result.append(lomxml);
            }
            if (result.equals("")){
            	log.error("xQuery:method didn't return answer, xQuery=" + xQuery);
                return null;
            }
        		result.append("</results>");
        	    return result.toString();
          //GAP end
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
        return xQuery(xQuery, start, max);
    }

    public int count(String query) throws QueryTranslationException, QueryMetadataException {
        String xQuery = TranslateLanguage.translateToCount(query, getLanguage(), TranslateLanguage.XQUERY);
        return xQueryCount(xQuery);
    }

	@Override
	public String xQuery(String query) throws QueryTranslationException,
			QueryMetadataException {
        return xQuery(query, 0, 101);
	}

}

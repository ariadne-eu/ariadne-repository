package org.ariadne_eu.metadata.insert;

import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 19:05:44
 * To change this template use File | Settings | File Templates.
 */
public class InsertMetadataOracleDbImpl extends InsertMetadataImpl {
    private static Logger log = Logger.getLogger(InsertMetadataOracleDbImpl.class);

    private String tableName;
    private String columnName;
    private String identifierColumnName;

    public InsertMetadataOracleDbImpl() {
    }

    public InsertMetadataOracleDbImpl(int language) {
        setLanguage(language);
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
//            String driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER + "." + getLanguage());
//            if (driver == null)
//                driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_DRIVER);
//            Class.forName(driver);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //TODO: auto generate?
//                if(collection == null)
//                    generateCollection(URI, collectionString, username, password);
            tableName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_TABLENAME);
            if (tableName == null)
                tableName = "Metadatastore";
            columnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_COLUMNNAME);
            if (columnName == null)
                columnName = "lomxml";
            identifierColumnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_IDCOLUMNNAME);
            if (identifierColumnName == null)
                identifierColumnName = "GLOBAL_IDENTIFIER";
        } 
//        catch (ClassNotFoundException e) {
//            log.error("initialize: ", e);
//        } 
        catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    private void generateCollection(String URI, String collectionString, String username, String password) {
        //TODO: auto generate?
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

    /*
     * NOTE: Collection is not implemented!
     * 
     * */
    public synchronized void insertMetadata(String identifier, String metadata, String collection) throws InsertMetadataException {
        try {
            Connection con = getConnection();
            PreparedStatement pstmt;
            
            pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE "+identifierColumnName+" = ?");
            pstmt.setString(1, identifier);
            pstmt.execute();
            pstmt.close();

            java.sql.Clob clobData = getCLOB(metadata, con);

            pstmt = con.prepareStatement(
                    "INSERT INTO " + tableName + " ("+identifierColumnName+", " + columnName + ") " +
            "VALUES(?, XMLType(?))");

            pstmt.setString(1, identifier);
            pstmt.setClob(2, clobData);

            pstmt.execute();
            pstmt.close();
            con.close();
            log.info("insertMetadata:identifier:\""+identifier+"\"");
        } catch (SQLException e) {
            log.error("insertMetadata:identifier:\""+identifier+"\" ", e);
            throw new InsertMetadataException(e);
        } 
    }
    
    private static CLOB getCLOB(String xmlData, Connection conn) throws SQLException {
		CLOB tempClob = null;
		try {
			// If the temporary CLOB has not yet been created, create new
			tempClob = CLOB.createTemporary(conn, true, CLOB.DURATION_SESSION);

			// Open the temporary CLOB in readwrite mode to enable writing
			tempClob.open(CLOB.MODE_READWRITE);
			// Get the output stream to write
			Writer tempClobWriter = tempClob.getCharacterOutputStream();
			// Write the data into the temporary CLOB
			tempClobWriter.write(xmlData);

			// Flush and close the stream
			tempClobWriter.flush();
			tempClobWriter.close();

			// Close the temporary CLOB
			tempClob.close();
		} catch (SQLException sqlexp) {
			tempClob.freeTemporary();
			sqlexp.printStackTrace();
		} catch (Exception exp) {
			tempClob.freeTemporary();
			exp.printStackTrace();
		}
		return tempClob;
	} 
}

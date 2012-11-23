package org.ariadne_eu.metadata.delete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

public class DeleteMetadataIBMDB2DbImpl extends DeleteMetadataImpl {
    private static Logger log = Logger.getLogger(DeleteMetadataIBMDB2DbImpl.class);

    private String tableName;
    private String columnName;
    private String identifierColumnName;

    public DeleteMetadataIBMDB2DbImpl() {
    }

    public DeleteMetadataIBMDB2DbImpl(int implementation) {
        setImplementation(implementation);
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
        	Class.forName("com.ibm.db2.jcc.DB2Driver");
            tableName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_TABLENAME);
            if (tableName == null)
                tableName = "Metadatastore";
            columnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_COLUMNNAME);
            if (columnName == null)
                columnName = "lomxml";
            identifierColumnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_SQL_IDCOLUMNNAME);
            if (identifierColumnName == null)
                identifierColumnName = "GLOBAL_IDENTIFIER";
        } catch (ClassNotFoundException e) {
            log.error("initialize: ", e);
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    private Connection getConnection() throws SQLException {
        String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI + "." + getImplementation());
        if (URI == null)
            URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_URI);
        String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME + "." + getImplementation());
        if (username == null)
            username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_USERNAME);
        String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD + "." + getImplementation());
        if (password == null)
            password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_PASSWORD);
        return DriverManager.getConnection(URI,username, password);
    }

    public synchronized void deleteMetadata(String identifier) {
        try {
            Connection con = getConnection();
            PreparedStatement pstmt;
            
            pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE "+identifierColumnName+" = ?");
            pstmt.setString(1, identifier);
            pstmt.execute();
            pstmt.close();
            con.close();
            log.info("deleteMetadata:identifier:\""+identifier+"\"");
        } catch (SQLException e) {
            log.error("deleteMetadata:identifier:\""+identifier+"\" ", e);
        } 
    }
}

package org.ariadne_eu.content.retrieve;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.QueryMetadataException;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.QueryMetadataImpl;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.utils.config.RepositoryConstants;

import com.ibm.db2.jcc.DB2Xml;

/**
 * Created by ben
 * Date: 3-mrt-2007
 * Time: 15:23:30
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveContentIBMDB2DbImpl extends RetrieveContentImpl {

    private static Logger log = Logger.getLogger(RetrieveContentIBMDB2DbImpl.class);

    private String tableName;
    private String columnName;
    private String identifierColumnName;
    private static Vector xpathIdentifiers;
    private static Vector xpathLocations;
    private static String xmlns;
    private static String collection;
    
    private static final int DATA_BLOCK_SIZE = 1024;


    public RetrieveContentIBMDB2DbImpl() {
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
        	Class.forName("com.ibm.db2.jcc.DB2Driver");
            //TODO: auto generate?
//                if(collection == null)
//                    generateCollection(URI, collectionString, username, password);
            tableName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_XMLDB_SQL_TABLENAME);
            if (tableName == null)
                tableName = "Contentstore";
            columnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_XMLDB_SQL_COLUMNNAME);
            if (columnName == null)
                columnName = "contentxml";
            identifierColumnName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_XMLDB_SQL_IDCOLUMNNAME);
            if (identifierColumnName == null)
                identifierColumnName = "GLOBAL_IDENTIFIER";
            // to get the location
            xmlns = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_XMLNS_XSD); //XMLNS is not query-language dependent
            xpathIdentifiers = new Vector();
            if (PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + ".1") == null)
            	xpathIdentifiers.add("general/identifier/entry/text()");
            else {
                int i = 1;
                while(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i) != null) {
                	xpathIdentifiers.add(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i));
                    i++;
                }
            }
            
            xpathLocations = new Vector();
            if (PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_MD_XPATHQRY_LOCATION + ".1") == null)
            	xpathLocations.add("technical/location/text()");
            else {
                int i = 1;
                while(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_MD_XPATHQRY_LOCATION + "." + i) != null) {
                	xpathLocations.add(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_MD_XPATHQRY_LOCATION + "." + i));
                    i++;
                }
            }
            
            collection = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_LOC);
            if(collection == null) {
                collection = "collection(\"metadatastore\")";
                log.warn("initialize:property \""+ RepositoryConstants.getInstance().MD_DB_XMLDB_LOC +"\" not defined");
            }
        } catch (ClassNotFoundException e) {
            log.error("initialize: ", e);
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

//    private static void generateCollection(String URI, String collectionString, String username, String password) {
//        //TODO: auto generate?
//        try {
//            Collection root = DatabaseManager.getCollection(URI + "/db", username, password);
//            CollectionManagementService mgtService = (CollectionManagementService)
//                root.getService("CollectionManagementService", "1.0");
//            collection = mgtService.createCollection(collectionString.substring("/db".length()));
//            collection = DatabaseManager.getCollection(URI + collectionString, username, password);
//        } catch (XMLDBException e1) {
//            e1.printStackTrace();
//        }
//    }


    public DataHandler retrieveContent(String identifier) {
    	String location = null;
		File file = new File(identifier);
    	try {
			String cntMetadata = getMetadataForID(identifier);
			if (cntMetadata == null) {
				location = retrieveMetadataLocation(identifier);
					        		
				if (location != null) {
					BufferedInputStream in = new BufferedInputStream(new URL(location).openStream());
					FileOutputStream fos = new FileOutputStream(file);
					BufferedOutputStream bout = new BufferedOutputStream(fos,DATA_BLOCK_SIZE);
					byte data[] = new byte[DATA_BLOCK_SIZE];
					while(in.read(data,0,DATA_BLOCK_SIZE)>=0)
						bout.write(data);
					bout.close();
					in.close();
				}
			} else {
				file = getFileFromMetadata(cntMetadata);
			}
			
			
		} catch (MalformedURLException e) {
			log.error("retrieveContent:identifier=" + identifier, e);
		} catch (FileNotFoundException e) {
			log.error("retrieveContent:identifier=" + identifier, e);
		} catch (IOException e) {
			log.error("retrieveContent:identifier=" + identifier, e);
		}
		if (file == null || !file.exists()) {
		    return null;
		}
		return new DataHandler(new FileDataSource(file));
    }
    
    public String retrieveFileName(String identifier) {
    	String location = null;
    	String metadata = getMetadataForID(identifier);
        if (metadata == null){
//            return "";
        	location = retrieveMetadataLocation(identifier); 
        	if (location != null)
        		return location;
        	else
        		return "";
        }
        String fileName = getFileNameFromMetadata(metadata);
        return fileName;
    }
    
    public String retrieveFileType(String identifier) {
    	String metadata = getMetadataForID(identifier);
        if (metadata == null)
            return "";
        String fileType = getFileTypeFromMetadata(metadata);
        return fileType;
    }

    private String getMetadataForID(String identifier) {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement("SELECT "+columnName+" FROM "+tableName+" WHERE "+identifierColumnName+" = ?");
            pstmt.setString(1, identifier);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                DB2Xml xml = (DB2Xml) rs.getObject(1);
                return xml.getDB2String();
            }
        } catch (SQLException e) {
            log.error("getMetadataForID:identifier=" + identifier, e);
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {
                log.error("getMetadataForID:identifier=" + identifier, e);
            }
        }
        return null;
    }
    
    private String retrieveMetadataLocation (String identifier) {
    	String location = null;
    	try {
			
	    	QueryMetadataImpl xqueryImpl = (QueryMetadataImpl) QueryMetadataFactory.getQueryImpl(-1);
			
			for (int i = 0; i < xpathIdentifiers.size() && location == null; i++) {
				String xpathIdentifier = (String) xpathIdentifiers.elementAt(i);
				for (int j = 0; j < xpathLocations.size() && location == null; j++) {
					String xpathLocation = (String) xpathLocations.elementAt(j);
					String xquery = "xquery version \"1.0\";\n" +
	        		(xmlns == null ? "" : "declare default element namespace \"" + xmlns + "\"; (:hello:)\n") + 
	        		"for $x in " + collection + " " +
	        		"where $x/lom/" + xpathIdentifier + " = \"" + identifier + "\" "+
	        		"return $x/lom/" + xpathLocation;
	        		location = xqueryImpl.xQuery(xquery);
				}
			} 
    	} catch (QueryTranslationException e) {
			log.error("retrieveContent:identifier=" + identifier, e);
		} catch (QueryMetadataException e) {
			log.error("retrieveContent:identifier=" + identifier, e);
		}
		return location;
    }

    private static File getFileFromMetadata(String metadata) {
        int start = metadata.indexOf("<fullpath>") + "<fullpath>".length();
        int end = metadata.indexOf("</fullpath>");
        String filename = metadata.substring(start, end);
        return new File(filename);
    }
    
    private static String getFileNameFromMetadata(String metadata) {
        int start = metadata.indexOf("<filename>") + "<filename>".length();
        int end = metadata.indexOf("</filename>");
        String fileName = metadata.substring(start, end);
        
        return fileName;
    }
    
    private static String getFileTypeFromMetadata(String metadata) {
        int start = metadata.indexOf("<filetype>") + "<filetype>".length();
        int end = metadata.indexOf("</filetype>");
        String fileType = metadata.substring(start, end);
        
        return fileType;
    }

    private Connection getConnection() throws SQLException {
        String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_URI);
        String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_USERNAME);
        String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_PASSWORD);
        return DriverManager.getConnection(URI,username, password);
    }
}

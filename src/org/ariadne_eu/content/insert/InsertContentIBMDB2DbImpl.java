package org.ariadne_eu.content.insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.rmi.dgc.VMID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

import com.ibm.db2.jcc.DB2Xml;

/**
 * Created by ben
 * Date: 3-mrt-2007
 * Time: 15:23:30
 * To change this template use File | Settings | File Templates.
 */
public class InsertContentIBMDB2DbImpl extends InsertContentImpl {

    private static Logger log = Logger.getLogger(InsertContentIBMDB2DbImpl.class);

    private String tableName;
    private String columnName;
    private String identifierColumnName;

    public InsertContentIBMDB2DbImpl() {
        initialize();
    }

    public InsertContentIBMDB2DbImpl(int nb) {
        setNumber(nb);
        initialize();
    }

    void initialize() {
        super.initialize();
        try {
//            String driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_DRIVER + "." + getNumber());
//            if (driver == null)
//                driver = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_DRIVER);
//            Class.forName(driver);
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
        } catch (ClassNotFoundException e) {
            log.error("initialize: ", e);
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }


//    public void insertContent(String identifier, DataHandler dataHandler) {
//        try {
//            File file = getFileForID(identifier, "", "");
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            FileOutputStream outputStream = new FileOutputStream(file);
//            dataHandler.writeTo(outputStream);
//            outputStream.flush();
//            outputStream.close();
//            dataHandler.getInputStream().close();
////            return true;
//            log.info("insertContent:identifier:\""+identifier+"\"");
//        } catch (IOException e) {
//            log.error("insertContent:identifier:\""+identifier+"\" ", e);
////            return false;
//        }
//    }
    
    public synchronized void insertContent(String identifier, DataHandler dataHandler, String fileName, String fileType) {
        try {
            File file = getFileForID(identifier, fileName, fileType);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            dataHandler.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            dataHandler.getInputStream().close();
//            return true;
            log.info("insertContent:identifier:\""+identifier+"\"");
        } catch (IOException e) {
            log.error("insertContent:identifier:\""+identifier+"\" ", e);
//            return false;
        }
    }

//    private File getFileForID(String identifier) {
//        String metadata = getMetadataForID(identifier);
//        if (metadata == null) {
//            //identifier doesn't exist yet
//            return createUniqueFile(identifier);
//        } else {
//            return getFileFromMetadata(metadata);
//        }
//
//    }
    
    private File getFileForID(String identifier, String fileName, String fileType) {
        String metadata = getMetadataForID(identifier);
        if (metadata == null) {
            //identifier doesn't exist yet
            return createUniqueFile(identifier, fileName, fileType);
        } else {
        	File temp = getFileFromMetadata(metadata); 
        	storeIdentifierPath(identifier, temp.getName(), fileName, fileType, temp);
            return temp;
        }

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

    private static int counter = 0;
    private static Random random = new Random();
    private static VMID vmid = new VMID();

//    private File createUniqueFile(String identifier) {
//        byte[] junk = new byte[16];
//
//        random.nextBytes(junk);
//
//        String input = new StringBuffer().append(vmid).append(
//                new java.util.Date()).append(junk).append(counter++).append(identifier).toString();
//
//        byte[] bytes = getMD5Bytes(input.getBytes());
//        String name = new BigInteger(bytes).abs().toString();
//
//        String basePath = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DR_BASEPATH);
//        if (!basePath.endsWith(File.separator))
//            basePath += File.separator;
//        String fullPath = basePath + name;
//        File file = new File(fullPath);
//        if (!file.exists()) {
//            storeIdentifierPath(identifier, name, file);
//            return file;
//        }
//        else
//            return createUniqueFile(identifier); // bad luck, file already exists, retry
//    }
    
    private File createUniqueFile(String identifier, String fileName, String fileType) {
        byte[] junk = new byte[16];

        random.nextBytes(junk);

        String input = new StringBuffer().append(vmid).append(new java.util.Date()).append(junk).append(counter++).append(identifier).toString();

        byte[] bytes = getMD5Bytes(input.getBytes());
        String relativePath = new BigInteger(bytes).abs().toString();

        String basePath = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DR_BASEPATH);
        if (!basePath.endsWith(File.separator))
            basePath += File.separator;
        String fullPath = basePath + relativePath;
        File file = new File(fullPath);
        if (!file.exists()) {
            storeIdentifierPath(identifier, relativePath, fileName, fileType, file);
            return file;
        }
        else
            return createUniqueFile(identifier, fileName, fileType); // bad luck, file already exists, retry
    }
    
//    private File createUniqueFile(String identifier) {
//      String basePath = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DR_BASEPATH);
//      if (!basePath.endsWith(File.separator))
//          basePath += File.separator;
//      String fullPath = basePath + identifier + ".zip";
//      File file = new File(fullPath);
//      if (!file.exists()) {
//          storeIdentifierPath(identifier, identifier + ".zip", file);
//          return file;
//      }
//      else
//          return createUniqueFile(identifier); // bad luck, file already exists, retry
//  }

//    private void storeIdentifierPath(String identifier, String name, File file) {
//        String xml = "<content><identifier type=\"ariadneIDv1\">" + identifier + "</identifier><relativepath>" + name + "</relativepath><fullpath>" + file.getAbsolutePath() + "</fullpath></content>";
//
//
//        try {
//            Connection con = getConnection();
//
//            PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE "+identifierColumnName+" = ?");
//            pstmt.setString(1, identifier);
//            pstmt.execute();
//            pstmt.close();
//
//            java.sql.Blob blobData = com.ibm.db2.jcc.t2zos.DB2LobFactory.createBlob(xml.getBytes("UTF-8"));
//
//            pstmt = con.prepareStatement(
//                    "INSERT INTO " + tableName + " ("+identifierColumnName+", " + columnName + ") " +
//            "VALUES(?, XMLPARSE(document cast(? as Blob) strip whitespace))");
//
//            pstmt.setString(1, identifier);
//            pstmt.setBlob(2, blobData);
//
//            pstmt.execute();
//            pstmt.close();
//            con.close();
//            log.info("insertContent:identifier:\""+identifier+"\"");
//        } catch (SQLException e) {
//            log.error("insertContent:identifier:\""+identifier+"\" ", e);
//        } catch (UnsupportedEncodingException e) {
//            log.error("insertContent:identifier:\""+identifier+"\" ", e);
//        }
//    }
    
    private void storeIdentifierPath(String identifier, String relativePath, String fileName, String fileType,  File file) {
    	String xml = "<content><identifier type=\"ariadneIDv1\">" + identifier
		+ "</identifier><relativepath>" + relativePath
		+ "</relativepath><fullpath>" + file.getAbsolutePath()
		+ "</fullpath><filename>" + fileName + "</filename><filetype>" + fileType
		+ "</filetype></content>";


        try {
            Connection con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE "+identifierColumnName+" = ?");
            pstmt.setString(1, identifier);
            pstmt.execute();
            pstmt.close();

            java.sql.Blob blobData = com.ibm.db2.jcc.t2zos.DB2LobFactory.createBlob(xml.getBytes("UTF-8"));

            pstmt = con.prepareStatement(
                    "INSERT INTO " + tableName + " ("+identifierColumnName+", " + columnName + ") " +
            "VALUES(?, XMLPARSE(document cast(? as Blob) strip whitespace))");

            pstmt.setString(1, identifier);
            pstmt.setBlob(2, blobData);

            pstmt.execute();
            pstmt.close();
            con.close();
            log.info("insertContent:identifier:\""+identifier+"\"");
        } catch (SQLException e) {
            log.error("insertContent:identifier:\""+identifier+"\" ", e);
        } catch (UnsupportedEncodingException e) {
            log.error("insertContent:identifier:\""+identifier+"\" ", e);
        }
    }

    private Connection getConnection() throws SQLException {
        String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_URI);
        String username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_USERNAME);
        String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_PASSWORD);
        return DriverManager.getConnection(URI,username, password);
    }

    /**
     * Return an MD5 checksum for data as a byte array.
     *
     * @param data
     *            The data to checksum.
     * @return MD5 checksum for the data as a byte array.
     */
    private static byte[] getMD5Bytes(byte[] data)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            return digest.digest(data);
        }
        catch (NoSuchAlgorithmException nsae)
        {
        }

        // Should never happen
        return null;
    }

    private static File getFileFromMetadata(String metadata) {
        int start = metadata.indexOf("<fullpath>") + "<fullpath>".length();
        int end = metadata.indexOf("</fullpath>");
        String filename = metadata.substring(start, end);
        return new File(filename);
    }
}

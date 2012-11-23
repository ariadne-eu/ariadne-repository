package org.ariadne_eu.content.insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.dgc.VMID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.activation.DataHandler;
import javax.xml.transform.OutputKeys;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * Created by ben
 * Date: 3-mrt-2007
 * Time: 15:23:30
 * To change this template use File | Settings | File Templates.
 */
public class InsertContentExistDbImpl extends InsertContentImpl {

	private static Logger log = Logger.getLogger(InsertContentExistDbImpl.class);

	private Collection collection;

	public InsertContentExistDbImpl() {
		initialize();
	}

	public InsertContentExistDbImpl(int nb) {
		setNumber(nb);
		initialize();
	}

	void initialize() {
		super.initialize();
		try {
			String URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_URI + "." + getNumber());
			if (URI == null)
				URI = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_URI);
			try {
				Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
				Database database = (Database) cl.newInstance();
				DatabaseManager.registerDatabase(database);

				String username = PropertiesManager.getInstance()
						.getProperty(RepositoryConstants.getInstance().CNT_DB_USERNAME + "." + getNumber());
				if (username == null)
					username = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_USERNAME);
				String password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_PASSWORD + "." + getNumber());
				if (password == null)
					password = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DB_PASSWORD);

				collection = DatabaseManager.getCollection(URI, username,password);
				//TODO: auto generate?
				//                if(collection == null)
				//                    generateCollection(URI, collectionString, username, password);
			} catch (ClassNotFoundException e) {
				log.error("initialize: ", e);
			} catch (InstantiationException e) {
				log.error("initialize: ", e);
			} catch (IllegalAccessException e) {
				log.error("initialize: ", e);
			} catch (XMLDBException e) {
				//TODO: auto generate?
				//                generateCollection(URI, collectionString, username, password);
			}
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

//	public void insertContent(String identifier, DataHandler dataHandler) {
//		try {
//			File file = getFileForID(identifier, "", "");
//			if (!file.getParentFile().exists()) {
//				file.getParentFile().mkdirs();
//			}
//			FileOutputStream outputStream = new FileOutputStream(file);
//			dataHandler.writeTo(outputStream);
//			outputStream.flush();
//			outputStream.close();
//			dataHandler.getInputStream().close();
//			//            return true;
//			log.info("insertContent:identifier:\"" + identifier + "\"");
//		} catch (IOException e) {
//			log.error("insertContent:identifier:\"" + identifier + "\" ", e);
//			//            return false;
//		}
//	}

	public synchronized void insertContent(String identifier, DataHandler dataHandler, String fileName, String fileType) {
		//exist cant handle ":" on the identifier
		identifier = identifier.replaceAll(":", "_");
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
			log.info("insertContent:identifier:\"" + identifier + "\"");
		} catch (IOException e) {
			log.error("insertContent:identifier:\"" + identifier + "\" ", e);
			//            return false;
		}
	}

	private File getFileForID(String identifier, String fileName, String fileType) {
		String metadata = getMetadataForID(identifier);
		if (metadata == null) {
			//identifier doesn't exist yet
			return createUniqueFile(identifier, fileName, fileType);
		} else {
			File temp = getFileFromMetadata(metadata);
			storeIdentifierPath(identifier,temp.getName(),fileName,fileType,temp);
			return temp;
		}
	}
	

	private String getMetadataForID(String identifier) {
		String metadata = null;
		try {
			//retrieve document with given ID
			collection.setProperty(OutputKeys.INDENT, "no");
			XMLResource res = (XMLResource) collection.getResource(identifier);
			if (res != null) {
				metadata = (String) res.getContent();
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
		return metadata;
	}

	private static int counter = 0;
	private static Random random = new Random();
	private static VMID vmid = new VMID();

//	private File createUniqueFile(String identifier) {
//		byte[] junk = new byte[16];
//
//		random.nextBytes(junk);
//
//		String input = new StringBuffer().append(vmid).append(
//				new java.util.Date()).append(junk).append(counter++).append(
//				identifier).toString();
//
//		byte[] bytes = getMD5Bytes(input.getBytes());
//		String relativePath = new BigInteger(bytes).abs().toString();
//
//		String basePath = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DR_BASEPATH);
//		if (!basePath.endsWith(File.separator))
//			basePath += File.separator;
//		String fullPath = basePath + relativePath;
//		File file = new File(fullPath);
//		if (!file.exists()) {
//			storeIdentifierPath(identifier, relativePath, file);
//			return file;
//		} else
//			return createUniqueFile(identifier); // bad luck, file already exists, retry
//	}
	
	private File createUniqueFile(String identifier, String fileName, String fileType) {
		byte[] junk = new byte[16];

		random.nextBytes(junk);

		String input = new StringBuffer().append(vmid).append(
				new java.util.Date()).append(junk).append(counter++).append(
				identifier).toString();

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
		} else
			return createUniqueFile(identifier, fileName, fileType); // bad luck, file already exists, retry
	}

//	private void storeIdentifierPath(String identifier, String relativePath, File file) {
//		String xml = "<content><identifier type=\"ariadneIDv1\">" + identifier
//				+ "</identifier><relativepath>" + relativePath
//				+ "</relativepath><fullpath>" + file.getAbsolutePath()
//				+ "</fullpath><fileName>" + "" + "</fileName><fileType>" + ""
//				+ "</fileType></content>";
//
//		try {
//			XMLResource document = (XMLResource) collection.createResource(
//					identifier, "XMLResource");
//			document.setContent(xml);
//			collection.storeResource(document);
//		} catch (XMLDBException e) {
//			e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
//		}
//	}
	
	private void storeIdentifierPath(String identifier, String relativePath, String fileName, String fileType, File file) {
		String xml = "<content><identifier type=\"ariadneIDv1\">" + identifier
				+ "</identifier><relativepath>" + relativePath
				+ "</relativepath><fullpath>" + file.getAbsolutePath()
				+ "</fullpath><filename>" + fileName + "</filename><filetype>" + fileType
				+ "</filetype></content>";

		try {
			XMLResource document = (XMLResource) collection.createResource(
					identifier, "XMLResource");
			document.setContent(xml);
			collection.storeResource(document);
		} catch (XMLDBException e) {
			e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
		}
	}

	/**
	 * Return an MD5 checksum for data as a byte array.
	 *
	 * @param data
	 *            The data to checksum.
	 * @return MD5 checksum for the data as a byte array.
	 */
	private static byte[] getMD5Bytes(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");

			return digest.digest(data);
		} catch (NoSuchAlgorithmException nsae) {
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

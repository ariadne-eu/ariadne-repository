/**
 * 
 */
package org.ariadne_eu.content.insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * @author gonzalo
 *
 */
public class InsertContentFSImpl extends InsertContentImpl {
	private static Logger log = Logger.getLogger(InsertContentFSImpl.class);
	private File baseFolder;

	void initialize() {
		super.initialize();
		try {
			String basePath = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().CNT_DR_BASEPATH);
			if (basePath == null)
				log.error("initialize failed: no " + RepositoryConstants.getInstance().CNT_DR_BASEPATH + " found");
			else
				baseFolder = new File(basePath);
		} catch (Throwable t) {
			log.error("initialize: ", t);
		}
	}

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
			// return true;
			log.info("insertContent:identifier:\"" + identifier + "\"");
		} catch (IOException e) {
			log.error("insertContent:identifier:\"" + identifier + "\" ", e);
			// return false;
		}
	}

	private synchronized File getFileForID(String identifier, String fileName, String fileType) {
		String name = identifier.replaceAll(":", "_");
        name = name.replaceAll("/", ".s.");
		File idFolder = new File(baseFolder.getAbsolutePath() + File.separator + name + File.separator);
		if (idFolder.exists()) {
			File[] subFiles = idFolder.listFiles(); 
			if (subFiles.length > 0) {
				for (int i = 0; i < subFiles.length; i++) {
					File file = subFiles[i];
					file.delete();
				}
			}
		} else {
			idFolder.mkdir();
		}
		if (fileName.equalsIgnoreCase(""))
			fileName = name;
		return new File(baseFolder.getAbsolutePath() + File.separator + name + File.separator + fileName);	
	}

}

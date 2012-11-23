/**
 * 
 */
package org.ariadne_eu.metadata.delete;

import java.io.File;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * @author gonzalo
 *
 */
public class DeleteMetadataFSImpl extends DeleteMetadataImpl {
	
	private static Logger log = Logger.getLogger(DeleteMetadataFSImpl.class);
	private String dirString;

	
	void initialize() {
		super.initialize();
		try {
			dirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFS_DIR + "." + getImplementation());
			if (dirString == null)
				dirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_SPIFS_DIR);
			if (dirString == null)
				log.error("initialize failed: no " + RepositoryConstants.getInstance().MD_SPIFS_DIR + " found");
			File dir = new File(dirString);
			if (!dir.isDirectory())
				log.error("initialize failed: " + RepositoryConstants.getInstance().MD_SPIFS_DIR + " invalid directory");
		} catch (Throwable t) {
			log.error("initialize: ", t);
		}
	}
	
	@Override
	public synchronized void deleteMetadata(String identifier) {
		String name = identifier.replaceAll(":", "_");
        name = name.replaceAll("/", ".s.");
        
        File folder = new File(dirString);
        File[] subfolders = folder.listFiles();
        for (int i = 0; i < subfolders.length; i++) {
        	if (subfolders[i].isDirectory()) {
        		File result = new File(subfolders[i].getPath() + File.separator + name + ".xml");
        		result.delete();
        	}
		}
        
		File result = new File(dirString + name + ".xml");
		result.delete();
		
	}

}

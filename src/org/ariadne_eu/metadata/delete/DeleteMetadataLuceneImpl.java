package org.ariadne_eu.metadata.delete;

import java.io.File;

import net.sourceforge.minor.lucene.core.service.IndexService;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 25-aug-2007
 * Time: 12:21:35
 * To change this template use File | Settings | File Templates.
 */
public class DeleteMetadataLuceneImpl extends DeleteMetadataImpl {
	private static Logger log = Logger.getLogger(DeleteMetadataLuceneImpl.class);
	private File indexDir;

	public DeleteMetadataLuceneImpl() {
		initialize();
	}

	public DeleteMetadataLuceneImpl(int implementation) {
		setImplementation(implementation);
		initialize();
	}

	void initialize() {
		super.initialize();
		try {
			String indexDirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + "." + getImplementation());
			if (indexDirString == null)
				indexDirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR);
			if (indexDirString == null)
				log.error("initialize failed: no " + RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + " found");
			indexDir = new File(indexDirString);
			if (!indexDir.isDirectory())
				log.error("initialize failed: " + RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + " invalid directory");
			//TODO: check for valid lucene index
		} catch (Throwable t) {
			log.error("initialize: ", t);
		}
	}

	public synchronized void deleteMetadata(String identifier) {
		try {
			DeleteDelegateSingleStringImpl indexDeleterDelegate = new DeleteDelegateSingleStringImpl(identifier);
			
			IndexService.delete(indexDir, indexDeleterDelegate);
			log.info("deleteMetadata:identifier:\"" + identifier + "\"");
		} catch (Exception e) {
			log.error("deleteMetadata:identifier:\"" + identifier + "\" ", e);
		}
	}

	public File getIndexDir() {
		return indexDir;
	}
}

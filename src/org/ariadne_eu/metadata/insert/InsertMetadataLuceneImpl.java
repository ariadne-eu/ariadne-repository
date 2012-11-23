package org.ariadne_eu.metadata.insert;

import java.io.File;
import java.io.IOException;

import net.sourceforge.minor.lucene.core.service.IndexService;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;

/**
 * Created by ben
 * Date: 25-aug-2007
 * Time: 12:21:35
 * To change this template use File | Settings | File Templates.
 */
public class InsertMetadataLuceneImpl extends InsertMetadataImpl {
	private static Logger log = Logger.getLogger(InsertMetadataLuceneImpl.class);
	private File indexDir;

	public InsertMetadataLuceneImpl() {
		initialize();
	}

	public InsertMetadataLuceneImpl(int language) {
		setLanguage(language);
		initialize();
	}

	void initialize() {
		super.initialize();
		try {
			String indexDirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + "." + getLanguage());
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
	
	@Override
	public synchronized void insertMetadata(String identifier, String metadata, String collection) throws InsertMetadataException {
		insertMetadata(identifier, metadata, new String[] {collection});
		
	}

	public synchronized void insertMetadata(String identifier, String metadata, String[] collection) throws InsertMetadataException{
		try {
			InsertDelegateSingleStringImpl indexInserterDelegate = new InsertDelegateSingleStringImpl(identifier, metadata,collection);
			
			boolean create = createIndex(indexDir);
			
			IndexService.insert(indexDir, indexInserterDelegate, create);
			log.info("insertMetadata:identifier:\"" + identifier + "\"");
		} catch (Exception e) {
			log.error("insertMetadata:identifier:\"" + identifier + "\" ", e);
			throw new InsertMetadataException(e);
		}
	}
	
	public void optimizeLuceneIndex() {
		try {
			IndexService.optimize(indexDir);
		} catch (Exception e) {
			log.error("optimizeLuceneIndex:indexDir:\"" + indexDir + "\" ", e);
		}
	}
	
	public boolean createIndex(File indexDir) throws IOException {
		return !(IndexReader.indexExists(FSDirectory.open(indexDir)));
	}

	public void createLuceneIndex() {
		IndexWriter writer = null;
		try {
			DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
			writer = new IndexWriter(FSDirectory.open(indexDir), analyzer.getAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
			writer.setUseCompoundFile(true);
			writer.optimize();
		} catch (IOException e) {
			e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
				}
			}
		}
	}

	public File getIndexDir() {
		return indexDir;
	}

}

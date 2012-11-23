package net.sourceforge.minor.lucene.core.indexer;

import java.io.File;

import net.sourceforge.minor.lucene.core.utils.Check;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;
import org.ariadne_eu.utils.solr.SolrServerManagement;

public class IndexHandler implements IndexInserter, IndexDeleter, IndexUpdater {
	
	private static Logger log = Logger.getLogger(IndexHandler.class);
	
	private File indexDir;
	
	public IndexHandler(File _indexDir){
		Check.checkObject(_indexDir);
		this.indexDir = _indexDir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see indexer.IndexInserter#insert(indexer.IndexInserterDelegate)
	 */
	public final void insert(IndexInserterDelegate insert, boolean create) throws Exception {
		synchronized (indexDir) {
            IndexWriter writer = null;
            try {
            	DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
            	
    			writer = new IndexWriter(FSDirectory.open(this.getIndexDir()), analyzer.getAnalyzer(), create,IndexWriter.MaxFieldLength.UNLIMITED);
            	writer.setUseCompoundFile(true);
                insert.insert(writer);
            } catch(Exception e) {
            	log.error("insert: ", e);
    			throw new Exception("Cannot insert document", e);
            }
            finally {
                if (writer != null) {
                    writer.close();
                }
            }
            //Also has a critical section
//            ReaderManagement.getInstance().setNewReader(indexDir);
//            SolrServerManagement.getInstance().
        }
	}

	public final void delete(IndexDeleterDelegate delete) throws Exception {
		synchronized (indexDir) {
            IndexWriter writer = null;
            try {
            	DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
            	
    			writer = new IndexWriter(FSDirectory.open(this.getIndexDir()), analyzer.getAnalyzer(),IndexWriter.MaxFieldLength.UNLIMITED);
                delete.delete(writer);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            //	Also has a critical section
//            ReaderManagement.getInstance().setNewReader(indexDir);
        }
	}

	public final void update(IndexUpdaterDelegate update) throws Exception {
		synchronized (indexDir) {
            IndexWriter writer = null;
            try {
            	DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
    			
    			writer = new IndexWriter(FSDirectory.open(this.getIndexDir()), analyzer.getAnalyzer(),IndexWriter.MaxFieldLength.UNLIMITED);
                writer.setUseCompoundFile(true);
                update.update(writer);
                //writer.optimize();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            //		Also has a critical section
//            ReaderManagement.getInstance().setNewReader(indexDir);
        }
	}
	
	public final void optimize() throws Exception {
		synchronized (indexDir) {
            IndexWriter writer = null;
            try {
            	DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
    			
    			writer = new IndexWriter(FSDirectory.open(this.getIndexDir()), analyzer.getAnalyzer(),IndexWriter.MaxFieldLength.UNLIMITED);
                writer.optimize();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            //		Also has a critical section
//            ReaderManagement.getInstance().setNewReader(indexDir);
        }
	}

	public File getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(File indexDir) {
		this.indexDir = indexDir;
	}

}

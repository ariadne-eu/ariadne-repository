package net.sourceforge.minor.lucene.core.searcher;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

public class MemoryReaderManagement {
	private static MemoryReaderManagement instance = null;
	private static Logger log = Logger.getLogger(MemoryReaderManagement.class);
	private static IndexSearcher searcher = null;
	
	private MemoryReaderManagement(){
		System.out.println("Initializing Lucene InMemory...");
		String indexDirString = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR);
        if (indexDirString == null)
            log.error("initialize failed: no " + RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + " found");
        File indexDir = new File(indexDirString);
        RAMDirectory idx = null;
		try {
			idx = new RAMDirectory(FSDirectory.open(indexDir));
			searcher = new IndexSearcher(idx, true);
			System.out.println("Lucene InMemory loaded!");
		} catch (IOException e) {
			log.error(e);
		}            
	}
	
	public static void initialize() {
		
		if ( instance == null ){
            synchronized( MemoryReaderManagement.class ){
                if ( instance == null ){
                    instance = new MemoryReaderManagement();
                }
            }
        }
    }

    public static MemoryReaderManagement getInstance(){
        if ( instance == null ){
            synchronized( MemoryReaderManagement.class ){
                if ( instance == null ){
                    instance = new MemoryReaderManagement();
                }
            }
        }
        return instance;
    }
    
    public synchronized IndexSearcher getSearcher() {
    	return searcher;
    }
    
}
package net.sourceforge.minor.lucene.core.service;

import java.io.File;
import java.util.Date;

import net.sourceforge.minor.lucene.core.indexer.IndexDeleterDelegate;
import net.sourceforge.minor.lucene.core.indexer.IndexHandler;
import net.sourceforge.minor.lucene.core.indexer.IndexHandlerFactory;
import net.sourceforge.minor.lucene.core.indexer.IndexInserterDelegate;
import net.sourceforge.minor.lucene.core.indexer.IndexUpdaterDelegate;
import net.sourceforge.minor.lucene.core.utils.Check;

import org.apache.log4j.Logger;

public class IndexService {
	
    private static Logger log =  Logger.getLogger(IndexService.class);
    
	public static void insert(File indexDir, IndexInserterDelegate insert, boolean create) throws Exception{
		Check.checkObject(indexDir);
		Check.checkObject(insert);
		long start = new Date().getTime();
		IndexHandler indexHandler = IndexHandlerFactory.getIndexHandler(indexDir);
		indexHandler.insert(insert, create);
		long end = new Date().getTime();
		log.info("Indexing took "+(end - start)+" ms");
	}
	
	public static void update(File indexDir, IndexUpdaterDelegate update) throws Exception{
		Check.checkObject(indexDir);
		Check.checkObject(update);
		IndexHandler indexHandler = IndexHandlerFactory.getIndexHandler(indexDir);
		indexHandler.update(update);
	}
	
	public static void delete(File indexDir, IndexDeleterDelegate delete) throws Exception{
		Check.checkObject(indexDir);
		Check.checkObject(delete);
		long start = new Date().getTime();
		IndexHandler indexHandler = IndexHandlerFactory.getIndexHandler(indexDir);
		indexHandler.delete(delete);
		long end = new Date().getTime();
		log.info("Deleting took "+(end - start)+" ms");
	}
	
	/*public static String search(File indexDir, String query, IndexSearchDelegate resultFormat) throws Exception{
		Check.checkObject(indexDir);
		Check.checkString(query);
		Check.checkObject(resultFormat);
		return Searcher.search(indexDir, query, resultFormat);
	}
	
	public static String searchTerm(File indexDir, String field, IndexTermSearchDelegate resultFormat) throws Exception{
		Check.checkObject(indexDir);
		Check.checkString(field);
		Check.checkObject(resultFormat);
		return Searcher.searchTerm(indexDir, field, resultFormat);
	}*/
	
	public static void optimize(File indexDir) throws Exception{
		Check.checkObject(indexDir);
		IndexHandler indexHandler = IndexHandlerFactory.getIndexHandler(indexDir);
		indexHandler.optimize();
	}
}

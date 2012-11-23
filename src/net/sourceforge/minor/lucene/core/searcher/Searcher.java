/*package net.sourceforge.minor.lucene.core.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;


public class Searcher {

	private static Logger log =  Logger.getLogger(Searcher.class);

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      throw new Exception("Usage: java " + Searcher.class.getName()
        + " <index dir> <query>");
    }

    File indexDir = new File(args[0]);
    
    BufferedReader bR = new BufferedReader(new InputStreamReader(new FileInputStream("build/conf/query.txt"), "UTF-8"));
    String q = bR.readLine();
    
    
    if (!indexDir.exists() || !indexDir.isDirectory()) {
      throw new Exception(indexDir +
        " does not exist or is not a directory.");
    }
	  
    //search(indexDir, q);//TODO
  }
  
  public static String searchTerm(File indexDir, String field, IndexTermSearchDelegate result) throws Exception{
	  IndexReader reader = ReaderManagement.getInstance().getReader(indexDir);
	  TermEnum enum1 = reader.terms(new Term(field,""));
	  
	  String searchTermResult = result.result(enum1);
	  
//	  ReaderManagement.getInstance().unRegister(indexDir, reader);
	  
	  return searchTermResult;
  }
  
  
  public static String search(File indexDir, String q, IndexSearchDelegate result) throws Exception {
	IndexReader reader = ReaderManagement.getInstance().getReader(indexDir);
    IndexSearcher is = new IndexSearcher(reader);
    
    DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
    
    Query query = new QueryParser("contents",  analyzer.getAnalyzer()).parse(q);//TODO "contents"
    
    long start = new Date().getTime();
    Hits hits = is.search(query);
    long end = new Date().getTime();
    
    log.info("Found " + hits.length() + " + document(s) (in " + (end - start) +
    			" milliseconds) that matched query '" + q + "':");
    
    
    String searchResult = result.result(hits, is);
    
//    ReaderManagement.getInstance().unRegister(indexDir, reader);
    
    return searchResult;
  }
  
}*/
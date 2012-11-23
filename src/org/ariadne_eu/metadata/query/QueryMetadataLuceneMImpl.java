package org.ariadne_eu.metadata.query;

import java.io.File;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;
import net.sourceforge.minor.lucene.core.searcher.MemoryReaderManagement;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateARIADNERFJS;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateICOPERCompactJS;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateICOPERJS;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateICOPERLODCompactJS;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateLomImpl;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateMACEEnrichedLomImpl;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateRFMCoAffilList;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateRFMList;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateRFMEntity;
import org.ariadne_eu.metadata.resultsformat.ResultDelegatePlrfImpl;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateRLomImpl;
import org.ariadne_eu.metadata.resultsformat.ResultDelegateSolrImpl;
import org.ariadne_eu.metadata.resultsformat.TranslateResultsformat;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;



public class QueryMetadataLuceneMImpl extends QueryMetadataImpl {
	
    private static Logger log = Logger.getLogger(QueryMetadataLuceneMImpl.class);
    private File indexDir;
    private IndexSearcher searcher;


    void initialize() {
        super.initialize();
    }
    
    public synchronized String xQuery(String xQuery) throws QueryMetadataException {
    	throw new QueryMetadataException(new Exception("Not supported query language"));
    }

    public synchronized String query(String query, int start, int max, int resultsFormat) throws QueryTranslationException, QueryMetadataException {
        String lQuery = TranslateLanguage.translateToQuery(query, getLanguage(), TranslateLanguage.LUCENE, start, max, resultsFormat);
        return luceneQuery(lQuery, start, max, resultsFormat);
    }
    

    public synchronized int count(String query) throws QueryTranslationException, QueryMetadataException {
        String lQuery = TranslateLanguage.translateToCount(query, getLanguage(), TranslateLanguage.LUCENE);
        return luceneCount(lQuery, 1);
    }

    private synchronized String luceneQuery(String lQuery, int start, int max, int resultsFormat) {
        try {
        	int n = start + max - 1;
        	
        	TopDocs topDocs = null;
            IndexSearchDelegate result = null;
            
            if (resultsFormat == TranslateResultsformat.LOM) {
            	result = new ResultDelegateLomImpl(start, max);
            }else if (resultsFormat == TranslateResultsformat.RLOM) {
            	result = new ResultDelegateRLomImpl(start, max);
            } else if (resultsFormat == TranslateResultsformat.PLRF0 ||
                       resultsFormat == TranslateResultsformat.PLRF1 ||
                       resultsFormat == TranslateResultsformat.PLRF2 ||
                       resultsFormat == TranslateResultsformat.PLRF3) {
                result = new ResultDelegatePlrfImpl(start, max);
            } else if (resultsFormat == TranslateResultsformat.SOLR) {
            	result = new ResultDelegateSolrImpl(start,max,lQuery);
            } else if (resultsFormat == TranslateResultsformat.MELOM) {
            	result = new ResultDelegateMACEEnrichedLomImpl(start,max);
            } else if (resultsFormat == TranslateResultsformat.ATOM_LOM) {
            	result = new ResultDelegateLomImpl(start, max);
            } else if (resultsFormat == TranslateResultsformat.ICJS) {
            	result = new ResultDelegateICOPERCompactJS(start,max);
            } else if (resultsFormat == TranslateResultsformat.ILCJS) {
            	result = new ResultDelegateICOPERLODCompactJS(start,max);
            } else if (resultsFormat == TranslateResultsformat.IJS) {
            	result = new ResultDelegateICOPERJS(start,max);
            } else if (resultsFormat == TranslateResultsformat.ARFJS) {
            	result = new ResultDelegateARIADNERFJS(start,max,lQuery);
            } else if (resultsFormat == TranslateResultsformat.RFME) {
            	result = new ResultDelegateRFMEntity(start,max);
            } else if (resultsFormat == TranslateResultsformat.RFML) {
            	result = new ResultDelegateRFMList(start,max);
            } else if (resultsFormat == TranslateResultsformat.RFMCOAFFILL) {
            	result = new ResultDelegateRFMCoAffilList(start,max);
            } else {
            	//for the VsqlToLucene Implementation, when there is no resultformat defined!!
            	result = new ResultDelegateLomImpl(start, max);
            }
            
            if (resultsFormat == TranslateResultsformat.SOLR ||
            		resultsFormat == TranslateResultsformat.ARFJS)  {
            	topDocs = null;
            } else {
            	topDocs = getDocs(lQuery, n);
            }
            
            String searchResult = result.result(topDocs, searcher);

            return searchResult;
        } catch (Exception e) {
        	log.error("Lucene query exception",e);
            return null;
        } 
    }

    private synchronized int luceneCount(String lQuery, int n) {
        try {
        	searcher = MemoryReaderManagement.getInstance().getSearcher();
            return getDocs(lQuery, n).totalHits;
        } catch (Exception e) {
        	log.error("Lucene query exception",e);
            return -1;
        } 
    }

    private synchronized TopDocs getDocs(String lQuery, int n) {
    	
        try {
        	searcher = MemoryReaderManagement.getInstance().getSearcher();

			//XXX Note that QueryParser is not thread-safe.
			DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
			org.apache.lucene.search.Query query = new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,"contents",  analyzer.getAnalyzer()).parse(lQuery);
			
			return searcher.search(query, n);
		} catch (ParseException e) {
			log.error("Lucene parse exception",e);
		} catch (Exception e) {
			log.error("Lucene query exception",e);
		} 
		return null;
    }

    
}

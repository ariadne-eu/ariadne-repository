package org.ariadne_eu.metadata.resultsformat;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class ResultDelegateRFMCoAffilList implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateRFMCoAffilList.class);

    private int start;
    private int max;

    public ResultDelegateRFMCoAffilList(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;

	    ScoreDoc[] hits = topDocs.scoreDocs;

	    String query = "";
	    
	    Set<String> ids = new HashSet<String>();
	    
	    // && (max < 0 || i < start-1+max)
	    for (int i = start-1; i < topDocs.totalHits; i++) {
	    	doc = searcher.doc(hits[i].doc);
			Field[] singleIds = doc.getFields("rdf.publication.author.person.affiliation.organization.id");
			for (Field field : singleIds) {
				ids.add(field.stringValue());
			}
	    }
	    
	    for (String string : ids) {
			if(query != "") query += " OR ";
			query += "rdf.organization.id : " + string;
		}
	    
		log.info("ResultDelegateRFMCoAffilList" + ": query=" + query + ",startResult=" + start + ",nbResults=" + (max-start) + ",resultsFormat=" + TranslateResultsformat.RFML);
	    
	    String result =  QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(query, start, max, TranslateResultsformat.RFML );
	    return result;
	}
}

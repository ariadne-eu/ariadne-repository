package org.ariadne_eu.metadata.resultsformat;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class ResultDelegateRFMList implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateRFMList.class);

    private int start;
    private int max;

    public ResultDelegateRFMList(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;
	    
	    JSONObject resultJSON = new JSONObject();
	    JSONArray resultsJSON = new JSONArray();
	    
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    
	    resultJSON.put("total_results", topDocs.totalHits);
	    resultJSON.put("total_pages", Math.ceil(Double.valueOf(Integer.toString(topDocs.totalHits)) / max));
	    resultJSON.put("current_page", (start-1) / max + 1 );
	    resultJSON.put("items_per_page", max);
	    
	    
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
	    	doc = searcher.doc(hits[i].doc);
			try {
				JSONObject result = new JSONObject();
				JSONArray authorsJSON = new JSONArray();
				if (doc.get("rdf.person.about") != null) {
					result.put("rdf.person.about", doc.get("rdf.person.about"));
					result.put("rdf.person.name", doc.get("rdf.person.name"));
					resultsJSON.put(result);
				} else if (doc.get("rdf.publication.about") != null) {
					result.put("rdf.publication.about", doc.get("rdf.publication.about"));
					result.put("rdf.publication.title", doc.get("rdf.publication.title"));
					result.put("rdf.publication.year", doc.get("rdf.publication.year"));
					Field[] authors = doc.getFields("rdf.publication.author.person.about");
					for (int j = 0; j < authors.length; j++) {
						JSONObject authorJSON = new JSONObject();
						Field author = authors[j];
						authorJSON.put("rdf.person.about", author.stringValue());
						authorsJSON.put(authorJSON);
					}
					result.put("rdf.publication.author", authorsJSON);
					resultsJSON.put(result);
				} else if (doc.get("rdf.organization.about") != null) {
					result.put("rdf.organization.about", doc.get("rdf.organization.about"));
					result.put("rdf.organization.fn", doc.get("rdf.organization.fn"));
					resultsJSON.put(result);
				}
				
			} catch (JSONException e) {
				log.debug("result :: id=" + doc.get("key"), e);
				log.error(e);
			}
			log.debug(doc.get("key") + " = " + hits[i].score);
	    }
	    resultJSON.put("documents", resultsJSON);
		return resultJSON.toString();
    	
	}
}

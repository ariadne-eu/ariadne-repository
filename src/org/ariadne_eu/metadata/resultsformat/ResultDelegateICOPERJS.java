package org.ariadne_eu.metadata.resultsformat;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResultDelegateICOPERJS implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateICOPERJS.class);

    private int start;
    private int max;

    public ResultDelegateICOPERJS(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;
	    
	    JSONObject resultJson = new JSONObject();
	    JSONArray arrayJson = new JSONArray();
	    
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
        	doc = searcher.doc(hits[i].doc);
			JSONObject json = new JSONObject();
	    	
	    	try {
	    		json.put("id", doc.get("lom.metametadata.identifier.entry"));
	    		json.put("md", doc.get("md"));
			} catch (JSONException ex) {
				log.error(ex);
			}
			arrayJson.put(json);
			log.debug(doc.get("key") + " = " + hits[i].score);
	    }
		resultJson.put("results",arrayJson);
		return resultJson.toString();
	}
}

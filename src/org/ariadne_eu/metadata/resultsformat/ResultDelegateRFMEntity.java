package org.ariadne_eu.metadata.resultsformat;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class ResultDelegateRFMEntity implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateRFMEntity.class);

    private int start;
    private int max;

    public ResultDelegateRFMEntity(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;
	    JSONObject json = new JSONObject();
	    
	    
	    
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
	    	doc = searcher.doc(hits[i].doc);
			try {
				json = XML.toJSONObject(doc.get("md"));
			} catch (JSONException e) {
				log.debug("result :: id=" + doc.get("key"), e);
				log.error(e);
			}
			log.debug(doc.get("key") + " = " + hits[i].score);
	    }
		return json.toString();
    	
	}
}

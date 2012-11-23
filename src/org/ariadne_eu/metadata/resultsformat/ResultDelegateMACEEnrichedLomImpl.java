package org.ariadne_eu.metadata.resultsformat;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class ResultDelegateMACEEnrichedLomImpl implements IndexSearchDelegate {

    private int start;
    private int max;

    public ResultDelegateMACEEnrichedLomImpl(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;

	    StringBuilder sBuild = new StringBuilder();
	    sBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<results cardinality=\""+ topDocs.totalHits +"\">\n");
		
	    
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
        	doc = searcher.doc(hits[i].doc);
	    	sBuild.append(doc.get("maceenrichedlom")+"\n\n");
	    }
	    sBuild.append("</results>");

    	return sBuild.toString();
	}

}

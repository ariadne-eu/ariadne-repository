package org.ariadne_eu.metadata.resultsformat;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class ResultDelegatePlrfImpl implements IndexSearchDelegate {

    private int start;
    private int max;

    public ResultDelegatePlrfImpl(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
	    Document doc;

	    StringBuilder sBuild = new StringBuilder();
	    sBuild.append("<Record>\n");
	    ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
        	doc = searcher.doc(hits[i].doc);
	    	sBuild.append("<Metadata>").append(doc.get("md")).append("</Metadata>").append("\n\n");//TODO: only return the part reguired for the given plrf level
	    }
	    sBuild.append("</Record>");

    	return sBuild.toString();
	}

}

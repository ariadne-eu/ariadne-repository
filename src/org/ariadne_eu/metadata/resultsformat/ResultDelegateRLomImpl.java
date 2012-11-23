/**
 * 
 */
package org.ariadne_eu.metadata.resultsformat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import de.fit.cam.ranking.ServiceProvider;
import de.fit.cam.ranking.domain.RankedLom;
import de.fit.cam.ranking.service.RankingService;

/**
 * @author gonzalo
 *
 */
public class ResultDelegateRLomImpl implements IndexSearchDelegate {
	
	private static Logger log = Logger.getLogger(ResultDelegateRLomImpl.class);

	private int start;
    private int max;

    public ResultDelegateRLomImpl(int start, int max) {
        this.start = start;
        this.max = max;
    }

    public String result(TopDocs topDocs, org.apache.lucene.search.IndexSearcher searcher) throws Exception {
    	
	    Document doc;
	    HashMap<String, Integer> lRank = new HashMap<String, Integer>();

	    RankingService rankingService = ServiceProvider.getRankingService();
		
		List<RankedLom> loms = new ArrayList<RankedLom>();
		
		ScoreDoc[] hits = topDocs.scoreDocs;
	    for (int i = start-1; i < topDocs.totalHits && (max < 0 || i < start-1+max); i++) {
        	doc = searcher.doc(hits[i].doc);
    		loms.add(new RankedLom(doc.get("key"),hits[i].score));
    		lRank.put(doc.get("key"), i);
    	}
		List<RankedLom> results = rankingService.getLomRanking(loms, 0.5);
		
		//build the resultset
    	StringBuilder sBuild = new StringBuilder();
	    sBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<results cardinality=\""+topDocs.totalHits+"\">\n");
	    
	    for (RankedLom rankedLom : results) {
	    	sBuild.append((searcher.doc(hits[(Integer)lRank.get(rankedLom.getId())].doc)).get("md"));
	    	log.debug(rankedLom.getId()+":"+rankedLom.getRankingValue());
		}
	    sBuild.append("</results>");
	    
	    return sBuild.toString();
	}
}

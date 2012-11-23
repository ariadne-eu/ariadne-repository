package org.ariadne_eu.metadata.query.language;

import org.apache.log4j.Logger;
import org.ariadne_eu.metadata.query.lucene.query.LuceneQLMaker;
import org.ariadne_eu.metadata.query.lucene.query.QueryMakerException;


/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 17:28:38
 * To change this template use File | Settings | File Templates.
 */
public class LuceneHandler extends Translate {

    private static Logger log = Logger.getLogger(LuceneHandler.class);

    public LuceneHandler(int startQueryLanguage, int endQueryLanguage) {
        super(startQueryLanguage, endQueryLanguage);
    }

    public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
    	return query;
    }

    

    public String translateToCount(String query) throws QueryTranslationException {
        return translateToQuery(query, -1, -1, -1);
    }
}

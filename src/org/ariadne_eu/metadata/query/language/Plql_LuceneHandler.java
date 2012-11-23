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
public class Plql_LuceneHandler extends Translate {

    private static Logger log = Logger.getLogger(Plql_LuceneHandler.class);

    public Plql_LuceneHandler(int startQueryLanguage, int endQueryLanguage) {
        super(startQueryLanguage, endQueryLanguage);
    }

    public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
//        if (getStartQueryLanguage() != TranslateLanguage.LUCENE && getStartQueryLanguage() != TranslateLanguage.PLQL0 && getStartQueryLanguage() != TranslateLanguage.PLQL1 && getStartQueryLanguage() != TranslateLanguage.PLQL2)
    	if ( getStartQueryLanguage() != TranslateLanguage.PLQL0 && getStartQueryLanguage() != TranslateLanguage.PLQL1 && getStartQueryLanguage() != TranslateLanguage.PLQL2)
            throw new QueryTranslationException();
        try {

            return LuceneQLMaker.createQuery(getEunQueryLanguage(), query);
        } catch (QueryMakerException e) {
            log.error(e);
            return null;
        }
    }

    private String getEunQueryLanguage() {
        if (getStartQueryLanguage() == TranslateLanguage.PLQL0)
            return LuceneQLMaker.PLQL_L0;
        if (getStartQueryLanguage() == TranslateLanguage.PLQL1)
            return LuceneQLMaker.PLQL_L1;
        if (getStartQueryLanguage() == TranslateLanguage.PLQL2)
            return LuceneQLMaker.PLQL_L2;
//        if (getStartQueryLanguage() == TranslateLanguage.LUCENE)
//            return LuceneQLMaker.LuceneQL;
        return null;
    }

    public String translateToCount(String query) throws QueryTranslationException {
        return translateToQuery(query, -1, -1, -1);
    }
}

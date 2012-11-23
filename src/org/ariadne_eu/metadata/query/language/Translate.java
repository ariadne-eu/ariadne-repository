package org.ariadne_eu.metadata.query.language;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 17:21:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class Translate {
    private int startQueryLanguage;
    private int endQueryLanguage;

    protected Translate(int startQueryLanguage, int endQueryLanguage) {
        this.startQueryLanguage = startQueryLanguage;
        this.endQueryLanguage = endQueryLanguage;
    }


    protected int getEndQueryLanguage() {
        return endQueryLanguage;
    }

    protected int getStartQueryLanguage() {
        return startQueryLanguage;
    }

    public abstract String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException;
    public abstract String translateToCount(String query) throws QueryTranslationException;
}

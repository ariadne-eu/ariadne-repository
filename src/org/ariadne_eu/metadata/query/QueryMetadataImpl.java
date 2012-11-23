package org.ariadne_eu.metadata.query;

import org.ariadne_eu.metadata.query.language.QueryTranslationException;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 16:35:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class QueryMetadataImpl {
    private int language;

    public abstract String xQuery(String query) throws QueryTranslationException, QueryMetadataException;
    public abstract String query(String query, int start, int max, int resultsFormat) throws QueryTranslationException, QueryMetadataException;
    public abstract int count(String query) throws QueryTranslationException, QueryMetadataException;


    public int getLanguage() {
        return language;
    }

    void setLanguage(int language) {
        this.language = language;
    }

    void initialize() {
        
    }
}

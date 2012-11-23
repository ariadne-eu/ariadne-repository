//package org.ariadne_eu.metadata.query;
//
//import org.apache.log4j.Logger;
//import org.ariadne_eu.metadata.query.language.QueryTranslationException;
//import org.ariadne_eu.metadata.query.language.TranslateLanguage;
//
///**
// * Created by ben
// * Date: 5-mei-2007
// * Time: 16:36:40
// * To change this template use File | Settings | File Templates.
// */
//public abstract class XQueryMetadataImpl extends QueryMetadataImpl {
//
//    private static Logger log = Logger.getLogger(XQueryMetadataImpl.class);
//
//    public String query(String query, int start, int max, int resultsFormat) throws QueryTranslationException, QueryMetadataException {
//        String xQuery = TranslateLanguage.translateToQuery(query, getLanguage(), TranslateLanguage.XQUERY, start, max, resultsFormat);
//        return xQuery(xQuery);
//    }
//
//    public int count(String query) throws QueryTranslationException, QueryMetadataException {
//        String xQuery = TranslateLanguage.translateToCount(query, getLanguage(), TranslateLanguage.XQUERY);
//        return Integer.parseInt(xQuery(xQuery));
//    }
//
//    public abstract String xQuery(String xQuery) throws QueryMetadataException;
//}

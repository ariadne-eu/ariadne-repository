package org.ariadne_eu.metadata.query.language.plql_xquery;

import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.Translate;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 17:20:21
 * To change this template use File | Settings | File Templates.
 */
public class Plql_XQueryHandler extends Translate {

    private static Logger log = Logger.getLogger(Plql_XQueryHandler.class);
    private String collectionName;
    private String xmlns;
    private boolean wholeWord = true;

    public Plql_XQueryHandler(int startQueryLanguage, int endQueryLanguage) {
        super(startQueryLanguage, endQueryLanguage);
        initialize();
    }

    public void initialize() {
        try {
            collectionName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_LOC + "." + getStartQueryLanguage());
            if(collectionName == null)
                collectionName = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_DB_XMLDB_LOC);
            if(collectionName == null) {
                collectionName = "collection(\"metadatastore\")";
                log.warn("initialize:property \""+ RepositoryConstants.getInstance().MD_DB_XMLDB_LOC +"\" not defined");
            }
            xmlns = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_XMLNS_XSD); //XMLNS is not query-language dependent
            wholeWord = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_XQUERY_WHOLEWORD) == null || !PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_XQUERY_WHOLEWORD).equalsIgnoreCase("false");
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }

    public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
        if (getEndQueryLanguage() == TranslateLanguage.XQUERY) {
            switch(getStartQueryLanguage()) {
                case TranslateLanguage.PLQL0:
                    return plql0ToXQuery(query, startResult, nbResults, resultsFormat);
                case TranslateLanguage.PLQL1:
                    return plql1ToXQuery(query, startResult, nbResults, resultsFormat);
                case TranslateLanguage.PLQL2:
                    return plql2ToXQuery(query, startResult, nbResults, resultsFormat);
            }
        }
        throw new QueryTranslationException();
    }

    public String translateToCount(String query) throws QueryTranslationException {
        if (getEndQueryLanguage() == TranslateLanguage.XQUERY) {
            switch(getStartQueryLanguage()) {
                case TranslateLanguage.PLQL0:
                    return plql0ToXQueryCount(query);
                case TranslateLanguage.PLQL1:
                    return plql1ToXQueryCount(query);
                case TranslateLanguage.PLQL2:
                    return plql2ToXQueryCount(query);
            }
        }
        throw new QueryTranslationException();

    }

    public String plql0ToXQuery(String query, int startResult, int nbResults, int resultsFormat) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer0.query.PlqlLayer0Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer0.query.PlqlLayer0Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setStartResult(startResult);
        parser.setNbResults(nbResults);
        parser.setResultsFormat(resultsFormat);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql0ToXQuery:couldn't translate \"" + query + "\"");
        else
            log.debug("plql0ToXQuery:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }
    public String plql0ToXQueryCount(String query) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer0.count.PlqlLayer0Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer0.count.PlqlLayer0Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql0ToXQueryCount:couldn't translate \"" + query + "\"");
        else
            log.debug("plql0ToXQueryCount:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }

    public String plql1ToXQuery(String query, int startResult, int nbResults, int resultsFormat) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer1.query.PlqlLayer1Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer1.query.PlqlLayer1Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setStartResult(startResult);
        parser.setNbResults(nbResults);
        parser.setResultsFormat(resultsFormat);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql1ToXQuery:couldn't translate \"" + query + "\"");
        else
            log.debug("plql1ToXQuery:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }
    public String plql1ToXQueryCount(String query) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer1.count.PlqlLayer1Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer1.count.PlqlLayer1Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql1ToXQueryCount:couldn't translate \"" + query + "\"");
        else
            log.debug("plql1ToXQueryCount:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }

    public String plql2ToXQuery(String query, int startResult, int nbResults, int resultsFormat) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer2.query.PlqlLayer2Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer2.query.PlqlLayer2Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setStartResult(startResult);
        parser.setNbResults(nbResults);
        parser.setResultsFormat(resultsFormat);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql2ToXQuery:couldn't translate \"" + query + "\"");
        else
            log.debug("plql2ToXQuery:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }
    public String plql2ToXQueryCount(String query) {
        Reader r = new StringReader( query ) ;
        org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer2.count.PlqlLayer2Analyzer parser = new org.ariadne_eu.metadata.query.language.plql_xquery.translation.layer2.count.PlqlLayer2Analyzer( r ) ;
        parser.setCollection(collectionName);
        parser.setXmlns(xmlns);
        parser.setWholeWord(wholeWord);
        parser.parse();
        String xQuery = parser.getQuery();
        if (xQuery == null)
            log.info("plql2ToXQueryCount:couldn't translate \"" + query + "\"");
        else
            log.debug("plql2ToXQueryCount:translated \"" + query + "\" to \"" + xQuery + "\"");
        return xQuery;
    }
}

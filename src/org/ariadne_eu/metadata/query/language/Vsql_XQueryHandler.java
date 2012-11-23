package org.ariadne_eu.metadata.query.language;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.ariadne_eu.metadata.query.language.plql_xquery.Plql_XQueryHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 17:28:38
 * To change this template use File | Settings | File Templates.
 */
public class Vsql_XQueryHandler extends Translate {

    private static Logger log = Logger.getLogger(Vsql_XQueryHandler.class);

    public Vsql_XQueryHandler(int startQueryLanguage, int endQueryLanguage) {
        super(startQueryLanguage, endQueryLanguage);
    }

    public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
        if (getStartQueryLanguage() != TranslateLanguage.VSQL && getEndQueryLanguage() != TranslateLanguage.XQUERY)
            throw new QueryTranslationException();
        try {
            return new Plql_XQueryHandler(TranslateLanguage.PLQL0, getEndQueryLanguage()).translateToQuery(vsqlToPLQL(query), startResult, nbResults, resultsFormat);
        } catch (QueryTranslationException e) {
            log.error(e);
            return null;
        }
    }

    public String translateToCount(String query) throws QueryTranslationException {
        if (getStartQueryLanguage() != TranslateLanguage.VSQL && getEndQueryLanguage() != TranslateLanguage.XQUERY)
            throw new QueryTranslationException();
        try {
            return new Plql_XQueryHandler(TranslateLanguage.PLQL0, getEndQueryLanguage()).translateToCount(vsqlToPLQL(query));
        } catch (QueryTranslationException e) {
            log.error(e);
            return null;
        }
    }

    private String vsqlToPLQL(String query) {
        try {
            InputSource input = new InputSource(new StringReader(query));
            Node queryNode = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input).getFirstChild();
            NodeList nl = XPathAPI.selectNodeList(queryNode, "term/text()");

            String plql = "";

            for (int i = 0; i < nl.getLength(); i++) {
                String term =nl.item(i).getNodeValue();
                if (i == 0)
                    plql += "\"" + term + "\"";
                else
                    plql += " and \"" + term + "\"";
            }
            log.debug("vsqlToPLQL:translated \"" + query + "\" to \"" + plql + "\"");

            return plql;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}

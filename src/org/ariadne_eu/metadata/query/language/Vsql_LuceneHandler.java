/**
 * 
 */
package org.ariadne_eu.metadata.query.language;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author gonzalo
 *
 */
public class Vsql_LuceneHandler extends Translate {

    private static Logger log = Logger.getLogger(Vsql_LuceneHandler.class);

    public Vsql_LuceneHandler(int startQueryLanguage, int endQueryLanguage) {
        super(startQueryLanguage, endQueryLanguage);
    }

    public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
		if (getStartQueryLanguage() != TranslateLanguage.VSQL && getEndQueryLanguage() != TranslateLanguage.LUCENE) 
			throw new QueryTranslationException();

		try {
			return vsqlToLucene(query);
		} catch (RuntimeException e) {
			log.error(e);
			return null;
		}
	}

    public String translateToCount(String query) throws QueryTranslationException {
    	return translateToQuery(query, -1, -1, -1);
    }

    private String vsqlToLucene(String query) {
        try {
            InputSource input = new InputSource(new StringReader(query));
            Node queryNode = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input).getFirstChild();
            NodeList nl = XPathAPI.selectNodeList(queryNode, "term/text()");

            String lquery = "";

            for (int i = 0; i < nl.getLength(); i++) {
                String term =nl.item(i).getNodeValue();
                if (i == 0)
                	lquery += term ;
                else
                	lquery += " AND " + term;
            }
            log.debug("vsqlToLucene:translated \"" + query + "\" to \"" + lquery + "\"");

            return lquery;
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

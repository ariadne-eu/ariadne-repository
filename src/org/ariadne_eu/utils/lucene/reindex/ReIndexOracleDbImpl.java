/**
 * 
 */
package org.ariadne_eu.utils.lucene.reindex;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.metadata.insert.InsertMetadataImpl;
import org.ariadne_eu.metadata.insert.InsertMetadataLuceneImpl;
import org.ariadne_eu.metadata.query.QueryMetadataException;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.QueryMetadataImpl;
import org.ariadne_eu.metadata.query.QueryMetadataOracleDbImpl;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author bramv
 *
 */
public class ReIndexOracleDbImpl extends ReIndexImpl {
	
	private static String xquery;
    private static String xmlns ;
    private static int startResult = 1 ;
    private static int nbResults = 25 ;
    private static Vector xpathQueries;
    
    private static Logger log = Logger.getLogger(ReIndexOracleDbImpl.class);
	
    public ReIndexOracleDbImpl() {
    	initialize();
    }
    
	void initialize() {
        super.initialize();
        
        try {
            xmlns = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_XMLNS_XSD); //XMLNS is not query-language dependent
            xpathQueries = new Vector();
            if (PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + ".1") == null)
                xpathQueries.add("general/identifier/entry/text()");
            else {
                int i = 1;
                while(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i) != null) {
                    xpathQueries.add(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_XPATH_QRY_ID + "." + i));
                    i++;
                }
            }
            createXQuery();
        } catch (Throwable t) {
            log.error("initialize: ", t);
        }
    }
	
	/*
     * NOTE: Collection is not implemented!
     * 
     * */
	public void reIndexMetadata() {
		
		QueryMetadataImpl xqueryImpl = (QueryMetadataImpl) QueryMetadataFactory.getQueryImpl(-1);
		InsertMetadataImpl[] insertImpls = InsertMetadataFactory.getInsertImpl();
		InsertMetadataLuceneImpl luceneImpl = null;
		for (int i = 0; i < insertImpls.length; i++) {
			InsertMetadataImpl insertImpl = insertImpls[i];
			if (insertImpl instanceof InsertMetadataLuceneImpl)
				luceneImpl = (InsertMetadataLuceneImpl) insertImpl;
		}

		if (luceneImpl == null)
			return;

		luceneImpl.createLuceneIndex();

		String implementation = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().MD_INSERT_IMPLEMENTATION);
		if (implementation != null) {

			startResult = 1;

			while (true) {
				String results;
				try {
					createXQuery();
					results = (new QueryMetadataOracleDbImpl()).xQuery(xquery,startResult,nbResults);
					
					if (results == null)
						break;

					results = "<results>" + results + "</results>";
					startResult += nbResults;

					StringReader stringReader = new StringReader(results);
					InputSource input = new InputSource(stringReader);
					Document doc = null;
					try {
						doc = DocumentBuilderFactory.newInstance()
								.newDocumentBuilder().parse(input);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Node result = doc.getFirstChild();
					NodeList nl = result.getChildNodes();

					log.info("startResult:" + startResult + "NodeListLength:"+ nl.getLength());

					if (nl.getLength() == 0)
						break;

					for (int i = 0; i < nl.getLength(); i++) {
						Node item = nl.item(i);
						Element theNode = ((Element) item);
						String identifier = null;
						for (int j = 0; j < xpathQueries.size()
								&& identifier == null; j++) {
							String xpathQuery = (String) xpathQueries.elementAt(j);

							try {
								identifier = XPathAPI.selectSingleNode(theNode,xpathQuery).getNodeValue();
							} catch (Exception e) {
							}
						}

						StringWriter out = new StringWriter();
						XMLSerializer serializer = new XMLSerializer(out,new OutputFormat(doc));
						serializer.serialize(theNode);
						String lom = out.toString();

						if (identifier != null) 
							luceneImpl.insertMetadata(identifier, lom, "ARIADNE");
					}
				} catch (QueryMetadataException e) {
					log.error("reIndexMetadata: ", e);
				} catch (ClassCastException e) {
					log.error("reIndexMetadata: ", e);
				} catch (Exception e) {
					log.error("reIndexMetadata: ", e);
				}

			}
		}
	}
	
	public static void createXQuery(){
    	xquery = "xquery version \"1.0\";\n" +
    		(xmlns == null ? "" : "declare default element namespace \"" + xmlns + "\"; (:hello:)\n") + 
    		"for $lom in /lom return $lom ";
        
    }

}

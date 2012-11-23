/**
 * 
 */
package org.ariadne_eu.utils.lucene.analysis;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * @author gonzalo
 *
 */
public class DocumentAnalyzerFactory {
	
	private static Logger log = Logger.getLogger(DocumentAnalyzerFactory.class);
	
	private static DocumentAnalyzer analyzer;
	
	static {
        initialize();
    }

    public static void initialize() {
    	String analyzerClass = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_ANALYZER);
    	if (analyzerClass == null) {
    		analyzer = new LOMDocumentAnalyzer();
    	} else {
    		try {
				Class implClass = Class.forName(analyzerClass);
				analyzer = (DocumentAnalyzer) implClass.newInstance();
			} catch (ClassNotFoundException e) {
				log.error("DocumentAnalyzerFactory::Error while initializing query class", e);
			} catch (InstantiationException e) {
				log.error("DocumentAnalyzerFactory::Error while initializing query class", e);
			} catch (IllegalAccessException e) {
				log.error("DocumentAnalyzerFactory::Error while initializing query class", e);
			}
    	}
    }

    public static DocumentAnalyzer getDocumentAnalyzerImpl() {
        return (analyzer);
    }

}

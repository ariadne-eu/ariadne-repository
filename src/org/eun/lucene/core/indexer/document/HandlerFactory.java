/**
 * 
 */
package org.eun.lucene.core.indexer.document;

import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.document.LOMHandler;

/**
 * @author gonzalo
 *
 */
public class HandlerFactory {
	
	private static Logger log = Logger.getLogger(HandlerFactory.class);
	
	private static DocumentHandler handler;
	
	static {
        initialize();
    }

    public static void initialize() {
    	String handlerClass = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_HANDLER);
    	if (handlerClass == null) {
    		handler = new LOMHandler();
    	} else {
    		try {
				Class implClass = Class.forName(handlerClass);
				handler = (DocumentHandler) implClass.newInstance();
			} catch (ClassNotFoundException e) {
				log.error("HandlerFactory::Error while initializing query class", e);
			} catch (InstantiationException e) {
				log.error("HandlerFactory::Error while initializing query class", e);
			} catch (IllegalAccessException e) {
				log.error("HandlerFactory::Error while initializing query class", e);
			}
    	}
    }

    public static DocumentHandler getDocumentHandlerImpl() {
        return (handler);
    }

}

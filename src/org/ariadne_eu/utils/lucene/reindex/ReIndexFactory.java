package org.ariadne_eu.utils.lucene.reindex;


import org.apache.log4j.Logger;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;


public class ReIndexFactory {

    private static Logger log = Logger.getLogger(ReIndexFactory.class);
    
    private static ReIndexImpl reIndex;

    static {
        initialize();
    }

    public static void initialize() {
    	String reIndexClass = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_REINDEX);
    	if (reIndexClass == null) {
    		reIndex = new ReIndexIBMDB2DbImpl();
    	} else {
    		try {
				Class implClass = Class.forName(reIndexClass);
				reIndex = (ReIndexImpl) implClass.newInstance();
			} catch (ClassNotFoundException e) {
				log.error("ReIndexFactory::Error while initializing query class", e);
			} catch (InstantiationException e) {
				log.error("ReIndexFactory::Error while initializing query class", e);
			} catch (IllegalAccessException e) {
				log.error("ReIndexFactory::Error while initializing query class", e);
			}
    	}
    }

    public static ReIndexImpl getReIndexImpl() {
        return (reIndex);
    }

	
}

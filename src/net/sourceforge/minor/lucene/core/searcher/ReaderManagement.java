package net.sourceforge.minor.lucene.core.searcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

public class ReaderManagement {
	private static ReaderManagement instance = null;
	private static Logger log = Logger.getLogger(ReaderManagement.class);
	
	private Map<File, List<ReaderContainer>> mReaders;
	
	private ReaderManagement(){
		mReaders = new HashMap<File, List<ReaderContainer>>();
	}

    public static ReaderManagement getInstance(){
        if ( instance == null ){
            synchronized( ReaderManagement.class ){
                if ( instance == null ){
                    instance = new ReaderManagement();
                }
            }
        }
        return instance;
    }
    
    /*
     * Get the last reader for the index in argument and register (inc a counter for that reader)
     */
    public synchronized IndexReader getReader(File indexDir) throws Exception{
    	if (!mReaders.containsKey(indexDir)){
    		setNewReader(indexDir);
    		return getReader(indexDir);
    	}
    	synchronized (mReaders) {
    		try{
    			
    		List<ReaderContainer> lReader = mReaders.get(indexDir);
    		log.debug("getReader :: lReader size = "+lReader.size()+" | nb index files= "+indexDir.listFiles().length);
    		
    		if (lReader.size() ==  0) {
    			setNewReader(indexDir);
    		} else {
    			ReaderContainer readerContainer = lReader.get(lReader.size()-1);
    			if (readerContainer.isClosable()){
    	    		readerContainer.close();
    	    		lReader.remove(readerContainer);
    	    		log.debug("getReader :: closed = (unRegister) size lreader = "+lReader.size());
    	    		setNewReader(indexDir);
    	    		
    	    	}
    		}

    		ReaderContainer readerContainer = lReader.get(lReader.size()-1);
            readerContainer.incNbSearch();
    		return readerContainer.getReader();
    		
    		} catch(Exception ex){
        		log.fatal("getReader :: mReaders.containsKey(indexDir)" +mReaders.containsKey(indexDir)+" ERR:"+ex);
        		if (mReaders.containsKey(indexDir)){
        			List<ReaderContainer> lReader = mReaders.get(indexDir);
        			log.fatal("getReader :: size reader for this index : "+lReader.size() +" index: "+indexDir.getCanonicalPath());
            	}
        		return null;
        	}
		}
    }
    
    /*
     * Before changing the reader, check if the latest reader is being used by someone, if not close that reader
     */
    public synchronized void setNewReader(File indexDir) throws IOException{
    	synchronized (mReaders) {
    		try{
        	List<ReaderContainer> lReader;
        	if (!mReaders.containsKey(indexDir)){
        		lReader = new ArrayList<ReaderContainer>();
        		lReader.add(new ReaderContainer(IndexReader.open(FSDirectory.open(indexDir))));
        		mReaders.put(indexDir, lReader);
        	} else {
        		lReader = mReaders.get(indexDir);
        		
        		if (lReader.size() > 0){
        			log.debug("setNewReader-STEP1 :: lReader.size() = " + lReader.size());
//        			for (int i = lReader.size() - 1; i >= 0 ; i--) {
//        				ReaderContainer readerContainer = lReader.get(i);
        			ReaderContainer readerContainer = lReader.get(lReader.size() - 1);
            			
                    	if (readerContainer.isClosable()){
                    		readerContainer.close();
                    		lReader.remove(readerContainer);
                    	}
//        			}
            	}
        		lReader.add(new ReaderContainer(IndexReader.open(FSDirectory.open(indexDir))));
        		log.debug("setNewReader :: lReader.size() = " + lReader.size());
        	}
        	
    		} catch(Exception ex){
        		log.fatal("setNewReader :: mReaders.containsKey(indexDir)" +mReaders.containsKey(indexDir)+" indexDir "+indexDir.getCanonicalPath()+" ERR:"+ex);
        	}
		}
    }
    
    /*
     * Unregister a reader, if it's the last one using it then close the reader except if it's the only reader for that index!
     */
    public synchronized void unRegister(File indexDir, IndexReader reader) throws Exception{
    	if (!mReaders.containsKey(indexDir)){
    		throw new Exception("Unauthorized operation");
    	}
    	synchronized (mReaders) {
	    	List<ReaderContainer> lReader = mReaders.get(indexDir);
	    	//
	    	log.debug("unRegister-STEP1 :: lReader size = "+lReader.size()+" | index of reader = "+lReader.indexOf(new ReaderContainer(reader)));
	    	//
	    	ReaderContainer readerContainer = lReader.get(lReader.indexOf(new ReaderContainer(reader)));
	    	readerContainer.decNbSearch();
	    	if (readerContainer.isClosable()){
	    		readerContainer.close();
	    		lReader.remove(readerContainer);
	    		log.debug("unRegister :: closed = (unRegister) size lreader = "+lReader.size()+" | reader = "+reader);
	    		
	    	}
	    	log.debug("unRegister-STEP2 :: lReader size = "+lReader.size()+" | index of reader = "+lReader.indexOf(new ReaderContainer(reader)));
    	}
    	
    }
    
}
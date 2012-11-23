package net.sourceforge.minor.lucene.core.indexer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class IndexHandlerFactory {
	
	private static Map<File, IndexHandler> mIndex  = new HashMap<File, IndexHandler>();
	
	public static IndexHandler getIndexHandler(File indexDir) {
		
		if ( ! mIndex.containsKey(indexDir)){
			synchronized (mIndex) {				
				if ( ! mIndex.containsKey(indexDir)){
					mIndex.put(indexDir, new IndexHandler(indexDir));
				}
			}	
		}
		
		return mIndex.get(indexDir);
	}

}

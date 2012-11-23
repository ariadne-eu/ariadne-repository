/**
 * 
 */
package org.ariadne_eu.metadata.delete;

import net.sourceforge.minor.lucene.core.indexer.IndexDeleterDelegate;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

/**
 * @author gonzalo
 *
 */
public class DeleteDelegateSingleStringImpl implements IndexDeleterDelegate {
	
	private String key;

	public DeleteDelegateSingleStringImpl(String _key){
		this.key = _key;
	}
	
	public void delete(IndexWriter writer) throws Exception {
		Term term = new Term("key", key);
		writer.deleteDocuments(term);
	}

}

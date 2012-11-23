///**
// * 
// */
//package org.ariadne_eu.utils.lucene.query;
//
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.search.Hits;
//import org.apache.lucene.search.IndexSearcher;
//
///**
// * @author gonzalo
// *
// */
//public class SingletonIndexSearcher {
//	
//	private static SingletonIndexSearcher instance;
//	private static IndexSearcher is;
//
//	private SingletonIndexSearcher(IndexReader reader) {
//		is = new IndexSearcher(reader);
//	}
//
//	public static SingletonIndexSearcher getSingletonIndexSearcher(IndexReader reader) {
//		if (instance == null)
//			instance = new SingletonIndexSearcher(reader);
//		is = new IndexSearcher(reader);
//		return instance;
//	}
//
//	public Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException();
//	}
//	
//	public static Hits search(org.apache.lucene.search.Query query) throws Exception{
//		return is.search(query);
//	}
//
//}

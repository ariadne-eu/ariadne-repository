package org.ariadne_eu.utils.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;


public abstract class DocumentAnalyzer extends Analyzer{

	public abstract Analyzer getAnalyzer();
	
}



/**
 * 
 */
package org.ariadne_eu.utils.lucene.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * @author gonzalo
 *
 */
public class LODDocumentAnalyzer extends DocumentAnalyzer{
	
	private static PerFieldAnalyzerWrapper pfanalyzer;
	
	public LODDocumentAnalyzer() {
		pfanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_29));
		pfanalyzer.addAnalyzer("key", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("date.insert", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("untokenized.xmlns", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.solr", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("collection", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("learningoutcome.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("learningoutcome.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("learningoutcome.description.string", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("learningoutcome.title.string", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("learningoutcome.title.string.language", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("learningoutcome.type.source", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("learningoutcome.type.value", new KeywordAnalyzer());
	}

	public PerFieldAnalyzerWrapper getAnalyzer() {
		return pfanalyzer;
	}

	@Override
	public TokenStream tokenStream(String arg0, Reader arg1) {
		return null;
	}

}

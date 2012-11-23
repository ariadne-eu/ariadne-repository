package org.ariadne_eu.utils.lucene.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class CollectionDocumentAnalyzer extends DocumentAnalyzer{
	
private static PerFieldAnalyzerWrapper pfanalyzer;
	
	public CollectionDocumentAnalyzer() {
		pfanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_29));
		pfanalyzer.addAnalyzer("key", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("date.insert", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("untokenized.xmlns", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.desciption.language", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.target.targetdescription.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.target.targetdescription.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.target.targetdescription.protocolidentifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.target.targetdescription.protocolidentifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("metadatacollection.target.targetdescription.location", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.name", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.version", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.protocolDescriptionBindingNamespace", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("protocol.protocolDescriptionBindingLocation", new KeywordAnalyzer());
	}

	public PerFieldAnalyzerWrapper getAnalyzer() {
		return pfanalyzer;
	}

	@Override
	public TokenStream tokenStream(String arg0, Reader arg1) {
		return null;
	}

}

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
public class SWRCDocumentAnalyzer extends DocumentAnalyzer{
	
	private static PerFieldAnalyzerWrapper pfanalyzer;
	
	public SWRCDocumentAnalyzer() {
		pfanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_29));
		pfanalyzer.addAnalyzer("collection", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("contents", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("date.insert", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("key", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.person.about", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.person.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.person.name", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.person.affiliation.organization.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.about", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.author.person.about", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.author.person.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.author.person.affiliation.organization.about", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.author.person.affiliation.organization.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.year", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.publication.title", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.publication.keywords", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.publication.spatial", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.organization.about", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.organization.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("rdf.organization.fn", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.organization.adr.description.country-name", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("rdf.organization.adr.description.locality", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		
	}

	public PerFieldAnalyzerWrapper getAnalyzer() {
		return pfanalyzer;
	}

	@Override
	public TokenStream tokenStream(String arg0, Reader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

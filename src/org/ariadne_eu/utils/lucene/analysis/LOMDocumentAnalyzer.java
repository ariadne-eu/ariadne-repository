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
public class LOMDocumentAnalyzer extends DocumentAnalyzer{
	
	private static PerFieldAnalyzerWrapper pfanalyzer;
	
	public LOMDocumentAnalyzer() {
		pfanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_29));
		pfanalyzer.addAnalyzer("key", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("collection", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("contents", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("date.insert", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("untokenized.xmlns", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("xmlns", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.solr", new KeywordAnalyzer());
		
		
		pfanalyzer.addAnalyzer("lom.general.identifier.entry.exact", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.general.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.general.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.general.title.string.exact", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.general.title.string", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("lom.general.keyword.string", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("lom.general.language", new KeywordAnalyzer());
		
		pfanalyzer.addAnalyzer("lom.metametadata.identifier.entry.exact", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.metametadata.identifier.entry", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.metametadata.identifier.catalog", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.metametadata.contribute.entity.provider", new KeywordAnalyzer());
		
		pfanalyzer.addAnalyzer("lom.technical.format", new KeywordAnalyzer());
		
		pfanalyzer.addAnalyzer("lom.educational.interactivitytype.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.learningresourcetype.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.interactivitylevel.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.semanticdensity.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.intendedenduserrole.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.context.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.typicalagerange.string", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.difficulty.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.educational.learningoutcome.identifier.entry", new KeywordAnalyzer());
		
		pfanalyzer.addAnalyzer("lom.rights.description.string.language", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.rights.description.string", new SnowballAnalyzer(Version.LUCENE_29,"English"));
		pfanalyzer.addAnalyzer("lom.rights.cost.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.rights.copyrightandotherrestrictions.value", new KeywordAnalyzer());
		
		pfanalyzer.addAnalyzer("lom.classification.purpose.value", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.purpose.value.exact", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.source.string", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.entry.string.exact", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.id", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.competency.eqf", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.domain.eqf", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.eqf", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.eqf.range", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.id.competency", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.id.domain", new KeywordAnalyzer());
		pfanalyzer.addAnalyzer("lom.classification.taxonpath.taxon.id.cso-foe", new KeywordAnalyzer());

		
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

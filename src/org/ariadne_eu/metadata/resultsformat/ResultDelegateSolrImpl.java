/**
 * 
 */
package org.ariadne_eu.metadata.resultsformat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.solr.SolrServerManagement;

/**
 * @author gonzalo
 * 
 */
public class ResultDelegateSolrImpl implements IndexSearchDelegate {

	private static Logger log = Logger.getLogger(QueryMetadataLuceneImpl.class);
	private int start;
	private int max;
	private String lQuery;
	private static Vector facetFields;

	static {
		try {

			facetFields = new Vector();
			int i = 1;

			Collection solrs = PropertiesManager.getInstance().getPropertyStartingWith(RepositoryConstants.getInstance().SR_SOLR_FACETFIELD + ".").values();
			for (Object object : solrs) {
				facetFields.add((String) object);
			}

			if (!(facetFields.size() > 0)) {
				log.error("initialize:property \"" + RepositoryConstants.getInstance().SR_SOLR_FACETFIELD + ".n\" not defined");
			}

		} catch (Throwable t) {
			log.error("initialize: ", t);
		}
	}

	public ResultDelegateSolrImpl(int start, int max, String lQuery) {
		this.start = start;
		this.max = max;
		this.lQuery = lQuery;

	}

	public String result(TopDocs topDocs, IndexSearcher searcher) throws Exception {
		SolrServerManagement serverMgt = SolrServerManagement.getInstance();
		
		
		StringBuilder sBuild = new StringBuilder();
		sBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>\n");		

		SolrQuery solrQuery = new SolrQuery().setQuery(lQuery).setFacet(true).setFacetLimit(-1).setFacetMinCount(0).setFacetSort(FacetParams.FACET_SORT_COUNT).setParam("rows", Integer.toString(max)).setParam("start", Integer.toString(start));


		for (Iterator iterator = facetFields.iterator(); iterator.hasNext();) {
			String facetField = (String) iterator.next();
			solrQuery.addFacetField(facetField);
		}
		
		QueryResponse rsp = serverMgt.getServer().query(solrQuery);
		
		
		List facetsFields = rsp.getFacetFields();
		sBuild.append("<facets>\n");
		if (facetsFields.size() > 0) {
			List facetValues;
			FacetField facetField;
			FacetField.Count innerFacetField;
			for (Iterator facetIterator = facetsFields.iterator(); facetIterator.hasNext();) {
				facetField = (FacetField) facetIterator.next();
				sBuild.append("<facet_field name=\"" + facetField.getName() + "\">\n");

				facetValues = facetField.getValues();
				if (facetValues != null) {
					for (Iterator ifacetIterator = facetValues.iterator(); ifacetIterator.hasNext();) {
						innerFacetField = (FacetField.Count) ifacetIterator.next();
						sBuild.append("<facet_count name=\"" + innerFacetField.getName() + "\">" + innerFacetField.getCount() + "</facet_count>\n");
					}
				}
				sBuild.append("</facet_field>\n");
			}
		}
		sBuild.append("</facets>\n");
		sBuild.append("</response>");
		
		return sBuild.toString();
	}

}

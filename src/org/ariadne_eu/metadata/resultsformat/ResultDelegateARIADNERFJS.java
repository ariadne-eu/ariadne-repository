package org.ariadne_eu.metadata.resultsformat;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.minor.lucene.core.searcher.IndexSearchDelegate;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.solr.SolrServerManagement;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResultDelegateARIADNERFJS implements IndexSearchDelegate {
	private static Logger log = Logger.getLogger(ResultDelegateARIADNERFJS.class);

	private int start;
	private int max;
	private String lQuery;
	private static Vector<String> facetFields;

	static {
		try {
			facetFields = new Vector<String>();

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

	public ResultDelegateARIADNERFJS(int start, int max, String lQuery) {
		this.start = start;
		this.max = max;
		this.lQuery = lQuery;
	}

	public String result(TopDocs topDocs, IndexSearcher searcher) throws JSONException, CorruptIndexException, IOException {
		SolrDocument doc;

		QueryResponse response = getSolrResponse();
		
		JSONObject resultsJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		JSONArray idArrayJson = new JSONArray();
		JSONArray metadataArrayJson = new JSONArray();
		resultJson.put("error", "");
		resultJson.put("errorMessage", "");
		resultJson.put("facets", getFacets(response.getFacetFields()));
		
		
		int size = (int)response.getResults().getNumFound();
		if (size == -1)
			size = Integer.MAX_VALUE;
		
		for (int i = 0; i < max && (max < 0 || i < size - start) ; i++) {
			JSONObject json = new JSONObject();
			doc = response.getResults().get(i);
			try {
				idArrayJson.put(doc.get("lom.general.identifier.entry"));
				
				if (doc.get("lom.general.title.string") != null)
					json.put("title", doc.get("lom.general.title.string"));
				else
					json.put("title", new String(""));
				
				if (doc.get("lom.general.description.string") != null)
					json.put("description", doc.get("lom.general.description.string"));
				else
					json.put("description", new String(""));

                               //** 18/11/12 NE: language
                                if (doc.get("lom.general.language") != null)
					json.put("language", doc.get("lom.general.language"));
				else
					json.put("language", new String(""));

				if (doc.get("lom.general.keyword.string") != null){
					Collection keywordsCollection = doc.getFieldValues("lom.general.keyword.string");
					String keywords = "";
					JSONArray keywordsArray = new JSONArray();
					for (Iterator iterator = keywordsCollection.iterator(); iterator.hasNext();) {
//						if (keywords.equals(""))
//							keywords += "&#044;";
						String keyword = (String) iterator.next();
//						keywords += keyword;
						keywordsArray.put(keyword);
					}
//					json.put("keywords", keywords);
					json.put("keywords", keywordsArray);
				}
				else
					json.put("keywords", new String(""));
				
				if (doc.get("lom.technical.location") != null)
					json.put("location", doc.get("lom.technical.location"));
				else
					json.put("location", new String(""));
				
				if (doc.get("lom.general.identifier.entry") != null)
					json.put("identifier", doc.get("lom.general.identifier.entry"));
				else
					json.put("identifier", new String(""));

                                /** In order to pass the context and the meta-metadata identifier **/
                                if (doc.get("lom.educational.context.value") != null)
                                        json.put("context", doc.get("lom.educational.context.value"));
                                else
                                        json.put("context", new String(""));

                                if (doc.get("lom.metametadata.identifier.entry") != null)
                                        json.put("metaMetadataId", doc.get("lom.metametadata.identifier.entry"));
                                else
                                        json.put("metaMetadataId", new String(""));

                                if (doc.get("lom.technical.format") != null)
                                        json.put("format", doc.get("lom.technical.format"));
                                else
                                        json.put("format", new String(""));

                                /** 18/11/12 NE: dataPovider **/
                                if (doc.get("lom.metametadata.identifier.catalog") != null)
                                        json.put("dataProvider", doc.get("lom.metametadata.identifier.catalog"));
                                else
                                        json.put("dataProvider", new String(""));

                            /** 18/11/12 NE: Added in order to pass thumbnail URI, **/
                                if (doc.get("lom.technical.duration") != null)
                                        json.put("thumbURL", doc.get("lom.technical.duration"));
                                else
                                        json.put("thumbURL", new String(""));

                            /** 18/11/12 NE: Added in order to pass license URI, **/
                             if (doc.get("lom.rights.copyrightandotherrestrictions.string") != null)
                                        json.put("licenses", "http://creativecommons.org/licenses/"+doc.get("lom.rights.copyrightandotherrestrictions.string")+"/3.0/");
                                else
                                        json.put("licenses", new String(""));

			} catch (JSONException ex) {
				log.error(ex);
			}
			metadataArrayJson.put(json);
		}
		resultJson.put("id", idArrayJson);
		resultJson.put("metadata", metadataArrayJson);
		resultJson.put("nrOfResults", size);

		resultsJson.put("result", resultJson);
		
		return resultsJson.toString();
	}
	
	private QueryResponse getSolrResponse() {
		SolrServerManagement serverMgt = SolrServerManagement.getInstance();

		SolrQuery solrQuery = new SolrQuery().setQuery(lQuery).setFacet(true).setFacetLimit(-1).setFacetMinCount(1).setFacetSort(FacetParams.FACET_SORT_COUNT).setParam("rows", Integer.toString(max)).setParam("start", Integer.toString(start));


		for (Iterator<String> iterator = facetFields.iterator(); iterator.hasNext();) {
			String facetField = (String) iterator.next();
			solrQuery.addFacetField(facetField);
		}
		QueryResponse rsp = null;
		
		try {
			rsp = serverMgt.getServer().query(solrQuery);
			
		} catch (SolrServerException e) {
			log.error("getSolrResponse: Solr server error", e);
		} catch (IOException e) {
			log.error("getSolrResponse: Solr I/O error", e);
		} 
		return rsp;
	}

	private JSONArray getFacets(List facetsFields) {
		JSONArray facetsJson = new JSONArray();
		try {
			if (facetsFields.size() > 0) {
				List<Count> facetValues;
				FacetField facetField;
				FacetField.Count innerFacetField;
				for (Iterator facetIterator = facetsFields.iterator(); facetIterator.hasNext();) {
					JSONObject facetJson = new JSONObject();
					facetField = (FacetField) facetIterator.next();
					facetJson.put("field", changeFacetName(facetField.getName()));

					facetValues = facetField.getValues();
					if (facetValues != null) {
						JSONArray valuesJson = new JSONArray();
						for (Iterator ifacetIterator = facetValues.iterator(); ifacetIterator.hasNext();) {
							JSONObject value = new JSONObject();
							innerFacetField = (FacetField.Count) ifacetIterator.next();
							value.put("val", innerFacetField.getName());
							value.put("count", innerFacetField.getCount());
							valuesJson.put(value);
						}
						facetJson.put("numbers", valuesJson);
					}
					facetsJson.put(facetJson);
				}

			}
		} catch (JSONException e) {
			log.error("getFacets: JSON format error", e);
		}
		return facetsJson;
	}

	private String changeFacetName(String internalName) {
		if (internalName.equalsIgnoreCase("lom.educational.learningresourcetype.value"))
			return "lrt";
		else if (internalName.equalsIgnoreCase("lom.educational.context.value"))
			return "context";
		else if (internalName.equalsIgnoreCase("lom.technical.format"))
			return "format";
		else if (internalName.equalsIgnoreCase("lom.general.language"))
			return "language";
		else if (internalName.equalsIgnoreCase("collection"))
			return "provider";
		else if (internalName.equalsIgnoreCase("lom.educational.interactivitytype.value"))
			return "it";
		else if (internalName.equalsIgnoreCase("lom.educational.interactivitylevel.value"))
			return "il";
		else if (internalName.equalsIgnoreCase("lom.educational.intendedenduserrole.value"))
			return "iur";
		else if (internalName.equalsIgnoreCase("lom.educational.typicalagerange.string"))
			return "tagr";
		else if (internalName.equalsIgnoreCase("lom.general.keyword.string"))
			return "keyword";
		else if (internalName.equalsIgnoreCase("lom.rights.description.string"))
			return "rights";
		else if (internalName.equalsIgnoreCase("lom.rights.copyrightandotherrestrictions.string"))
			return "licences";
                 else if (internalName.equalsIgnoreCase("lom.classification.taxonpath.taxon.entry.string"))
			return "classification";
                else if (internalName.equalsIgnoreCase("lom.educational.typicalagerange.string"))
			return "temporal";
                else if (internalName.equalsIgnoreCase("lom.general.coverage.string"))
			return "spatial";
                else if (internalName.equalsIgnoreCase("lom.classification.description.string"))
			return "common";

			return internalName;
	}
}

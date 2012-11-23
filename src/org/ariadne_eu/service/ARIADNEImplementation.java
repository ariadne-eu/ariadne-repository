package org.ariadne_eu.service;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.ariadne.util.Stopwatch;
import org.ariadne_eu.metadata.query.QueryMetadataException;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.metadata.resultsformat.TranslateResultsformat;
import org.ariadne_eu.utils.rest.Query;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.spi.resource.Singleton;
import com.sun.jersey.api.json.JSONWithPadding;

@Singleton
@Path("/ariadne")
public class ARIADNEImplementation {
	
	private static Logger log = Logger.getLogger(ARIADNEImplementation.class);
	
	@GET
	@Path("/rest")
	@Produces("application/json")
	public String query(@QueryParam("json") String json) {
		Stopwatch sw = new Stopwatch();
		sw.start();
		String query = "";
		Query qry = new Query();
		JSONObject jo;
		log.debug("query:json=" + json);
		try {
			jo = new JSONObject(json);
			qry.parseJson(jo);
			//CNF :: ((a OR b) AND (c OR d))
			if (qry.searchTerms != null) {
				for (int i = 0; i < qry.searchTerms.length; i++) {
					if (i > 0)
						query = query.concat(" AND ");
					for (int j = 0; j < qry.searchTerms[i].length; j++) {						
						if (j > 0)
							query = query.concat(" OR ");
						if (qry.searchTerms[i].length > 1 && j == 0 )
							query = query.concat(" ( ");
						query = query.concat(qry.searchTerms[i][j]);
						if (qry.searchTerms[i].length > 1 && j == (qry.searchTerms[i].length - 1) )
							query = query.concat(" ) ");
					}
					
				}
			}
			query = changeFacetName(query);
			if (qry.exclusionTerms != null) {
				for (int i = 0; i < qry.exclusionTerms.length; i++) {
//					System.out.println(qry.exclusionTerms[i]);
				}
			}
			if (qry.facets != null) {
				for (int i = 0; i < qry.facets.length; i++) {
//					System.out.println(qry.facets[i]);
				}
			}
			
//			System.out.println(qry.idListOffset);
//			System.out.println(qry.idListSize);
//			System.out.println(qry.maxCntFacets);
//			System.out.println(qry.sortKey);
			
			if (qry.rankingTerms != null) {
				for (int i = 0; i < qry.rankingTerms.length; i++) {
					System.out.println(qry.rankingTerms[i]);
				}
			}
			
			log.info("query:query="+query);
			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(query, qry.resultListOffset, qry.resultListSize, TranslateResultsformat.ARFJS);
			JSONObject jResults = new JSONObject(result);
			JSONObject jResult = jResults.getJSONObject("result");
			jResult.put("processingTime", sw.stop());
			
			return jResults.toString();
			
		} catch (JSONException e) {
			log.error("synchronousQuery: QueryTranslationException", e);
		}
		catch (QueryTranslationException e) {
			log.error("synchronousQuery: QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error("synchronousQuery: QueryMetadataException", e);
		}

		return "";
	}
	
	@GET
	@Path("/restp")
	@Produces({ "application/x-javascript", MediaType.APPLICATION_JSON })
	public JSONWithPadding querywithPadding(@QueryParam("callback") @DefaultValue("fn") String callback,@QueryParam("json") String json) {
		Stopwatch sw = new Stopwatch();
		sw.start();
		String query = "";
		Query qry = new Query();
		JSONObject jo;
		log.debug("query:json=" + json + ",callback:"+callback);
		try {
			jo = new JSONObject(json);
			qry.parseJson(jo);
			//CNF :: ((a OR b) AND (c OR d))
			if (qry.searchTerms != null) {
				for (int i = 0; i < qry.searchTerms.length; i++) {
					if (i > 0)
						query = query.concat(" AND ");
					for (int j = 0; j < qry.searchTerms[i].length; j++) {						
						if (j > 0)
							query = query.concat(" OR ");
						if (qry.searchTerms[i].length > 1 && j == 0 )
							query = query.concat(" ( ");
						query = query.concat(qry.searchTerms[i][j]);
						if (qry.searchTerms[i].length > 1 && j == (qry.searchTerms[i].length - 1) )
							query = query.concat(" ) ");
					}
					
				}
			}
			query = changeFacetName(query);
			if (qry.exclusionTerms != null) {
				for (int i = 0; i < qry.exclusionTerms.length; i++) {
//					System.out.println(qry.exclusionTerms[i]);
				}
			}
			if (qry.facets != null) {
				for (int i = 0; i < qry.facets.length; i++) {
//					System.out.println(qry.facets[i]);
				}
			}
			
//			System.out.println(qry.idListOffset);
//			System.out.println(qry.idListSize);
//			System.out.println(qry.maxCntFacets);
//			System.out.println(qry.sortKey);
			
			if (qry.rankingTerms != null) {
				for (int i = 0; i < qry.rankingTerms.length; i++) {
					System.out.println(qry.rankingTerms[i]);
				}
			}
			
			log.info("querywithPadding:query="+query);
			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(query, qry.resultListOffset, qry.resultListSize, TranslateResultsformat.ARFJS);
			JSONObject jResults = new JSONObject(result);
			JSONObject jResult = jResults.getJSONObject("result");
			jResult.put("processingTime", sw.stop());
			
			return new JSONWithPadding(jResults.toString(),callback);
			
		} catch (JSONException e) {
			log.error("synchronousQuery: QueryTranslationException", e);
		}
		catch (QueryTranslationException e) {
			log.error("synchronousQuery: QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error("synchronousQuery: QueryMetadataException", e);
		}

		return new JSONWithPadding("");
	}
	
	private String changeFacetName(String expression) {
		if (expression.contains("lrt:"))
			expression = expression.replace("lrt:", "lom.educational.learningresourcetype.value:");
		if (expression.contains("context:"))
			expression =  expression.replace("context:", "lom.educational.context.value:");
		if (expression.contains("format:"))
			expression =  expression.replace("format:", "lom.technical.format:");
		if (expression.contains("language:"))
			expression =  expression.replace("language:", "lom.general.language:");
		if (expression.contains("provider:"))
			expression =  expression.replace("provider:", "collection:");
		if (expression.contains("it:"))
			expression =  expression.replace("it:", "lom.educational.interactivitytype.value:");
		if (expression.contains("il:"))
			expression =  expression.replace("il:", "lom.educational.interactivitylevel.value:");
		if (expression.contains("iur:"))
			expression =  expression.replace("iur:", "lom.educational.intendedenduserrole.value:");
		if (expression.contains("tagr:"))
			expression =  expression.replace("tagr:", "lom.educational.typicalagerange.string:");
		if (expression.contains("collection:\"*\""))
			expression =  expression.replace("collection:\"*\"", "lom.solr:\"all\"");
		if (expression.contains("keyword:"))
			expression =  expression.replace("keyword:", "lom.general.keyword.string:");
		if (expression.contains("rights:"))
			expression =  expression.replace("rights:", "lom.rights.description.string:");
                if (expression.contains("licenses:"))
                        expression = expression.replace("licences:","lom.rights.copyrightandotherrestrictions.string:");
                if (expression.contains("classification:"))
                        expression = expression.replace("classification:","lom.classification.taxonpath.taxon.entry.string:");
                if (expression.contains("temporal:"))
                        expression = expression.replace("temporal:","lom.educational.typicalagerange.string:");
                if (expression.contains("spatial:"))
                        expression = expression.replace("coverage:","lom.general.coverage.string:");
                if (expression.contains("common:"))
                        expression = expression.replace("common:","lom.classification.description.string:");

		return expression;
	}
	
}

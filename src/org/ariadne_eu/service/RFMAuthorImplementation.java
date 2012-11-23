package org.ariadne_eu.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.ariadne.util.CsvReader;
import org.ariadne_eu.metadata.query.QueryMetadataException;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.QueryTranslationException;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.metadata.resultsformat.TranslateResultsformat;

@Path("/authors")
public class RFMAuthorImplementation extends RFMImplementation{

	private static Logger log = Logger.getLogger(RFMAuthorImplementation.class);

	@Path("/{set:(/set/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getAuthors(@PathParam("set") String set,@PathParam("page") String page, @PathParam("items") String items) {
		String query = "collection:persons";
		
		return doPagingSetQuery(set, query, page, items, "getAuthors",TranslateResultsformat.RFML);
	}

	@Path("/{author_id}")
	@GET
	@Produces("application/json")
	public String getAuthor(@PathParam("author_id") String id) {

		if (id.equalsIgnoreCase("")) {
			log.error("getAuthor: Not a valid author identifier");
			return "{ }";
		}

		log.info("getAuthor: author_id=" + id);
		try {
			String fullquery = "";
			if(idGroups.containsKey(id)) {
				for (String sameId : idGroups.get(id)) {
					if(!fullquery.equalsIgnoreCase("")) fullquery += " OR ";
					fullquery += "rdf.person.id : \"" + sameId + "\"";
				}
			}else {			
				fullquery = "rdf.person.id : \"" + id + "\"";
			}

			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
			return result;
		} catch (QueryTranslationException e) {
			log.error("getAuthor:QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error("getAuthor:QueryMetadataException", e);
		}
		return "{ }";
	}

	@Path("/search/{keyword}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getAuthorsFromKeyword(@PathParam("keyword") String keyword, @PathParam("page") String page, @PathParam("items") String items) {
		String thePage = null;
		if (page != null && !page.equalsIgnoreCase("")) {
			thePage = page.split("/")[2];
		}
		String theItems = null;
		if (items != null && !items.equalsIgnoreCase("")) {
			theItems = items.split("/")[2];
		}
		return getAuthorsFromQuery(keyword, thePage, theItems);
	}

	@Path("/search")
	@GET
	@Produces("application/json")
	public String getAuthorsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
		query = "collection:persons AND contents:" + query;
		return doQuery(query, page, items, "getAuthorsFromQuery",TranslateResultsformat.RFML);
	}

	@Path("/organization/{organization_id}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getAuthorsOrganization(@PathParam("organization_id") String id, @PathParam("keyword") String keyword, @PathParam("page") String page,
			@PathParam("items") String items) {
		if (id.equalsIgnoreCase("")) {
			log.error("getAuthorsOrganization: Not a valid organization identifier");
			return "{ }";
		}
		Vector<String> queries = new Vector<String>();

		String fullquery = "";
		if(idGroups.containsKey(id)) {
			for (String sameId : idGroups.get(id)) {
				if(!fullquery.equalsIgnoreCase("")) {
					fullquery += " OR ";
				}else {
					 fullquery += "(";
				}
				fullquery += "rdf.person.affiliation.organization.id : \"" + sameId + "\"";
			}
			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
		}else {			
			fullquery = "rdf.person.affiliation.organization.id : \"" + id + "\" ";
		}


		if (!keyword.equalsIgnoreCase("")) {
			queries.add("contents:" + keyword.split("/")[2]);
		}

		for (int i = 0; i < queries.size(); i++) {
			fullquery = fullquery.concat(" AND " + queries.get(i));
		}

		log.info("getAuthorsOrganization: organization_id=" + id);
		return doPagingQuery(fullquery, page, items, "getAuthorsOrganization",TranslateResultsformat.RFML);
	}
}

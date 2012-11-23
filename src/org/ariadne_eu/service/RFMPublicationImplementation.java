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

@Path("/publications")
public class RFMPublicationImplementation extends RFMImplementation{

	private static Logger log = Logger.getLogger(RFMPublicationImplementation.class);
	@Path("/{publication_id}")
	@GET
	@Produces("application/json")
	public String getPublication(@PathParam("publication_id") String id) {

		if (id.equalsIgnoreCase("")) {
			log.error("getPublication: Not a valid publication identifier");
			return "{ }";
		}

		log.info("getPublication: publication_id=" + id);
		try {
			String fullquery = "rdf.publication.id : \"" + id + "\"";
			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
			return result;
		} catch (QueryTranslationException e) {
			log.error("getPublication:QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error("getPublication:QueryMetadataException", e);
		}
		return "{ }";
	}

	@Path("/{keyword:(/search/[^/]+?)?}{year:(/year/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getPublicationsFromKeyword(@PathParam("keyword") String keyword, @PathParam("year") String year, @PathParam("page") String page, @PathParam("items") String items) {
		String query = "collection:publications";
		
		if (keyword != null && !keyword.equalsIgnoreCase("")) {
			 query += " AND contents:" + keyword.split("/")[2];
		}
		
		if (year != null && !year.equalsIgnoreCase("")) {
			 query += " AND rdf.publication.year:" + year.split("/")[2];
		}
		
		return doPagingQuery(query, page, items, "getPublicationsFromKeyword",TranslateResultsformat.RFML);
	}

	@Path("/search")
	@GET
	@Produces("application/json")
	public String getPublicationsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
		query = "collection:publications AND contents:" + query;
		return doQuery(query, page, items, "getPublicationsFromQuery",TranslateResultsformat.RFML);
	}

	@Path("/author/{author_id}{year:(/year/[^/]+?)?}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getPublicationsAuthor(@PathParam("author_id") String id, @PathParam("year") String year, @PathParam("keyword") String keyword, @PathParam("page") String page,
			@PathParam("items") String items) {
		if (id.equalsIgnoreCase("")) {
			log.error("getAuthorPublications: Not a valid author identifier");
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
				fullquery += "rdf.publication.author.person.id : \"" + sameId + "\"";
			}
			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
		}else {			
			fullquery = "rdf.publication.author.person.id : \"" + id + "\" ";
		}
		
		if (!year.equalsIgnoreCase("")) {
			queries.add("rdf.publication.year:" + year.split("/")[2]);
		}
		if (!keyword.equalsIgnoreCase("")) {
			queries.add("contents:" + keyword.split("/")[2]);
		}

		for (int i = 0; i < queries.size(); i++) {
			fullquery = fullquery.concat(" AND " + queries.get(i));

		}

		return doPagingQuery(fullquery, page, items, "getAuthorPublications",TranslateResultsformat.RFML);
	}

	@Path("/organization/{organization_id1}{organization_id2:(/organization/[^/]+?)?}{year:(/year/[^/]+?)?}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getPublicationsOrganization(@PathParam("organization_id1") String id1, @PathParam("organization_id2") String id2, @PathParam("year") String year, @PathParam("keyword") String keyword, @PathParam("page") String page,
			@PathParam("items") String items) {
		if (id1.equalsIgnoreCase("")) {
			log.error("getPublicationsOrganization: Not a valid organization identifier");
			return "{ }";
		}
		Vector<String> queries = new Vector<String>();
//		String fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id + "\" ";

		String fullquery = "";
		if(idGroups.containsKey(id1)) {
			for (String sameId : idGroups.get(id1)) {
				if(!fullquery.equalsIgnoreCase("")) {
					fullquery += " OR ";
				}else {
					 fullquery += "(";
				}
				fullquery += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
			}
			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
		}else {			
			fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id1 + "\" ";
		}
		
		if (!id2.equalsIgnoreCase("")) {
			id2 = id2.split("/")[2];
			String query = "";
			if(idGroups.containsKey(id2)) {
				for (String sameId : idGroups.get(id2)) {
					if(!query.equalsIgnoreCase("")) {
						query += " OR ";
					}else {
						query += "(";
					}
					query += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
				}
				if(!query.equalsIgnoreCase("")) query += ") ";
			}else {			
				query = "rdf.publication.author.person.affiliation.organization.id : \"" + id2 + "\" ";
			}
		}
		
		if (!year.equalsIgnoreCase("")) {
			queries.add("rdf.publication.year:" + year.split("/")[2]);
		}
		if (!keyword.equalsIgnoreCase("")) {
			queries.add("contents:" + keyword.split("/")[2]);
		}

		for (int i = 0; i < queries.size(); i++) {
			fullquery = fullquery.concat(" AND " + queries.get(i));
		}

		log.info("getPublicationsOrganization: organization_id=" + id1);
		return doPagingQuery(fullquery, page, items, "getPublicationsOrganization",TranslateResultsformat.RFML);
	}

}

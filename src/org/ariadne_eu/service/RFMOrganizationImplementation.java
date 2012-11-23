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

@Path("/organizations")
public class RFMOrganizationImplementation extends RFMImplementation{

	private static Logger log = Logger.getLogger(RFMOrganizationImplementation.class);
	@Path("/{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getOrganizations(@PathParam("page") String page, @PathParam("items") String items) {
		String query = "collection:organizations";
		return doPagingQuery(query, page, items, "getOrganizations",TranslateResultsformat.RFML);
	}

	@Path("/{organization_id}")
	@GET
	@Produces("application/json")
	public String getOrganization(@PathParam("organization_id") String id) {

		if (id.equalsIgnoreCase("")) {
			log.error("getOrganization: Not a valid organization identifier");
			return "{ }";
		}

		log.info("getOrganization: organization_id=" + id);
		try {
			String fullquery = "rdf.organization.id : \"" + id + "\"";
			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
			return result;
		} catch (QueryTranslationException e) {
			log.error("getOrganization:QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error("getOrganization:QueryMetadataException", e);
		}
		return "{ }";
	}

	@Path("/search/{keyword}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getOrganizationsFromKeyword(@PathParam("keyword") String keyword, @PathParam("page") String page, @PathParam("items") String items) {
		String query = "collection:organizations AND contents:" + keyword;
		return doPagingQuery(query, page, items, "getOrganizationsFromQuery",TranslateResultsformat.RFML);
	}
	
	@Path("/co-author-organizations/{organization_id}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
	@GET
	@Produces("application/json")
	public String getCoAuthorOrganizationsByOrganization(@PathParam("organization_id") String id, @PathParam("page") String page, @PathParam("items") String items) {
		if (id.equalsIgnoreCase("")) {
			log.error("getCoAuthorOrganizationsByOrganization: Not a valid organization identifier");
			return "{ }";
		}

		String fullquery = "";
		if(idGroups.containsKey(id)) {
			for (String sameId : idGroups.get(id)) {
				if(!fullquery.equalsIgnoreCase("")) {
					fullquery += " OR ";
				}else {
					 fullquery += "(";
				}
				fullquery += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
			}
			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
		}else {			
			fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id + "\" ";
		}
		return doPagingQuery(fullquery, page, items, "getCoAuthorOrganizationsByOrganization",TranslateResultsformat.RFMCOAFFILL);
	}

	@Path("/search")
	@GET
	@Produces("application/json")
	public String getOrganizationsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
		query = "collection:organizations AND contents:" + query;
		return doQuery(query, page, items, "getOrganizationsFromQuery",TranslateResultsformat.RFML);
	}
}

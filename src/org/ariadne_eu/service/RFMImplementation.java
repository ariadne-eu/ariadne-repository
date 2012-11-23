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

public class RFMImplementation {

	//	private final static HashMap<String,Integer> ids = new HashMap<String, Integer>();
	//	private final static HashMap<Integer,Vector<String>> idGroups = new HashMap<Integer, Vector<String>>();
	protected final static HashMap<String,Vector<String>> idGroups = new HashMap<String, Vector<String>>();

	private static Logger log = Logger.getLogger(RFMImplementation.class);

	static {
		getIds();
	}
	
	public static void getIds() {
		// TODO Auto-generated method stub
		try {
			String file = "ids.txt";
			String path = System.getProperty("basePath");
			Vector<String[]> allIds = CsvReader.readFile(false, path + File.separator + "data" + File.separator + file);
			//			int i = 0;
			for (String[] strings : allIds) {
				List l = Arrays.asList(strings);
				Vector<String> idVector = new Vector(l);
				//			    idGroups.put(i, idVector);
				for (String string : idVector) {
					//					ids.put(string, i);
					idGroups.put(string, idVector);
				}
				//				i++;
			}
			//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			//			String str = reader.readLine();
			//			while (str != null) {
			//				str.split("");
			//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//
//	@Path("/authors{set:(/set/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getAuthors(@PathParam("set") String set,@PathParam("page") String page, @PathParam("items") String items) {
//		String query = "collection:persons";
//		
//		return doPagingSetQuery(set, query, page, items, "getAuthors",TranslateResultsformat.RFML);
//	}
//
//
//	@Path("/authors/{author_id}")
//	@GET
//	@Produces("application/json")
//	public String getAuthor(@PathParam("author_id") String id) {
//
//		if (id.equalsIgnoreCase("")) {
//			log.error("getAuthor: Not a valid author identifier");
//			return "{ }";
//		}
//
//		log.info("getAuthor: author_id=" + id);
//		try {
//			String fullquery = "";
//			if(idGroups.containsKey(id)) {
//				for (String sameId : idGroups.get(id)) {
//					if(!fullquery.equalsIgnoreCase("")) fullquery += " OR ";
//					fullquery += "rdf.person.id : \"" + sameId + "\"";
//				}
//			}else {			
//				fullquery = "rdf.person.id : \"" + id + "\"";
//			}
//
//			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
//			return result;
//		} catch (QueryTranslationException e) {
//			log.error("getAuthor:QueryTranslationException", e);
//		} catch (QueryMetadataException e) {
//			log.error("getAuthor:QueryMetadataException", e);
//		}
//		return "{ }";
//	}
//
//	@Path("/authors/search/{keyword}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getAuthorsFromKeyword(@PathParam("keyword") String keyword, @PathParam("page") String page, @PathParam("items") String items) {
//		String thePage = null;
//		if (page != null && !page.equalsIgnoreCase("")) {
//			thePage = page.split("/")[2];
//		}
//		String theItems = null;
//		if (items != null && !items.equalsIgnoreCase("")) {
//			theItems = items.split("/")[2];
//		}
//		return getAuthorsFromQuery(keyword, thePage, theItems);
//	}
//
//	@Path("/authors/search")
//	@GET
//	@Produces("application/json")
//	public String getAuthorsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
//		query = "collection:persons AND contents:" + query;
//		return doQuery(query, page, items, "getAuthorsFromQuery",TranslateResultsformat.RFML);
//	}
//
//	@Path("/authors/organization/{organization_id}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getAuthorsOrganization(@PathParam("organization_id") String id, @PathParam("keyword") String keyword, @PathParam("page") String page,
//			@PathParam("items") String items) {
//		if (id.equalsIgnoreCase("")) {
//			log.error("getAuthorsOrganization: Not a valid organization identifier");
//			return "{ }";
//		}
//		Vector<String> queries = new Vector<String>();
//
//		String fullquery = "";
//		if(idGroups.containsKey(id)) {
//			for (String sameId : idGroups.get(id)) {
//				if(!fullquery.equalsIgnoreCase("")) {
//					fullquery += " OR ";
//				}else {
//					 fullquery += "(";
//				}
//				fullquery += "rdf.person.affiliation.organization.id : \"" + sameId + "\"";
//			}
//			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
//		}else {			
//			fullquery = "rdf.person.affiliation.organization.id : \"" + id + "\" ";
//		}
//
//
//		if (!keyword.equalsIgnoreCase("")) {
//			queries.add("contents:" + keyword.split("/")[2]);
//		}
//
//		for (int i = 0; i < queries.size(); i++) {
//			fullquery = fullquery.concat(" AND " + queries.get(i));
//		}
//
//		log.info("getAuthorsOrganization: organization_id=" + id);
//		return doPagingQuery(fullquery, page, items, "getAuthorsOrganization",TranslateResultsformat.RFML);
//	}
//
//	/*@Path("/publications{year:(/year/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getPublications(@PathParam("year") String year, @QueryParam("page") String page, @QueryParam("items") String items) {
//		String query = "collection:publications";
//		if (!year.equalsIgnoreCase("")) {
//			query = query + " AND rdf.publication.year:" + year.split("/")[2];
//		} 
//		return doQuery(query, page, items, "getPublications",TranslateResultsformat.RFML);
//	}*/
//
//	/*@Path("/publications/{doi_prefix}/{doi_handle}")
//	@GET
//	@Produces("application/json")
//	public String getPublicationFromDOI(@PathParam("doi_prefix") String prefix, @PathParam("doi_handle") String handle) {
//		String id = prefix + "/" + handle;
//		if (id.equalsIgnoreCase("")) {
//			log.error("getPublicationFromDOI: Not a valid DOI identifier");
//			return "{ }";
//		}
//		log.info("getPublicationFromDOI: publication_id=" + id);
//		String fullquery = "rdf.publication.id : \"" + id + "\"";
//		return doQuery(fullquery, "1", "1", "getPublicationFromDOI",TranslateResultsformat.RFME);
//	}*/
//
////	@Path("/publications{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
////	@GET
////	@Produces("application/json")
////	public String getPublications(@PathParam("page") String page, @PathParam("items") String items) {
////		String query = "collection:publications";
////		return doPagingQuery(query, page, items, "getPublications",TranslateResultsformat.RFML);
////	}
//
//	@Path("/publications/{publication_id}")
//	@GET
//	@Produces("application/json")
//	public String getPublication(@PathParam("publication_id") String id) {
//
//		if (id.equalsIgnoreCase("")) {
//			log.error("getPublication: Not a valid publication identifier");
//			return "{ }";
//		}
//
//		log.info("getPublication: publication_id=" + id);
//		try {
//			String fullquery = "rdf.publication.id : \"" + id + "\"";
//			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
//			return result;
//		} catch (QueryTranslationException e) {
//			log.error("getPublication:QueryTranslationException", e);
//		} catch (QueryMetadataException e) {
//			log.error("getPublication:QueryMetadataException", e);
//		}
//		return "{ }";
//	}
//
//	@Path("/publications{keyword:(/search/[^/]+?)?}{year:(/year/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getPublicationsFromKeyword(@PathParam("keyword") String keyword, @PathParam("year") String year, @PathParam("page") String page, @PathParam("items") String items) {
//		String query = "collection:publications";
//		
//		if (keyword != null && !keyword.equalsIgnoreCase("")) {
//			 query += " AND contents:" + keyword.split("/")[2];
//		}
//		
//		if (year != null && !year.equalsIgnoreCase("")) {
//			 query += " AND rdf.publication.year:" + year.split("/")[2];
//		}
//		
//		return doPagingQuery(query, page, items, "getPublicationsFromKeyword",TranslateResultsformat.RFML);
//	}
//
//	@Path("/publications/search")
//	@GET
//	@Produces("application/json")
//	public String getPublicationsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
//		query = "collection:publications AND contents:" + query;
//		return doQuery(query, page, items, "getPublicationsFromQuery",TranslateResultsformat.RFML);
//	}
//
//	@Path("/publications/author/{author_id}{year:(/year/[^/]+?)?}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getPublicationsAuthor(@PathParam("author_id") String id, @PathParam("year") String year, @PathParam("keyword") String keyword, @PathParam("page") String page,
//			@PathParam("items") String items) {
//		if (id.equalsIgnoreCase("")) {
//			log.error("getAuthorPublications: Not a valid author identifier");
//			return "{ }";
//		}
//		Vector<String> queries = new Vector<String>();
//
//		String fullquery = "";
//		if(idGroups.containsKey(id)) {
//			for (String sameId : idGroups.get(id)) {
//				if(!fullquery.equalsIgnoreCase("")) {
//					fullquery += " OR ";
//				}else {
//					 fullquery += "(";
//				}
//				fullquery += "rdf.publication.author.person.id : \"" + sameId + "\"";
//			}
//			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
//		}else {			
//			fullquery = "rdf.publication.author.person.id : \"" + id + "\" ";
//		}
//		
//		if (!year.equalsIgnoreCase("")) {
//			queries.add("rdf.publication.year:" + year.split("/")[2]);
//		}
//		if (!keyword.equalsIgnoreCase("")) {
//			queries.add("contents:" + keyword.split("/")[2]);
//		}
//
//		for (int i = 0; i < queries.size(); i++) {
//			fullquery = fullquery.concat(" AND " + queries.get(i));
//
//		}
//
//		return doPagingQuery(fullquery, page, items, "getAuthorPublications",TranslateResultsformat.RFML);
//	}
//
//	@Path("/publications/organization/{organization_id1}{organization_id2:(/organization/[^/]+?)?}{year:(/year/[^/]+?)?}{keyword:(/search/[^/]+?)?}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getPublicationsOrganization(@PathParam("organization_id1") String id1, @PathParam("organization_id2") String id2, @PathParam("year") String year, @PathParam("keyword") String keyword, @PathParam("page") String page,
//			@PathParam("items") String items) {
//		if (id1.equalsIgnoreCase("")) {
//			log.error("getPublicationsOrganization: Not a valid organization identifier");
//			return "{ }";
//		}
//		Vector<String> queries = new Vector<String>();
////		String fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id + "\" ";
//
//		String fullquery = "";
//		if(idGroups.containsKey(id1)) {
//			for (String sameId : idGroups.get(id1)) {
//				if(!fullquery.equalsIgnoreCase("")) {
//					fullquery += " OR ";
//				}else {
//					 fullquery += "(";
//				}
//				fullquery += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
//			}
//			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
//		}else {			
//			fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id1 + "\" ";
//		}
//		
//		if (!id2.equalsIgnoreCase("")) {
//			id2 = id2.split("/")[2];
//			String query = "";
//			if(idGroups.containsKey(id2)) {
//				for (String sameId : idGroups.get(id2)) {
//					if(!query.equalsIgnoreCase("")) {
//						query += " OR ";
//					}else {
//						query += "(";
//					}
//					query += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
//				}
//				if(!query.equalsIgnoreCase("")) query += ") ";
//			}else {			
//				query = "rdf.publication.author.person.affiliation.organization.id : \"" + id2 + "\" ";
//			}
//		}
//		
//		if (!year.equalsIgnoreCase("")) {
//			queries.add("rdf.publication.year:" + year.split("/")[2]);
//		}
//		if (!keyword.equalsIgnoreCase("")) {
//			queries.add("contents:" + keyword.split("/")[2]);
//		}
//
//		for (int i = 0; i < queries.size(); i++) {
//			fullquery = fullquery.concat(" AND " + queries.get(i));
//		}
//
//		log.info("getPublicationsOrganization: organization_id=" + id1);
//		return doPagingQuery(fullquery, page, items, "getPublicationsOrganization",TranslateResultsformat.RFML);
//	}
//
//	@Path("/organizations{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getOrganizations(@PathParam("page") String page, @PathParam("items") String items) {
//		String query = "collection:organizations";
//		return doPagingQuery(query, page, items, "getOrganizations",TranslateResultsformat.RFML);
//	}
//
//	@Path("/organizations/{organization_id}")
//	@GET
//	@Produces("application/json")
//	public String getOrganization(@PathParam("organization_id") String id) {
//
//		if (id.equalsIgnoreCase("")) {
//			log.error("getOrganization: Not a valid organization identifier");
//			return "{ }";
//		}
//
//		log.info("getOrganization: organization_id=" + id);
//		try {
//			String fullquery = "rdf.organization.id : \"" + id + "\"";
//			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(fullquery, 1, 1, TranslateResultsformat.RFME);
//			return result;
//		} catch (QueryTranslationException e) {
//			log.error("getOrganization:QueryTranslationException", e);
//		} catch (QueryMetadataException e) {
//			log.error("getOrganization:QueryMetadataException", e);
//		}
//		return "{ }";
//	}
//
//	@Path("/organizations/search/{keyword}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getOrganizationsFromKeyword(@PathParam("keyword") String keyword, @PathParam("page") String page, @PathParam("items") String items) {
//		String query = "collection:organizations AND contents:" + keyword;
//		return doPagingQuery(query, page, items, "getOrganizationsFromQuery",TranslateResultsformat.RFML);
//	}
//	
//	@Path("/organizations/co-author-organizations/{organization_id}{page:(/page/[^/]+?)?}{items:(/items/[^/]+?)?}")
//	@GET
//	@Produces("application/json")
//	public String getCoAuthorOrganizationsByOrganization(@PathParam("organization_id") String id, @PathParam("page") String page, @PathParam("items") String items) {
//		if (id.equalsIgnoreCase("")) {
//			log.error("getCoAuthorOrganizationsByOrganization: Not a valid organization identifier");
//			return "{ }";
//		}
//		Vector<String> queries = new Vector<String>();
////		String fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id + "\" ";
//
//		String fullquery = "";
//		if(idGroups.containsKey(id)) {
//			for (String sameId : idGroups.get(id)) {
//				if(!fullquery.equalsIgnoreCase("")) {
//					fullquery += " OR ";
//				}else {
//					 fullquery += "(";
//				}
//				fullquery += "rdf.publication.author.person.affiliation.organization.id : \"" + sameId + "\"";
//			}
//			if(!fullquery.equalsIgnoreCase("")) fullquery += ") ";
//		}else {			
//			fullquery = "rdf.publication.author.person.affiliation.organization.id : \"" + id + "\" ";
//		}
//
//		log.info("getPublicationsOrganization: organization_id=" + id);
//		return doPagingQuery(fullquery, page, items, "getCoAuthorOrganizationsByOrganization",TranslateResultsformat.RFMCOAFFILL);
//	}
//
//	@Path("/organizations/search")
//	@GET
//	@Produces("application/json")
//	public String getOrganizationsFromQuery(@QueryParam("q") String query, @QueryParam("page") String page, @QueryParam("items") String items) {
//		query = "collection:organizations AND contents:" + query;
//		return doQuery(query, page, items, "getOrganizationsFromQuery",TranslateResultsformat.RFML);
//	}

	protected String doQuery(String query, String page, String items, String logPrefix, int resultsFormat) {
		try {

			if (query.equalsIgnoreCase("")) {
				log.error(logPrefix + ": Not a valid query");
				return "{ }";
			}

			int startResult = 1;
			int nbResults = 10;

			if (items != null) {
				try {
					nbResults = Integer.parseInt(items);
					if (nbResults < 1)
						throw new Exception();
				} catch (Exception e) {
					log.error(logPrefix + ": Invalid Results Set items");
					return "{ }";
				}
			}

			if (page != null) {
				try {
					startResult = (Integer.parseInt(page) * nbResults) - nbResults + 1;
					if (startResult < 1)
						throw new Exception("Start Result was : " + startResult);
				} catch (Exception e) {
					log.error(logPrefix + ": Invalid Start Result. (" + e.getMessage() + ")");
					e.printStackTrace();
					return "{ }";
				}
			}

			log.info(logPrefix + ": query=" + query + ",startResult=" + startResult + ",nbResults=" + nbResults + ",resultsFormat=" + resultsFormat);

			String result = QueryMetadataFactory.getQueryImpl(TranslateLanguage.LUCENE).query(query, startResult, nbResults, resultsFormat );
			return result;
		} catch (QueryTranslationException e) {
			log.error(logPrefix + ": QueryTranslationException", e);
		} catch (QueryMetadataException e) {
			log.error(logPrefix + ": QueryMetadataException", e);
		}
		return "{ }";
	}	
	

	protected String doPagingSetQuery(String set, String query, String page, String items, String logPrefix, int resultsFormat) {
		String theSet = null;
		if (set != null && !set.equalsIgnoreCase("")) {
			theSet = set.split("/")[2];
		}
		if(theSet != null) {
			query += " AND collection:" + theSet;
		}
		
		return doPagingQuery(query, page, items, logPrefix,resultsFormat);
	}
	

	protected String doPagingQuery(String query, String page, String items, String logPrefix, int resultsFormat) {
		String thePage = null;
		if (page != null && !page.equalsIgnoreCase("")) {
			thePage = page.split("/")[2];
		}
		String theItems = null;
		if (items != null && !items.equalsIgnoreCase("")) {
			theItems = items.split("/")[2];
		}
		
		return doQuery(query, thePage, theItems, logPrefix,resultsFormat);
	}
	
}

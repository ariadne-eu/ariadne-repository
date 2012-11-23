/**
 * 
 */
package org.ariadne_eu.metadata.query.language;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.ariadne_eu.metadata.query.lucene.query.LuceneQLMaker;
import org.ariadne_eu.metadata.query.lucene.query.QueryMakerException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author gonzalo
 * 
 */
public class Json_LuceneHandler extends Translate {

	public String[][] searchTerms; // Conjunction of disjunction
	public String[] exclusionTerms; // Disjunction of terms to be excluded
	public String[] rankingTerms; // These are terms that should be considered
									// in the ranking; e.g. "cny=BE"
	public String sortKey; // By which key the results should be sorted
	public String[] facets = new String[0]; // Which facets to count
	public int resultListStart = 1;
	public int resultListSize = 12;
	public int idListStart = 1;
	public int idListSize = 12;
	public int maxCntFacets = 0; // Maximum number of results that will be
									// counted for the facets

	private static Logger log = Logger.getLogger(Plql_LuceneHandler.class);

	public Json_LuceneHandler(int startQueryLanguage, int endQueryLanguage) {
		super(startQueryLanguage, endQueryLanguage);
	}

	public String translateToQuery(String query, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
		try {

			return LuceneQLMaker.createQuery(LuceneQLMaker.JsonQL, query);
		} catch (QueryMakerException e) {
			log.error(e);
			return null;
		}
	}

	public String translateToCount(String query) throws QueryTranslationException {
		return translateToQuery(query, -1, -1, -1);
	}

	private void parseJson(JSONObject jo) {
		try {	
			String expression = "";
			String language = "";
			ArrayList<String[]> stList = new ArrayList<String[]>();
			JSONArray clause = new JSONArray();
			if (jo.has("clause"))
				clause = jo.getJSONArray("clause");
			for (int i = 0; i < clause.length(); i++) {
				JSONObject thisClause = clause.getJSONObject(i);
				if (thisClause.has("language"))
					language = thisClause.getString("language").trim();
				if (thisClause.has("expression"))
					expression = thisClause.getString("expression").trim();
				;
				if (language.equalsIgnoreCase("vsql")) {
					List<String> subject = parseVSQL(expression);
					for (int j = 0; j < subject.size(); j++) {
						String[] st = new String[1];
						st[0] = "subject:" + subject.get(j);
						stList.add(st);
					}
				} else if (language.equalsIgnoreCase("facet")) {
					List<String> parsedFacets = parseFacet(expression);
					String[] facetString = new String[parsedFacets.size()];
					parsedFacets.toArray(facetString);
					stList.add(facetString);
				}
			}
			searchTerms = new String[stList.size()][];
			for (int i = 0; i < stList.size(); i++)
				searchTerms[i] = stList.get(i);
			System.out.println(toString());

			// Set facets to report the numbers on
			if (jo.has("facets")) {
				JSONArray myFacets = jo.getJSONArray("facets");
				facets = new String[myFacets.length()];
				for (int i = 0; i < myFacets.length(); i++) {
					facets[i] = myFacets.getString(i);
				}
			}

			if (jo.has("resultListStart"))
				resultListStart = jo.getInt("resultListStart");
			if (jo.has("resultListSize"))
				resultListSize = jo.getInt("resultListSize");
			// Set IDsFrom and IDsTo to default values
			idListStart = resultListStart;
			idListSize = resultListSize;
			if (jo.has("idListStart"))
				idListStart = jo.getInt("idListStart");
			if (jo.has("idListSize"))
				idListSize = jo.getInt("idListSize");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private List<String> parseVSQL(String content) {
		StringTokenizer st = new StringTokenizer(content, " ");
		List<String> result = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			result.add(st.nextToken().trim().toLowerCase());
		}
		return result;
	}

	private List<String> parseFacet(String content) {
		StringTokenizer st = new StringTokenizer(content.substring(1, content.length() - 1), ",");
		List<String> result = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String f = st.nextToken().trim().toLowerCase();
			result.add(f.substring(1, f.length() - 1).trim());
		}
		return result;
	}

	public static void main(String[] args) throws JSONException {
		String jsonQuery = "{\"toParse\":\"building\",\"all\":\"\",\"any\":[\"any1\",\"any2\",\"any3\"],\"none\":[],\"uiLanguage\":\"en\",\"preferredLanguages\":[],\"resultFormat\":\"newPage\",\"facets\":[\"language\",\"format\",\"context\",\"provider\",\"lrt\"],\"resultsFrom\":7,\"resultsTo\":9,\"idsFrom\":7,\"idsTo\":9,\"resultsOffset\":1}";
		System.out.println(jsonQuery);
		JSONObject jo108 = new JSONObject(jsonQuery);
		Json_LuceneHandler jlh = new Json_LuceneHandler(0, 0);
		jlh.parseJson(jo108);
	}

}

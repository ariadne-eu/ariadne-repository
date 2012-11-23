package org.ariadne_eu.oai.server.lucene.catalog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sourceforge.minor.lucene.core.searcher.ReaderManagement;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;
import org.oclc.oai.server.catalog.AbstractCatalog;
import org.oclc.oai.server.verb.BadArgumentException;
import org.oclc.oai.server.verb.BadResumptionTokenException;
import org.oclc.oai.server.verb.CannotDisseminateFormatException;
import org.oclc.oai.server.verb.IdDoesNotExistException;
import org.oclc.oai.server.verb.NoItemsMatchException;
import org.oclc.oai.server.verb.NoMetadataFormatsException;
import org.oclc.oai.server.verb.NoSetHierarchyException;
import org.oclc.oai.server.verb.OAIInternalServerError;
import org.oclc.oai.util.OAIUtil;

public class LuceneLomCatalog extends AbstractCatalog {

	private static Logger log = Logger.getLogger(QueryMetadataLuceneImpl.class);

	/**
	 * pending resumption tokens
	 */
	private HashMap resumptionResults = new HashMap();
	private static HashMap<String, String> sets = new HashMap<String, String>();

	//	private Searcher searcher;
	private DocumentAnalyzer docAnalyzer;

	private static int maxListSize;
	private static String lucenePath;
	private static String dateField;
	private static String identifierField;
	private static File indexDir;

	private static IndexReader reader;

	public LuceneLomCatalog(Properties properties) {
		String maxListSize = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MAXLSTSIZE);
		if (maxListSize == null) {
			throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_MAXLSTSIZE + " is missing from the properties file");
		} else {
			LuceneLomCatalog.maxListSize = Integer.parseInt(maxListSize);
		}
		String lucenePath = properties.getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR);
		if (lucenePath == null) {
			throw new IllegalArgumentException(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR + " is missing from the properties file");
		} else {
			LuceneLomCatalog.lucenePath = lucenePath;
		}
		String dateField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_DATEFIELD);
		if (lucenePath == null) {
			throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_DATEFIELD + " is missing from the properties file");
		} else {
			LuceneLomCatalog.dateField = dateField;
		}
		String identifierField = properties.getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_IDFIELD);
		if (identifierField == null) {
			throw new IllegalArgumentException(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_IDFIELD + " is missing from the properties file");
		} else {
			LuceneLomCatalog.identifierField = identifierField;
		}
		indexDir = new File(LuceneLomCatalog.lucenePath);
		docAnalyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
		try {

			Hashtable setKeys = PropertiesManager.getInstance().getPropertyStartingWith(RepositoryConstants.getInstance().OAICAT_SETS);
			String[] keys = (String[]) setKeys.keySet().toArray(new String[0]);
			String reposIdentifier = "";
			for(String key : keys) {
				String setSpec = key.replace(RepositoryConstants.getInstance().OAICAT_SETS + ".", "").replace("."+RepositoryConstants.getInstance().OAICAT_SETS_ID,"");
				reposIdentifier = PropertiesManager.getInstance().getProperty(key);
				sets.put(setSpec, reposIdentifier);
			}
		} catch (Exception e) {
			//NOOP
		}

	}

	private void loadIndexReader(File indexDir) {
		try {
			reader = ReaderManagement.getInstance().getReader(indexDir);
		} catch (IOException e) {
			log.error("Could not get a Reader instance",e);
		} catch (Exception e) {
			log.error("Could not get a Reader instance",e);
		}
	}

	private void closeIndexReader(IndexReader reader) {
		try {
			ReaderManagement.getInstance().unRegister(indexDir, reader);
		} catch (Exception e) {
			log.error("Could not remove a Reader instance",e);
		}
	}

	public Map listSets() throws NoSetHierarchyException, OAIInternalServerError {
		Hashtable setKeys = PropertiesManager.getInstance().getPropertyStartingWith(RepositoryConstants.getInstance().OAICAT_SETS);
		String[] keys = (String[]) setKeys.keySet().toArray(new String[0]);
		if(keys.length == 0) {
			throw new NoSetHierarchyException();
		}
		else {
			purge(); // clean out old resumptionTokens
			Map listSetsMap = new HashMap();
			ArrayList sets = new ArrayList();

			for(String key : keys) {
				String setSpec = key.replace(RepositoryConstants.getInstance().OAICAT_SETS + ".", "").replace("."+RepositoryConstants.getInstance().OAICAT_SETS_ID,"");
				sets.add(getSetXML(PropertiesManager.getInstance().getProperty(key),setSpec));
			}

			listSetsMap.put("sets", sets.iterator());
			return listSetsMap;  
		}
	}

	public Map listSets(String resumptionToken) throws BadResumptionTokenException, OAIInternalServerError {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Extract &lt;set&gt; XML string from setItem object
	 *
	 * @param setItem individual set instance in native format
	 * @return an XML String containing the XML &lt;set&gt; content
	 */
	public String getSetXML(String key, String setSpec)
	throws IllegalArgumentException {
		String setName = "Metadata originating from " + setSpec;
		String setDescription = "RepositoryIdentifier is " + key;

		StringBuffer sb = new StringBuffer();
		sb.append("<set>");
		sb.append("<setSpec>");
		sb.append(OAIUtil.xmlEncode(setSpec));
		sb.append("</setSpec>");
		sb.append("<setName>");
		sb.append(OAIUtil.xmlEncode(setName));
		sb.append("</setName>");
		if (setDescription != null) {
			sb.append("<setDescription>");
			sb.append(OAIUtil.xmlEncode(setDescription));
			sb.append("</setDescription>");
		}
		sb.append("</set>");
		return sb.toString();
	} 

	public Vector getSchemaLocations(String identifier) throws IdDoesNotExistException, NoMetadataFormatsException, OAIInternalServerError {
		Object nativeItem = getIndexRecord(identifier);
		/*
		 * Let your recordFactory decide which schemaLocations
		 * (i.e. metadataFormats) it can produce from the record.
		 * Doing so will preserve the separation of database access
		 * (which happens here) from the record content interpretation
		 * (which is the responsibility of the RecordFactory implementation).
		 */
		if (nativeItem == null) {
			throw new IdDoesNotExistException(identifier);
		} else {
			return getRecordFactory().getSchemaLocations(nativeItem);
		}
	}

	private Object getIndexRecord(String identifier) {
		loadIndexReader(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		//		SingletonIndexSearcher sis = SingletonIndexSearcher.getSingletonIndexSearcher(reader);
		String localIdentifier = getRecordFactory().fromOAIIdentifier(identifier);


		QueryParser parser = new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,"field", docAnalyzer.getAnalyzer());
		Query query = null;
		TermQuery termQuery = new TermQuery(new Term(identifierField, localIdentifier));
		try {
			query = parser.parse(termQuery.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Hits hits = null;
		try {
			hits = searcher.search(query);
			//			hits = SingletonIndexSearcher.search(query);
		} catch (IOException e) {
			e.printStackTrace();  
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document resultDoc = null;
		try {
			resultDoc = hits.doc(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeIndexReader(reader);
		return resultDoc;
	}

	@SuppressWarnings("unchecked")
	public Map listIdentifiers(String from, String until, String set, String metadataPrefix) throws BadArgumentException, CannotDisseminateFormatException, NoItemsMatchException, NoSetHierarchyException, OAIInternalServerError {
		loadIndexReader(new File(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		//		SingletonIndexSearcher sis = SingletonIndexSearcher.getSingletonIndexSearcher(reader);
		purge(); // clean out old resumptionTokens
		Map listIdentifiersMap = new HashMap();
		ArrayList headers = new ArrayList();
		ArrayList identifiers = new ArrayList();

		QueryParser parser = new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,dateField, docAnalyzer.getAnalyzer());
		Query query = null;
		String fromDate = from.replaceAll("-", "");
		fromDate = fromDate.replaceAll(":", "");
		fromDate = fromDate.replaceAll("T", "");
		fromDate = fromDate.replaceAll("Z", "0");
		String untilDate = until.replaceAll("-", "");
		untilDate = untilDate.replaceAll(":", "");
		untilDate = untilDate.replaceAll("T", "");
		untilDate = untilDate.replaceAll("Z", "0");
		Term termFrom = new Term(dateField, fromDate);
		Term termUntil = new Term(dateField, untilDate);
		RangeQuery rangeQuery = new RangeQuery(termFrom,termUntil,true);
		try {
			query = parser.parse(rangeQuery.toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Hits hits = null;
		try {
			hits = searcher.search(query);
			//			hits = SingletonIndexSearcher.search(query);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(hits.length() == 0) throw new NoItemsMatchException();

		/* Get some records from your database */
		int count = 0;
		/* load the records ArrayList */
		Object[] nativeItem = new Object[hits.length()];
		for (int i = 0; i < hits.length(); i++) {
			try {
				nativeItem[i] = hits.doc(i);
			} catch (IOException e) {

				e.printStackTrace();

			}
		}	
		for (count=0; count < maxListSize && count < hits.length(); ++count) {
			//record = constructRecord(nativeItem[count], metadataPrefix);
			//records.add(record);
			/* Use the RecordFactory to extract header/identifier pairs for each item */
			String[] header = getRecordFactory().createHeader(nativeItem[count]);
			headers.add(header[0]);
			identifiers.add(header[1]);
		}

		/* decide if you're done */
		if (count < hits.length()) {
			String resumptionId = getResumptionId();

			/*****************************************************************
			 * Store an object appropriate for your database API in the
			 * resumptionResults Map in place of nativeItems. This object
			 * should probably encapsulate the information necessary to
			 * perform the next resumption of ListIdentifiers. It might even
			 * be possible to encode everything you need in the
			 * resumptionToken, in which case you won't need the
			 * resumptionResults Map. Here, I've done a silly combination
			 * of the two. Stateless resumptionTokens have some advantages.
			 *****************************************************************/
			resumptionResults.put(resumptionId, nativeItem);

			/*****************************************************************
			 * Construct the resumptionToken String however you see fit.
			 *****************************************************************/
			StringBuffer resumptionTokenSb = new StringBuffer();
			resumptionTokenSb.append(resumptionId);
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(Integer.toString(count));
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(metadataPrefix);

			listIdentifiersMap.put("resumptionMap", getResumptionMap(resumptionTokenSb.toString(),
					hits.length(),
					0));
		}

		listIdentifiersMap.put("headers", headers.iterator());
		listIdentifiersMap.put("identifiers", identifiers.iterator());
		closeIndexReader(reader);
		return listIdentifiersMap;
	}

	private String constructRecord(Object nativeItem, String metadataPrefix)
	throws CannotDisseminateFormatException {
		String schemaURL = null;

		if (metadataPrefix != null) {
			if ((schemaURL = getCrosswalks().getSchemaURL(metadataPrefix)) == null)
				throw new CannotDisseminateFormatException(metadataPrefix);
		}
		return getRecordFactory().create(nativeItem, schemaURL, metadataPrefix);
	}

	/**
	 * Use the current date as the basis for the resumptiontoken
	 *
	 * @return a String version of the current time
	 */
	private synchronized static String getResumptionId() {
		Date now = new Date();
		return Long.toString(now.getTime());
	}

	/**
	 * Purge tokens that are older than the configured time-to-live.
	 */
	private void purge() {
		ArrayList old = new ArrayList();
		Date now = new Date();
		Iterator keySet = resumptionResults.keySet().iterator();
		while (keySet.hasNext()) {
			String key = (String)keySet.next();
			Date then = new Date(Long.parseLong(key) + getMillisecondsToLive());
			if (now.after(then)) {
				old.add(key);
			}
		}
		Iterator iterator = old.iterator();
		while (iterator.hasNext()) {
			String key = (String)iterator.next();
			resumptionResults.remove(key);
		}
	}

	/**
	 * Retrieve a list of records that satisfy the specified criteria. Note, though,
	 * that unlike the other OAI verb type methods implemented here, both of the
	 * listRecords methods are already implemented in AbstractCatalog rather than
	 * abstracted. This is because it is possible to implement ListRecords as a
	 * combination of ListIdentifiers and GetRecord combinations. Nevertheless,
	 * I suggest that you override both the AbstractCatalog.listRecords methods
	 * here since it will probably improve the performance if you create the
	 * response in one fell swoop rather than construct it one GetRecord at a time.
	 *
	 * @param from beginning date using the proper granularity
	 * @param until ending date using the proper granularity
	 * @param set the set name or null if no such limit is requested
	 * @param metadataPrefix the OAI metadataPrefix or null if no such limit is requested
	 * @return a Map object containing entries for a "records" Iterator object
	 * (containing XML <record/> Strings) and an optional "resumptionMap" Map.
	 * @exception CannotDisseminateFormatException the metadataPrefix isn't
	 * supported by the item.
	 */
	@SuppressWarnings("unchecked")
	public Map listRecords(String from, String until, String set, String metadataPrefix)
	throws CannotDisseminateFormatException {
		loadIndexReader(new File(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		purge(); // clean out old resumptionTokens
		Map listRecordsMap = new HashMap();
		ArrayList records = new ArrayList();

		/************************************************************************************
		 * perform the query on your DB according to the given parameters from, until and set
		 ************************************************************************************/

		if(sets.get(set) != null) {
			set = sets.get(set);
		}

		QueryParser parser = new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,dateField, docAnalyzer.getAnalyzer());
		Query query = null;
		String fromDate = from.replaceAll("-", "");
		fromDate = fromDate.replaceAll(":", "");
		fromDate = fromDate.replaceAll("T", "");
		fromDate = fromDate.replaceAll("Z", "0");
		String untilDate = until.replaceAll("-", "");
		untilDate = untilDate.replaceAll(":", "");
		untilDate = untilDate.replaceAll("T", "");
		untilDate = untilDate.replaceAll("Z", "0");
		Term termFrom = new Term(dateField, fromDate);
		Term termUntil = new Term(dateField, untilDate);
		RangeQuery rangeQuery = new RangeQuery(termFrom,termUntil,true);


		BooleanQuery q = null;
		Hits hits = null;

		if (set == null || !set.equalsIgnoreCase("")) {

			q = new BooleanQuery();

			q.add(rangeQuery, Occur.MUST);
			//			BooleanQuery.setMaxClauseCount(1000000); 
			if(set != null)q.add(new TermQuery(new Term(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().OAICAT_SERVER_CATALOG_SETFIELD),set)), Occur.MUST);
			log.debug(q);

			try {
				query = parser.parse(q.toString());
			} catch (Exception e) {
				log.error("listRecords",e);
			}

			try {
				hits = searcher.search(query);
			} catch (IOException e) {
				log.error("listRecords",e);
			} catch (Exception e) {
				log.error("listRecords",e);
			}
		}

		/** End Query **/

		int count = 0;

		/************************************************************************************
		 * create an Object[] nativeItem that contains all the results in your DB-format 
		 ************************************************************************************/
		String record = "empty_string";
		/* load the records ArrayList */
		//Object[] nativeItem = new Object[hits.length()];
		//		for (int i = 0; i < hits.length(); i++) {
		//			try {
		//				nativeItem[i] = hits.doc(i);
		//				System.out.println(i);
		//			} catch (IOException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
		/** End Create **/

		if (hits != null) {
			for (count = 0; count < maxListSize && count < hits.length(); ++count) {
				//			record = constructRecord(nativeItem[count], metadataPrefix);
				try {
					record = constructRecord(hits.doc(count), metadataPrefix);
					records.add(record);
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/* decide if you're done */
			if (count < hits.length()) {
				String resumptionId = getResumptionId();

				/*****************************************************************
				 * Store an object appropriate for your database API in the
				 * resumptionResults Map in place of nativeItems. This object
				 * should probably encapsulate the information necessary to
				 * perform the next resumption of ListIdentifiers. It might even
				 * be possible to encode everything you need in the
				 * resumptionToken, in which case you won't need the
				 * resumptionResults Map. Here, I've done a silly combination
				 * of the two. Stateless resumptionTokens have some advantages.
				 *****************************************************************/
				resumptionResults.put(resumptionId, query);

				/*****************************************************************
				 * Construct the resumptionToken String however you see fit.
				 *****************************************************************/
				StringBuffer resumptionTokenSb = new StringBuffer();
				resumptionTokenSb.append(resumptionId);
				resumptionTokenSb.append(":");
				resumptionTokenSb.append(Integer.toString(count));
				resumptionTokenSb.append(":");
				resumptionTokenSb.append(metadataPrefix);

				listRecordsMap.put("resumptionMap", getResumptionMap(resumptionTokenSb.toString(), hits.length(), 0));
			}
		}
		listRecordsMap.put("records", records.iterator());

		closeIndexReader(reader);
		return listRecordsMap;
	}

	/**
	 * Retrieve the next set of records associated with the resumptionToken
	 *
	 * @param resumptionToken implementation-dependent format taken from the
	 * previous listRecords() Map result.
	 * @return a Map object containing entries for "headers" and "identifiers" Iterators
	 * (both containing Strings) as well as an optional "resumptionMap" Map.
	 * @exception BadResumptionTokenException the value of the resumptionToken argument
	 * is invalid or expired.
	 */
	@SuppressWarnings("unchecked")
	public Map listRecords(String resumptionToken)
	throws BadResumptionTokenException {
		loadIndexReader(new File(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Map listRecordsMap = new HashMap();
		ArrayList records = new ArrayList();
		purge(); // clean out old resumptionTokens

		/**********************************************************************
		 * YOUR CODE GOES HERE
		 **********************************************************************/
		/**********************************************************************
		 * parse your resumptionToken and look it up in the resumptionResults,
		 * if necessary
		 **********************************************************************/
		StringTokenizer tokenizer = new StringTokenizer(resumptionToken, ":");
		String resumptionId;
		int oldCount;
		String metadataPrefix;
		try {
			resumptionId = tokenizer.nextToken();
			oldCount = Integer.parseInt(tokenizer.nextToken());
			metadataPrefix = tokenizer.nextToken();
		} catch (NoSuchElementException e) {
			throw new BadResumptionTokenException();
		}

		/* Get some more records from your database */
		Query query = (Query)resumptionResults.remove(resumptionId);
		if (query == null) {
			throw new BadResumptionTokenException();
		}
		int count;
		Hits hits = null;
		try {
			hits = searcher.search(query);
			//			hits = SingletonIndexSearcher.search(query);
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* load the headers and identifiers ArrayLists. */
		for (count = 0; count < maxListSize && count+oldCount < hits.length(); ++count) {
			try {
				String record = constructRecord(hits.doc(count+oldCount), metadataPrefix);
				records.add(record);
			} catch (CannotDisseminateFormatException e) {
				/* the client hacked the resumptionToken beyond repair */
				throw new BadResumptionTokenException();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/* decide if you're done */
		if (count+oldCount < hits.length()) {
			resumptionId = getResumptionId();

			/*****************************************************************
			 * Store an object appropriate for your database API in the
			 * resumptionResults Map in place of nativeItems. This object
			 * should probably encapsulate the information necessary to
			 * perform the next resumption of ListIdentifiers. It might even
			 * be possible to encode everything you need in the
			 * resumptionToken, in which case you won't need the
			 * resumptionResults Map. Here, I've done a silly combination
			 * of the two. Stateless resumptionTokens have some advantages.
			 *****************************************************************/
			resumptionResults.put(resumptionId, query);

			/*****************************************************************
			 * Construct the resumptionToken String however you see fit.
			 *****************************************************************/
			StringBuffer resumptionTokenSb = new StringBuffer();
			resumptionTokenSb.append(resumptionId);
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(Integer.toString(oldCount + count));
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(metadataPrefix);

			listRecordsMap.put("resumptionMap", getResumptionMap(resumptionTokenSb.toString(),
					hits.length(),
					oldCount));
		}
		listRecordsMap.put("records", records.iterator());
		closeIndexReader(reader);
		return listRecordsMap;
	}

	@SuppressWarnings("unchecked")
	public Map listIdentifiers(String resumptionToken) throws BadResumptionTokenException, OAIInternalServerError {
		purge(); // clean out old resumptionTokens
		Map listIdentifiersMap = new HashMap();
		ArrayList headers = new ArrayList();
		ArrayList identifiers = new ArrayList();

		StringTokenizer tokenizer = new StringTokenizer(resumptionToken, ":");
		String resumptionId;
		int oldCount;
		String metadataPrefix;
		try {
			resumptionId = tokenizer.nextToken();
			oldCount = Integer.parseInt(tokenizer.nextToken());
			metadataPrefix = tokenizer.nextToken();
		} catch (NoSuchElementException e) {
			throw new BadResumptionTokenException();
		}

		/* Get some more records from your database */
		Object[] nativeItems = (Object[])resumptionResults.remove(resumptionId);
		if (nativeItems == null) {
			throw new BadResumptionTokenException();
		}
		int count;

		/* load the headers and identifiers ArrayLists. */
		for (count = 0; count < maxListSize && count+oldCount < nativeItems.length; ++count) {
			/* Use the RecordFactory to extract header/identifier pairs for each item */
			String[] header = getRecordFactory().createHeader(nativeItems[count+oldCount]);
			headers.add(header[0]);
			identifiers.add(header[1]);
		}

		/* decide if you're done. */
		if (count+oldCount < nativeItems.length) {
			resumptionId = getResumptionId();

			resumptionResults.put(resumptionId, nativeItems);

			/*****************************************************************
			 * Construct the resumptionToken String however you see fit.
			 *****************************************************************/
			StringBuffer resumptionTokenSb = new StringBuffer();
			resumptionTokenSb.append(resumptionId);
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(Integer.toString(oldCount + count));
			resumptionTokenSb.append(":");
			resumptionTokenSb.append(metadataPrefix);

			listIdentifiersMap.put("resumptionMap", getResumptionMap(resumptionTokenSb.toString(),
					nativeItems.length,
					oldCount));
		}
		listIdentifiersMap.put("headers", headers.iterator());
		listIdentifiersMap.put("identifiers", identifiers.iterator());
		return listIdentifiersMap;
	}


	//	protected String parseToLuceneQuery(String query){
	//		try {
	//			StringTokenizer tokenizer = new StringTokenizer(query, ":");
	//			String result = tokenizer.nextToken();
	//			while(tokenizer.hasMoreElements()){
	//				result = result.concat("\\:" + tokenizer.nextToken());
	//			}
	//			return result;
	//		} catch (Exception e) {
	//			return null;
	//		}
	//	}

	public String getRecord(String oaiIdentifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException, OAIInternalServerError {
		loadIndexReader(new File(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR)));
		IndexSearcher searcher = new IndexSearcher(reader);

		String localIdentifier = oaiIdentifier;
		localIdentifier = localIdentifier.replaceAll("[:]", "\\\\:");

		Hits hits = null;
		try {
			hits = searcher.search(new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,"contents", docAnalyzer.getAnalyzer()).parse(identifierField + ":" + localIdentifier));
		} catch (IOException e) {
			throw new OAIInternalServerError(e.getMessage());
		} catch (ParseException e) {
			throw new OAIInternalServerError(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document resultDoc = null;
		if(hits.length() == 0){
			throw new IdDoesNotExistException(oaiIdentifier);
		}
		else {
			try {
				resultDoc = hits.doc(0);
			} catch (IOException e) {
				throw new OAIInternalServerError(e.getMessage());
			}
		}
		closeIndexReader(reader);
		return constructRecord(resultDoc, metadataPrefix);
	}

	public void close() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}

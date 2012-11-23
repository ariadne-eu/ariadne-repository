package org.ariadne_eu.utils.registry.query;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.store.FSDirectory;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzer;
import org.ariadne_eu.utils.lucene.analysis.DocumentAnalyzerFactory;

import be.cenorm.www.CreateAnonymousSession;
import be.cenorm.www.CreateAnonymousSessionResponse;
import be.cenorm.www.GetTotalResultsCount;
import be.cenorm.www.GetTotalResultsCountResponse;
import be.cenorm.www.SetQueryLanguage;
import be.cenorm.www.SetResultsFormat;
import be.cenorm.www.SetResultsSetSize;
import be.cenorm.www.SqiSessionManagementStub;
import be.cenorm.www.SqiTargetStub;
import be.cenorm.www.SynchronousQuery;
import be.cenorm.www.SynchronousQueryResponse;
import be.cenorm.www._SQIFaultException;

public class Query {
	
	private static SqiSessionManagementStub sqiSessionStub;
	private static SqiTargetStub sqiStub;
	
	public static String createAnonymousSession(String target){
		try {
			sqiSessionStub = new SqiSessionManagementStub(target);
			CreateAnonymousSession createASession = new CreateAnonymousSession();
			CreateAnonymousSessionResponse sessionResponse = sqiSessionStub.createAnonymousSession(createASession);
			String sessionId = sessionResponse.getCreateAnonymousSessionReturn();
			return sessionId;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
	}
	
	public static void setQueryLanguage(String sessionId, String language) throws RemoteException, _SQIFaultException {
		SetQueryLanguage queryLanguage = new SetQueryLanguage();
		queryLanguage.setQueryLanguageID(language);
		queryLanguage.setTargetSessionID(sessionId);
		sqiStub.setQueryLanguage(queryLanguage);

	}
	
	public static void setResultSetSize(String sessionId, int resultSize) throws RemoteException, _SQIFaultException {
		SetResultsSetSize resultsSetSize = new SetResultsSetSize();
		resultsSetSize.setResultsSetSize(resultSize);
		resultsSetSize.setTargetSessionID(sessionId);
		sqiStub.setResultsSetSize(resultsSetSize);
	}
	
	public static void setResultSetFormat(String sessionId, String format) throws RemoteException, _SQIFaultException {
		SetResultsFormat resultsSetFormat = new SetResultsFormat();
		resultsSetFormat.setResultsFormat(format);
		resultsSetFormat.setTargetSessionID(sessionId);
		sqiStub.setResultsFormat(resultsSetFormat);
	}
	
	public static String query(String sessionId, String query, int startResult) throws RemoteException, _SQIFaultException {
		SynchronousQuery syncQuery = new SynchronousQuery();
		syncQuery.setQueryStatement(query);
		syncQuery.setStartResult(startResult);
		syncQuery.setTargetSessionID(sessionId);
		
		SynchronousQueryResponse result = sqiStub.synchronousQuery(syncQuery);
		String synchronousQueryReturn = result.getSynchronousQueryReturn();
		return synchronousQueryReturn;
		
	}
	
	public static int countResults(String sessionId, String query) throws RemoteException, _SQIFaultException {
		GetTotalResultsCount getTotalResultsCount = new GetTotalResultsCount();
		getTotalResultsCount.setQueryStatement(query);
		getTotalResultsCount.setTargetSessionID(sessionId);
		GetTotalResultsCountResponse countResponse = sqiStub.getTotalResultsCount(getTotalResultsCount);
		return countResponse.getGetTotalResultsCountReturn();
	}
	
	public static String doQuery(String query, String axis2_url_default, int resultSize){

		try {
			String sessionId, format, language;
			int startResult;
			String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
		    if (axis2_url == null) axis2_url = axis2_url_default;
		        //axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";
			sessionId = createAnonymousSession(axis2_url + "/services/SqiSessionManagement");
			sqiStub = new SqiTargetStub(axis2_url + "/services/SqiTarget");
			
			//Set the Query language
			language = "plql1";
			setQueryLanguage(sessionId, language);
			
			setResultSetSize(sessionId, resultSize);
			format = "lom";
			setResultSetFormat(sessionId, format);
			
			//Do the query
			startResult = 1;
			
		
			String result = query(sessionId, query, startResult);

			return result;
			
		} catch (AxisFault e) {
			return null;
		} catch (RemoteException e) {
			return null;
		} catch (_SQIFaultException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		
		
	}
	
	public static String doQueryLucene(){
		org.apache.lucene.document.Document doc;
		try {
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(PropertiesManager.getInstance().getProperty("search.lucene.indexdir"))));
			IndexSearcher is = new IndexSearcher(reader);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar now = Calendar.getInstance();
			Calendar thePreviousWeekCalendar = (Calendar) now.clone();
			thePreviousWeekCalendar.add(Calendar.DAY_OF_YEAR, -20);
			
			
			RangeQuery query = new RangeQuery(new Term("date.insert", format.format(thePreviousWeekCalendar.getTime())), new Term("date.insert",format.format(now.getTime())), true);
			Hits hits;
			hits = is.search(query);
			StringBuilder sBuild = new StringBuilder();
			sBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<results cardinality=\""+hits.length()+"\">\n");
			for (int i = 0; i < hits.length() ; i++) {
				doc = hits.doc(i);
				sBuild.append(doc.get("md"));
			}
			sBuild.append("</results>");
			return sBuild.toString();
		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
		}
		
	}
	
	public static String doQueryLuceneDate(String lQuery){
		org.apache.lucene.document.Document doc;
		try {
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(PropertiesManager.getInstance().getProperty("search.lucene.indexdir"))));
			IndexSearcher is = new IndexSearcher(reader);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			/*Calendar now = Calendar.getInstance();
			Calendar thePreviousWeekCalendar = (Calendar) now.clone();
			thePreviousWeekCalendar.add(Calendar.DAY_OF_YEAR, -20);*/
						
			//RangeQuery query = new RangeQuery(new Term("date.insert", format.format(thePreviousWeekCalendar.getTime())), new Term("date.insert",format.format(now.getTime())), true);
			DocumentAnalyzer analyzer = DocumentAnalyzerFactory.getDocumentAnalyzerImpl();
			org.apache.lucene.search.Query query = new QueryParser(RepositoryConstants.getInstance().SR_LUCENE_VERSION,"key",  analyzer.getAnalyzer()).parse(lQuery);
			Hits hits;
			hits = is.search(query);
			StringBuilder sBuild = new StringBuilder();
			for (int i = 0; i < hits.length() ; i++) {
				doc = hits.doc(i);
				sBuild.append(doc.get("date.insert"));
			}
			return sBuild.toString();
		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
		} catch (org.apache.lucene.queryParser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				return null;
		}
		
	}

	
	public static void main(String[] args){
		doQueryLucene();
	}

}

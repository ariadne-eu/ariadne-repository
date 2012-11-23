package org.ariadne_eu.utils.registry;

import java.util.ArrayList;
import java.util.List;

public class Sqi {
	boolean _annonymous;
	String _mode;
	List<String> _queryLanguage;
	List<String> _resultFormat;
	List<String> _supportedAsynchronousMethod;
	List<String> _supportedSynchronousMethod;
	String _persistentSessionId;
	String _sessionService;
	String _queryService;
	
	public Sqi(){
		_queryLanguage = new ArrayList<String>();
		_resultFormat = new ArrayList<String>();
		_supportedAsynchronousMethod = new ArrayList<String>();
		_supportedSynchronousMethod = new ArrayList<String>();
	}
	
	public void setAnnonymousIdentification(){
		_annonymous = true;
	}
	
	public void setRequiredIdentification(){
		_annonymous = false;
	}
	
	public boolean getAnnonymousIdentification(){
		return _annonymous;
	}
	
	public void setModeSynchronous(){
		_mode = "synchronous";
		setQueryLanguageSynchronousMethod();
		setResultsFormatSynchronousMethod();
		setMaxQueryResultsSynchronousMethod();
		setMaxDurationSynchronousMethod();
		setResultsSetSizeSynchronousMethod();
		synchronousQuerySynchronousMethod();
		getTotalResultsCountSynchronousMethod();
	}
	
	public void setModeAsynchronous(){
		_mode = "asynchronous";
		setQueryLanguageAsynchronousMethod();
		setResultsFormatAsynchronousMethod();
		setMaxQueryResultsAsynchronousMethod();
		setMaxDurationAsynchronousMethod();
		asynchronousQueryAsynchronousMethod();
		setTotalResultsCountAsynchronousMethod();		
	}
	
	public String getMode(){
		return _mode;
	}
	
	public void setPersistentSessionId(String persistentSessionId){
		_persistentSessionId=persistentSessionId;		
	}
	
	public void setSessionService(String sessionService){
		_sessionService = sessionService;
	}
	
	public String getPresistentService(){
		return _persistentSessionId;
	}
	
	public String getSessionService(){
		return _sessionService;
	}
	
	public void setQueryService(String queryService){
		_queryService = queryService;
	}
	
	public String getQueryService(){
		return _queryService;		
	}
	
	public void addQueryLanguage(String language){
		_queryLanguage.add(language);		
	}
	
	public ArrayList<String> getQueryLanguage(){
		return (ArrayList<String>)_queryLanguage;		
	}
	
	public void addResultsFormat(String format){
		_resultFormat.add(format);		
	}
	
	public ArrayList<String> getResultsFormat(){
		return (ArrayList<String>)_resultFormat;
	}

	
	public void setQueryLanguageSynchronousMethod(){
		_supportedSynchronousMethod.add("setQueryLanguage");
		
	}
	public void setResultsFormatSynchronousMethod(){
		_supportedSynchronousMethod.add("setResultsFormat");
		
	}
	public void setMaxQueryResultsSynchronousMethod(){
		_supportedSynchronousMethod.add("setMaxQueryResults");
		
	}
	public void setMaxDurationSynchronousMethod(){
		_supportedSynchronousMethod.add("setMaxDuration");
		
	}
	public void setResultsSetSizeSynchronousMethod(){
		_supportedSynchronousMethod.add("setResultsSetSize");
		
	}
	public void synchronousQuerySynchronousMethod(){
		_supportedSynchronousMethod.add("synchronousQuery");
		
	}
	
	public void getTotalResultsCountSynchronousMethod(){
		_supportedSynchronousMethod.add("getTotalResultsCount");
		
	}
	
	
	public void setQueryLanguageAsynchronousMethod(){
		_supportedAsynchronousMethod.add("setQueryLanguage");
		
	}
	public void setResultsFormatAsynchronousMethod(){
		_supportedAsynchronousMethod.add("setResultsFormat");
		
	}
	public void setMaxQueryResultsAsynchronousMethod(){
		_supportedAsynchronousMethod.add("setMaxQueryResults");
		
	}
	public void setMaxDurationAsynchronousMethod(){
		_supportedAsynchronousMethod.add("setMaxDuration");
		
	}

	public void asynchronousQueryAsynchronousMethod(){
		_supportedAsynchronousMethod.add("asynchronousQuery");
		
	}
	
	public void setTotalResultsCountAsynchronousMethod(){
		_supportedAsynchronousMethod.add("setSourceLocation");
		
	}
	
	public ArrayList<String> getSynchronousMethodsSupported(){
		return (ArrayList<String>)_supportedSynchronousMethod;
	}
	
	public ArrayList<String> getAsynchronousMethodsSupported(){
		return (ArrayList<String>)_supportedAsynchronousMethod;
	}
	
	public String getXMLSqi(String tab){
		String xml = "";
		xml = tab+"<sqi xmlns=\"http://www.imsglobal.org/services/lode/imslosqi-1p0_v1p0\" xsi:schemaLocation=\"http://www.imsglobal.org/services/lode/imslosqi-1p0_v1p0 http://fire.eun.org/xsd/registry/imslosqi-1p0_v1p0.xsd \">\n";
		xml += tab+ "\t<anonymous>"+_annonymous+"</anonymous>\n" ;
		xml += tab+ "\t<mode>"+_mode+"</mode>\n" ;
		for (int i=0;i<_queryLanguage.size();i++){
			xml += tab+"\t<queryLanguage>"+ ((String)_queryLanguage.get(i))+"</queryLanguage>\n";
		}
		for (int i=0;i<_resultFormat.size();i++){
			xml += tab+ ("\t<resultFormat>"+_resultFormat.get(i)+"</resultFormat>\n");
		}
		for (int i=0;i<_supportedSynchronousMethod.size();i++){
			xml += tab+ ("\t<supportedSynchronousMethod>"+_supportedSynchronousMethod.get(i)+"</supportedSynchronousMethod>\n");
		}
		for (int i=0;i<_supportedAsynchronousMethod.size();i++){
			xml += tab+ ("\t<supportedAsynchronousMethod>"+_supportedAsynchronousMethod.get(i)+"</supportedAsynchronousMethod>\n");
		}
		xml += tab+"\t<sessionService>"+_sessionService+"</sessionService>\n" +
				tab+"\t<queryService>"+_queryService+"</queryService>\n" +
				tab+"</sqi>\n";
		return xml;
	}
	
	
}

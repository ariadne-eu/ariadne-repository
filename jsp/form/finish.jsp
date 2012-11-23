<%@page import="java.util.Properties" %>
<%@page import="java.io.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.util.*"%>
<%@page import="org.ariadne_eu.utils.update.QueryOnId"%>
<%@page import="org.ariadne_eu.utils.update.UpdateMetadata"%>
<%@page import="org.ariadne.config.PropertiesManager"%>
<%@page import="java.net.Authenticator"%>
<%@page import="java.net.PasswordAuthentication"%>
<%@page import="org.ariadne.util.ClientHttpRequest"%>
<%@page import="uiuc.oai.OAIRepository"%>
<%@page import="org.ariadne_eu.utils.registry.*"%>
<%@page import="uiuc.oai.OAIMetadataFormat"%>
<%@page import="uiuc.oai.OAIMetadataFormatList"%>
<%@page import="uiuc.oai.OAISetList"%>
<%@page import="org.ariadne_eu.utils.update.UpdateMetadataCollection"%>
<%@page import="org.ariadne.config.PropertiesManager,org.ariadne_eu.utils.config.servlets.Log4jInit,org.ariadne.config.PropertiesManager"%>
<%@page import="org.ariadne_eu.utils.config.RepositoryConstants"%>
<%!

boolean exists (String dir) {
	File testfile = new File(dir);
    if (testfile.exists() && testfile.isDirectory()) {
        return true;
    }
    else{
    	return false;
    }
}


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">





<%@page import="org.ariadne_eu.utils.registry.auth.CheckDatabase"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne OAI Harvester - Web Installer</title>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="css/install.css" type="text/css" />
<script type="text/javascript">
<!--


//-->
</script>
<%
      pageContext.include("/layout/headLinks.jsp");
%>

  </head>
  <body>
<%
    pageContext.include("/layout/header.jsp");
%>

<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-off">New Repository</div>
			<div class="step-off">SQI Target</div>
			<div class="step-off">OAI Target</div>
			<div class="step-off">SPI Target</div>
			<div class="step-on">Finish</div>

</div>

<div id="right">

<div id="step">Finish</div>

<div class="far-right">

</div>
<div class="clr"></div>

<%
String catalog = request.getParameter("catalog");
String repositoryName = request.getParameter("repositoryName");

String description = request.getParameter("description");
String email = request.getParameter("email");
String targetURLSqi = request.getParameter("targetURLSqi");
if (targetURLSqi!=null) targetURLSqi=targetURLSqi.trim();
String sessionURLSqi = request.getParameter("sessionURLSqi");
if (sessionURLSqi!=null) sessionURLSqi=sessionURLSqi.trim();
String sessionAuthSqi = request.getParameter("sessionAuthSqi");
if (sessionAuthSqi!=null) sessionAuthSqi=sessionAuthSqi.trim();
String langcodePLQL = request.getParameter("langcodePLQL");
String langcodeVSQL = request.getParameter("langcodeVSQL");
String langcodeQEL = request.getParameter("langcodeQEL");
String langcodeOther = request.getParameter("langcodeOther");
String synchronous = request.getParameter("synchronous");
String asynchronous = request.getParameter("asynchronous");
String resultFormatLOM = request.getParameter("resultFormatLOM");
String resultFormatRDF = request.getParameter("resultFormatRDF");
String resultFormatOther = request.getParameter("resultFormatOther");
String targetURLOai = request.getParameter("targetURLOai");
if (targetURLOai!=null) targetURLOai=targetURLOai.trim();
String targetURLSpi = request.getParameter("targetURLSpi");
if (targetURLSpi!=null) targetURLSpi=targetURLSpi.trim();


String id_repository = repositoryName.replaceAll(" ","");

String result = null;
int j = 0;


String temp = id_repository;
do{
	
	try{
		result = QueryOnId.getMACEquery().getMetadataCollectionInstance(temp);
		temp = id_repository + "_" + j++;
	}catch(Exception e){
		result=null;
	}	
}while (result!=null);

MetadataCollection metadataCollection = new MetadataCollection();
if (catalog.compareTo("")==0) metadataCollection.getIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG));
else metadataCollection.getIdentifier().setCatalog(catalog);
metadataCollection.getIdentifier().setEntry(temp);
metadataCollection.getDescription().setLanguage("en");
metadataCollection.getDescription().setString(description);
metadataCollection.getResponsible().setVCard("BEGIN:VCARD\\nEMAIL\\;TYPE=INTERNET:"+email+"\\nEND:VCARD\\n");

Sqi sqi= null;

if ((targetURLSqi.compareTo("")!=0)&&(sessionURLSqi.compareTo("")!=0)&&((langcodePLQL!=null)||(langcodeVSQL!=null)||(langcodeQEL!=null)||(langcodeOther.compareTo("")!=0))&&((synchronous!=null)||(asynchronous!=null))){
	sqi = new Sqi();
	if (targetURLSqi!=null){
		sqi.setQueryService(targetURLSqi);
	}
	if ((sessionAuthSqi!=null)&&((sessionAuthSqi).compareTo("auth")==0)) sqi.setRequiredIdentification();
	else sqi.setAnnonymousIdentification();
	if (sessionURLSqi!=null) sqi.setSessionService(sessionURLSqi);
	
	if (langcodePLQL!=null){
		sqi.addQueryLanguage("plql0");
		sqi.addQueryLanguage("plql1");
	}
	if (langcodeVSQL!=null) sqi.addQueryLanguage("vsql");
	if (langcodeQEL!=null) sqi.addQueryLanguage("qel");
	if (langcodeOther!=null){
		String[] langcode = langcodeOther.split(",");
		for(int i=0; i<langcode.length;i++){
			if (langcode[i].compareTo("")!=0) sqi.addQueryLanguage(langcode[i]);
		}
	}
	if (resultFormatLOM!=null){
		sqi.addResultsFormat("lom");
	}
	if (resultFormatRDF!=null){
		sqi.addResultsFormat("rdf");
	}
	if (resultFormatOther!=null){
		String[] format = resultFormatOther.split(",");
		for(int i=0; i<format.length;i++){
			if (format[i].compareTo("")!=0) sqi.addResultsFormat(format[i]);
		}
	}
	
	
	if (synchronous!=null) sqi.setModeSynchronous();
	if (asynchronous!=null) sqi.setModeAsynchronous();
	TargetDescription targetDescription = new TargetDescription();
	if (catalog.compareTo("")==0) targetDescription.getIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG)+"_targets");
	else targetDescription.getIdentifier().setCatalog(catalog+"_targets");
	targetDescription.getIdentifier().setEntry("target-sqi-"+temp);
	if (catalog.compareTo("")==0) targetDescription.getProtocolIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG)+"-protocols-targets");
	else targetDescription.getProtocolIdentifier().setCatalog(catalog+"-protocols-targets");
	targetDescription.getProtocolIdentifier().setEntry("sqi-v1");
	targetDescription.setLocation(targetURLSqi);
	targetDescription.getProtocolImplementationDescription().setSqi(sqi);
	metadataCollection.addTarget(targetDescription);
	
}

OAIRepository oairepository = null;
if (targetURLOai.compareTo("")!=0) {
	try{
		oairepository = new OAIRepository();
		oairepository.setBaseURL(targetURLOai);
		OaiPmh oaiPmh = new OaiPmh();
		OAIMetadataFormatList metadataFormatList = oairepository.listMetadataFormats();
		for (int i=0; i<metadataFormatList.getCompleteSize(); i++){
			try{
			MetadataFormat metadataFormat = new MetadataFormat();
			OAIMetadataFormat oaiMetadataFormat = metadataFormatList.getCurrentItem();
			metadataFormat.setMetadataPrefix(oaiMetadataFormat.getMetadataPrefix());
			metadataFormat.setMetadataNameSpace(oaiMetadataFormat.getMetadataNamespace());
			metadataFormat.setSchema(oaiMetadataFormat.getSchema());
			oaiPmh.addMetadataFormat(metadataFormat);
			metadataFormatList.moveNext();
			}catch(NullPointerException e){
				MetadataFormat metadataFormat = new MetadataFormat();			
				metadataFormat.setMetadataPrefix("null");
				metadataFormat.setMetadataNameSpace("null");
				metadataFormat.setSchema("null");
				oaiPmh.addMetadataFormat(metadataFormat);
				metadataFormatList.moveNext();
			}
		}
		OAISetList oaiSetList = oairepository.listSets();
		for (int i=0; i<oaiSetList.getCompleteSize(); i++){
			try{
				oaiPmh.addSets(oaiSetList.getCurrentItem().getSetSpec());
				oaiSetList.moveNext();
			}catch(NullPointerException e){
				oaiPmh.addSets("null");
				oaiSetList.moveNext();
			}
		}
		oaiPmh.setDeletedRecord(oairepository.getDeletedRecord());
		oaiPmh.setGranularuty(oairepository.getGranularity());
		oaiPmh.setEarliestDateStamp(oairepository.getEarliestDatestamp());
		TargetDescription targetDescription = new TargetDescription();
		if (catalog.compareTo("")==0) targetDescription.getIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG)+"_targets");
		else targetDescription.getIdentifier().setCatalog(catalog+ "_targets");
		targetDescription.getIdentifier().setEntry("target-oai-pmh-"+temp);
		targetDescription.getProtocolIdentifier().setCatalog("ariadne-protocols-targets");
		targetDescription.getProtocolIdentifier().setEntry("oai-pmh-v2");
		targetDescription.setLocation(targetURLOai);
		targetDescription.getProtocolImplementationDescription().setOaiPmh(oaiPmh);
		metadataCollection.addTarget(targetDescription);
	}catch(Exception e){
		e.printStackTrace();
		out.println(e);
		oairepository=null;
	}	
}

if (targetURLSpi.compareTo("")!=0){
	TargetDescription targetDescription = new TargetDescription();
	if (catalog.compareTo("")==0) targetDescription.getIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG)+"_targets");
	else targetDescription.getIdentifier().setCatalog(catalog+"_targets");
	targetDescription.getIdentifier().setEntry("target-spi-"+temp);
	if (catalog.compareTo("")==0) targetDescription.getProtocolIdentifier().setCatalog(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_CATALOG)+"-protocols-targets");
	else targetDescription.getProtocolIdentifier().setCatalog(catalog+"-protocols-targets");
	targetDescription.getProtocolIdentifier().setEntry("spi");
	targetDescription.setLocation(targetURLSpi);
	metadataCollection.addTarget(targetDescription);
}

if (metadataCollection.getTarget().size()==0){
	out.println("<h1>Please, introduce correct data</h1>");
	
}else{

	try{
		UpdateMetadataCollection.getInstance().publishMetadata(metadataCollection.getXMLMetadataCollection().trim());
		CheckDatabase.insertCollection((String)request.getSession().getAttribute("username"), metadataCollection.getIdentifier().getEntry());
		out.println("<h1>Target published successfully!</h1>");
		out.println("<a href=\"../search/index.jsp?query=metadatacollection.identifier.entry="+temp+"\">Consult your target published</a>");
		
	}catch(Exception e){
		out.println(e); 
		out.println(metadataCollection.getXMLMetadataCollection());
	}
}

	
	%>

<div class="clr"></div>
</div>
<div class="clr"></div>
</div>
<div class="clr"></div>


<%     pageContext.include("/layout/footer.jsp"); %> </body>

</html>
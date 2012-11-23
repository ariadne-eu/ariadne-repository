<%@page import="java.util.Properties" %>
<%@page import="java.io.File" %>
<%@page import="java.io.FileOutputStream" %>
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
<%@page import="org.ariadne.config.PropertiesManager,org.ariadne_eu.utils.config.servlets.Log4jInit,org.ariadne.config.PropertiesManager"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne OAI Harvester - Web Installer</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
<div class="step-off">Pre-installation</div>
<div class="step-off">Store selection</div>
<div class="step-off">Connection</div>
<div class="step-off">Admin</div>
<div class="step-off">Options</div>
<div class="step-on">Finish</div>

</div>

<div id="right">

<div id="step">Finish</div>

<div class="far-right">

</div>
<div class="clr"></div>

<%
//(new Log4jInit()).reloadLogging();

/*if(error.equals("")){
	out.println("<h1>Installation Successfull</h1>");
	out.println("The configuration files have been created and the configuration details have been saved.<br>");
	out.println("Please go to <a href=../configuration/testConfiguration.jsp>Test Configuration</a> to fully test the current configuration.");	
	out.println("<div class=\"center\"><br/><br/><br/><br/><br/><br/><br/>");
	out.println("<input name=\"Button2\" type=\"submit\" class=\"button\" value=\"Home\" onclick=\"window.location='../start/index.jsp'\"/></div>");
}
else{
	out.println("<h1>Installation Failed</h1>");
	out.println(error);
}*/
	
	Properties ariadne = new Properties();
	if (request.getParameter("cntstore").contains("fileSystem")){
		PropertiesManager.getInstance().saveProperty("mdstore.insert.implementation","org.ariadne_eu.metadata.insert.InsertMetadataFSImpl");
		PropertiesManager.getInstance().saveProperty("mdstore.delete.implementation","org.ariadne_eu.metadata.delete.DeleteMetadataFSImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.insert.implementation","org.ariadne_eu.content.insert.InsertContentFSImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.retrieve.implementation","org.ariadne_eu.content.retrieve.RetrieveContentFSImpl");
		PropertiesManager.getInstance().saveProperty("search.lucene.reindex","org.ariadne_eu.utils.lucene.reindex.ReIndexFSImpl");
	}else if (request.getParameter("cntstore").contains("db2")){
		PropertiesManager.getInstance().saveProperty("mdstore.insert.implementation","org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl");
		PropertiesManager.getInstance().saveProperty("mdstore.delete.implementation","org.ariadne_eu.metadata.delete.DeleteMetadataIBMDB2DbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.insert.implementation","org.ariadne_eu.content.insert.InsertContentIBMDB2DbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.retrieve.implementation","org.ariadne_eu.content.retrieve.RetrieveContentIBMDB2DbImpl");
		PropertiesManager.getInstance().saveProperty("search.lucene.reindex","org.ariadne_eu.utils.lucene.reindex.ReIndexExistDbImpl");
	}else if (request.getParameter("cntstore").contains("existDB")){
		PropertiesManager.getInstance().saveProperty("mdstore.insert.implementation","org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl");
		PropertiesManager.getInstance().saveProperty("mdstore.delete.implementation","org.ariadne_eu.metadata.delete.DeleteMetadataExistDbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.insert.implementation","org.ariadne_eu.content.insert.InsertContentExistDbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.retrieve.implementation","org.ariadne_eu.content.retrieve.RetrieveContentExistDbImpl");
		PropertiesManager.getInstance().saveProperty("search.lucene.reindex","org.ariadne_eu.utils.lucene.reindex.ReIndexIBMDB2DbImpl");
	}else if (request.getParameter("cntstore").contains("Oracle")){
		PropertiesManager.getInstance().saveProperty("mdstore.insert.implementation","org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl");
		PropertiesManager.getInstance().saveProperty("mdstore.delete.implementation","org.ariadne_eu.metadata.delete.DeleteMetadataOracleDbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.insert.implementation","org.ariadne_eu.content.insert.InsertContentOracleDbImpl");
		PropertiesManager.getInstance().saveProperty("cntstore.retrieve.implementation","org.ariadne_eu.content.retrieve.RetrieveContentOracleDbImpl");
	}
	
	PropertiesManager.getInstance().saveProperty("repository.username",request.getParameter("user"));
	PropertiesManager.getInstance().saveProperty("repository.password",request.getParameter("pass"));
	PropertiesManager.getInstance().saveProperty("repository.log4j.directory",request.getParameter("logSystemDir"));
	PropertiesManager.getInstance().saveProperty("repository.log4j.filename",request.getParameter("nameLogs"));
	PropertiesManager.getInstance().saveProperty("search.lucene.indexdir",request.getParameter("indexSystemDir"));
	PropertiesManager.getInstance().saveProperty("mdstore.spifs.dir",request.getParameter("fileSystemDir"));
	PropertiesManager.getInstance().saveProperty("search.lucene.handler",request.getParameter("handler"));
	PropertiesManager.getInstance().saveProperty("search.lucene.analyzer",request.getParameter("analyzer"));
	PropertiesManager.getInstance().saveProperty("search.lucene.reindex.maxqueryresults","50");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.1","metaMetadata/identifier/catalog[text()=\"oai\"]/parent::*/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.2","general/identifier/catalog[text()=\"oai\"]/parent::*/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.3","metaMetadata/identifier/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.4","general/identifier/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.5","//general/identifier/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.xpath.query.identifier.6","//identifier/entry/text()");
	PropertiesManager.getInstance().saveProperty("search.lucene.handler.mace","/Sandbox/app/apache-tomcat-5.5.26/webapps/repository/install/MACE_LOM_Category_9_CLASSIFICATION_v5.xml");
	PropertiesManager.getInstance().saveProperty("search.solr.dataDir","/Sandbox/temp/AriadneWS/repository/");
	PropertiesManager.getInstance().saveProperty("search.solr.instancedir","/Sandbox/app/apache-tomcat-5.5.26/webapps/repository/solr/");
	PropertiesManager.getInstance().saveProperty("search.solr.facetfield.1","lom.general.language");
	PropertiesManager.getInstance().saveProperty("search.solr.facetfield.2","lom.metametadata.identifier.catalog");
	PropertiesManager.getInstance().saveProperty("mdstore.insert.implementation.1","org.ariadne_eu.metadata.insert.InsertMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.delete.implementation.1","org.ariadne_eu.metadata.delete.DeleteMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.query.implementation.0","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.query.implementation.1","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.query.implementation.2","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.query.implementation.3","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
	PropertiesManager.getInstance().saveProperty("mdstore.insert.xmlns.xsd","http://ltsc.ieee.org/xsd/LOM");
	PropertiesManager.getInstance().saveProperty("mdstore.xquery.wholeword","false");
	PropertiesManager.getInstance().saveProperty("cntstore.dr.basepath","/Sandbox/temp/AriadneWS/cntstore/");
	PropertiesManager.getInstance().saveProperty("cntstore.md.xpathquery.location.1","technical/location/text()");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.seconds2live","360");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.granularity","YYYY-MM-DDThh:mm:ssZ");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.maxlistsize","100");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.email","ariadne@cs.kuleuven.be");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.reponame","AriadneNext Repository");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.earliestdatestamp","1000-01-01T00:00:00Z");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.deletedrecord","no");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.repoid","oaicat.ariadne.org");
	PropertiesManager.getInstance().saveProperty("oaicat.identify.description.1","<description><oai-identifier xmlns=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\\\"><scheme>oai</scheme><repositoryIdentifier>oaicat.ariadne.org</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:oaicat.ariadne.org:hdl:OCLCNo/ocm00000012</sampleIdentifier></oai-identifier></description>");
	if (request.getParameter("handler").compareTo("org.ariadne_eu.utils.lucene.document.CollectionHandler")!=0) {
		PropertiesManager.getInstance().saveProperty("oaicat.crosswalk.lom","org.ariadne_eu.oai.server.lucene.crosswalk.Lucene2oai_lom");
	}else{
		PropertiesManager.getInstance().removeKeyFromPropertiesFile("oaicat.crosswalk.lom");
		PropertiesManager.getInstance().saveProperty("oaicat.crosswalk.lom","org.ariadne_eu.oai.server.lucene.crosswalk.Lucene2oai_reg");  		
	}
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.class","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomCatalog");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.record.class","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomRecordFactory");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.field.md","md");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.field.date","date.insert");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.field.id","key");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.repoid","oaicat.ariadne.org");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.field.set","collection");
	PropertiesManager.getInstance().saveProperty("oaicat.server.catalog.fs.ext","xml");
	PropertiesManager.getInstance().saveProperty("oaicat.sets.ARIADNE.repoid","ARIADNE");
	PropertiesManager.getInstance().saveProperty("oaicat.handler.useoaischeme","false");
	PropertiesManager.getInstance().saveProperty("OAIHandler.useOaiIdScheme","false");
	PropertiesManager.getInstance().saveProperty("AbstractCatalog.oaiCatalogClassName","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomCatalog");
	PropertiesManager.getInstance().saveProperty("AbstractCatalog.recordFactoryClassName","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomRecordFactory"); 
	PropertiesManager.getInstance().saveProperty("AbstractCatalog.secondsToLive","3600"); 
	PropertiesManager.getInstance().saveProperty("AbstractCatalog.granularity","YYYY-MM-DDThh:mm:ssZ"); 
	PropertiesManager.getInstance().saveProperty("LuceneLomCatalog.maxListSize","100"); 
	PropertiesManager.getInstance().saveProperty("LuceneLomCatalog.dateField","date.insert"); 
	PropertiesManager.getInstance().saveProperty("LuceneLomCatalog.identifierField","key"); 
	PropertiesManager.getInstance().saveProperty("LuceneLomRecordFactory.repositoryIdentifier","oaicat.ariadne.org"); 
	PropertiesManager.getInstance().saveProperty("Lucene2oai_lom.fullLomField","md"); 
	PropertiesManager.getInstance().saveProperty("Identify.adminEmail","ariadne@cs.kuleuven.be"); 
	PropertiesManager.getInstance().saveProperty("Identify.repositoryName","AriadneNext Repository"); 
	PropertiesManager.getInstance().saveProperty("Identify.earliestDatestamp","1000-01-01T00:00:00Z"); 
	PropertiesManager.getInstance().saveProperty("Identify.deletedRecord","no"); 
	PropertiesManager.getInstance().saveProperty("Identify.repositoryIdentifier","oaicat.ariadne.org"); 
	PropertiesManager.getInstance().saveProperty("Identify.description.1","<description><oai-identifier xmlns=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\\\"><scheme>oai</scheme><repositoryIdentifier>oaicat.ariadne.org</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:oaicat.ariadne.org:hdl:OCLCNo/ocm00000012</sampleIdentifier></oai-identifier></description>"); 
	PropertiesManager.getInstance().saveProperty("Crosswalks.oai_lom","org.ariadne_eu.oai.server.lucene.crosswalk.Lucene2oai_lom"); 
	PropertiesManager.getInstance().saveProperty("mace.oai.aloe.target","http://mace.dfki.uni-kl.de/cgi-bin/oai_aloe.pl"); 
	PropertiesManager.getInstance().saveProperty("mace.oai.aloe.mdprefix","mace_lom"); 
	PropertiesManager.getInstance().saveProperty("app.baseURL","http://localhost:8080/repository/"); 
	PropertiesManager.getInstance().saveProperty("app.workspace.title","Ariadne SPI workspace"); 
	PropertiesManager.getInstance().saveProperty("app.collection.title","Ariadne SPI collection"); 
	PropertiesManager.getInstance().saveProperty("app.metadataSchema.1","http://ltsc.ieee.org/xsd/LOM/loose"); 
	PropertiesManager.getInstance().saveProperty("app.metadataSchema.2","http://www.share-tec.eu/validation/ShareTEC/minimal"); 
	PropertiesManager.getInstance().saveProperty("app.publishMetadata","yes");
	if (exists(request.getParameter("logSystemDir"))&&exists(request.getParameter("indexSystemDir"))&&exists(request.getParameter("fileSystemDir"))){
		/*File file = new File(application.getRealPath("install/"+"ariadne.properties"));                           
    	file.createNewFile();
    	ariadne.store(new FileOutputStream(file), "");  */
    	PropertiesManager.getInstance().init();
    	Log4jInit.reloadLive();
    	
    	out.println("<h1>Installation Successfull</h1>");
	}else{		
		out.println("<h1>Installation Failled. Directories specified don't exist</h1>");
		if (!exists(request.getParameter("logSystemDir"))){
			out.println("<br>"+request.getParameter("logSystemDir")+" doesn't exist");
			
		}
		if (!exists(request.getParameter("indexSystemDir"))){
			out.println("<br>"+request.getParameter("indexSystemDir")+" doesn't exist");
			
		}
		if (!exists(request.getParameter("fileSystemDir"))){
			out.println("<br>"+request.getParameter("fileSystemDir")+" doesn't exist");
			
		}
		
	}
	%>

<div class="clr"></div>
</div>
<div class="clr"></div>
</div>
<div class="clr"></div>


<div class="ctr">
Ariadne Foundation<br />
<a href="http://www.ariadne-eu.org/" target="_blank">ARIADNE</a> is an European Association open to the World, for Knowledge Sharing and Reuse.<br> The core of the ARIADNE infrastructure is a distributed network of learning repositories.
</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.io.InputStream" %>
<%@page import="java.util.Properties" %>
<%@page import="java.util.Enumeration" %>
<%@page import="java.io.File" %>
<%@page import="java.io.FileOutputStream" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>


<%
    /*InputStream stream = application.getResourceAsStream("/installation/ariadneTEMPLATE.properties");
    Properties props = new Properties();
    props.load(stream);
    int i=0;
    Enumeration enume = props.propertyNames();
    for (Enumeration e = props.propertyNames() ; e.hasMoreElements() ;) {
        out.println("Elemento "+i+":"+e.nextElement()+"<br/>");i++;

    }*/
    try{ 
    	if(request.getParameter("sendForm").contentEquals("true")){
    		out.println("Form sent");
    		Properties ariadne = new Properties();
    		ariadne.setProperty("repository.username",request.getParameter("repository.username"));
    		ariadne.setProperty("repository.password",request.getParameter("repository.password"));
    		ariadne.setProperty("repository.log4j.directory",request.getParameter("repository.log4j.directory"));
    		ariadne.setProperty("repository.log4j.filename",request.getParameter("repository.log4j.filename"));
    		ariadne.setProperty("search.lucene.indexdir",request.getParameter("search.lucene.indexdir"));
    		ariadne.setProperty("mdstore.spifs.dir",request.getParameter("mdstore.spifs.dir"));
    		ariadne.setProperty("search.lucene.handler",request.getParameter("search.lucene.handler"));
    		ariadne.setProperty("search.lucene.reindex",request.getParameter("search.lucene.reindex"));
    		ariadne.setProperty("mdstore.insert.implementation",request.getParameter("mdstore.insert.implementation"));
    		ariadne.setProperty("mdstore.delete.implementation",request.getParameter("mdstore.delete.implementation"));
    		ariadne.setProperty("cntstore.insert.implementation",request.getParameter("cntstore.insert.implementation"));
    		ariadne.setProperty("cntstore.retrieve.implementation",request.getParameter("cntstore.retrieve.implementation"));
    		ariadne.setProperty("search.lucene.reindex.maxqueryresults","50");
    		ariadne.setProperty("search.xpath.query.identifier.1","metaMetadata/identifier/catalog[text()=\"oai\"]/parent::*/entry/text()");
    		ariadne.setProperty("search.xpath.query.identifier.2","general/identifier/catalog[text()=\"oai\"]/parent::*/entry/text()");
    		ariadne.setProperty("search.xpath.query.identifier.3","metaMetadata/identifier/entry/text()");
    		ariadne.setProperty("search.xpath.query.identifier.4","general/identifier/entry/text()");
    		ariadne.setProperty("search.xpath.query.identifier.5","//general/identifier/entry/text()");
    		ariadne.setProperty("search.xpath.query.identifier.6","//identifier/entry/text()");
    		ariadne.setProperty("search.lucene.handler.mace","/Sandbox/app/apache-tomcat-5.5.26/webapps/repository/install/MACE_LOM_Category_9_CLASSIFICATION_v5.xml");
    		ariadne.setProperty("search.solr.dataDir","/Sandbox/temp/AriadneWS/repository/");
    		ariadne.setProperty("search.solr.instancedir","/Sandbox/app/apache-tomcat-5.5.26/webapps/repository/solr/");
    		ariadne.setProperty("search.solr.facetfield.1","lom.general.language");
    		ariadne.setProperty("search.solr.facetfield.2","lom.metametadata.identifier.catalog");
    		ariadne.setProperty("mdstore.insert.implementation.1","org.ariadne_eu.metadata.insert.InsertMetadataLuceneImp");
    		ariadne.setProperty("mdstore.delete.implementation.1","org.ariadne_eu.metadata.delete.DeleteMetadataLuceneImpl");
    		ariadne.setProperty("mdstore.query.implementation.0","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
    		ariadne.setProperty("mdstore.query.implementation.1","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
    		ariadne.setProperty("mdstore.query.implementation.2","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
    		ariadne.setProperty("mdstore.query.implementation.3","org.ariadne_eu.metadata.query.QueryMetadataLuceneImpl");
    		ariadne.setProperty("mdstore.insert.xmlns.xsd","http://ltsc.ieee.org/xsd/LOM");
    		ariadne.setProperty("mdstore.xquery.wholeword","false");
    		ariadne.setProperty("cntstore.dr.basepath","/Sandbox/temp/AriadneWS/cntstore/");
    		ariadne.setProperty("cntstore.md.xpathquery.location.1","technical/location/text()");
    		ariadne.setProperty("oaicat.server.catalog.seconds2live","360");
    		ariadne.setProperty("oaicat.server.catalog.granularity","YYYY-MM-DDThh:mm:ssZ");
    		ariadne.setProperty("oaicat.server.catalog.maxlistsize","100");
    		ariadne.setProperty("oaicat.identify.email","ariadne@cs.kuleuven.be");
    		ariadne.setProperty("oaicat.identify.reponame","AriadneNext Repository");
    		ariadne.setProperty("oaicat.identify.earliestdatestamp","1000-01-01T00:00:00Z");
    		ariadne.setProperty("oaicat.identify.deletedrecord","no");
    		ariadne.setProperty("oaicat.identify.repoid","oaicat.ariadne.org");
    		ariadne.setProperty("oaicat.identify.description.1","<description><oai-identifier xmlns=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\\\"><scheme>oai</scheme><repositoryIdentifier>oaicat.ariadne.org</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:oaicat.ariadne.org:hdl:OCLCNo/ocm00000012</sampleIdentifier></oai-identifier></description>");
    		ariadne.setProperty("oaicat.crosswalk.lom","org.ariadne_eu.oai.server.lucene.crosswalk.Lucene2oai_lom");
    		ariadne.setProperty("oaicat.server.catalog.class","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomCatalog");
    		ariadne.setProperty("oaicat.server.catalog.record.class","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomRecordFactory");
    		ariadne.setProperty("oaicat.server.catalog.field.md","lom");
    		ariadne.setProperty("oaicat.server.catalog.field.date","date.insert");
    		ariadne.setProperty("oaicat.server.catalog.field.id","key");
    		ariadne.setProperty("oaicat.server.catalog.repoId","oaicat.ariadne.org");
    		ariadne.setProperty("oaicat.server.catalog.field.set","collection");
    		ariadne.setProperty("oaicat.server.catalog.fs.ext","");
    		ariadne.setProperty("oaicat.sets.ARIADNE.repoid","ARIADNE");
    		ariadne.setProperty("oaicat.handler.useoaischeme","false");
    		ariadne.setProperty("OAIHandler.useOaiIdScheme","false");
    		ariadne.setProperty("AbstractCatalog.oaiCatalogClassName","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomCatalog");
    		ariadne.setProperty("AbstractCatalog.recordFactoryClassName","org.ariadne_eu.oai.server.lucene.catalog.LuceneLomRecordFactory"); 
    		ariadne.setProperty("AbstractCatalog.secondsToLive","3600"); 
    		ariadne.setProperty("AbstractCatalog.granularity","YYYY-MM-DDThh:mm:ssZ"); 
    		ariadne.setProperty("LuceneLomCatalog.maxListSize","100"); 
    		ariadne.setProperty("LuceneLomCatalog.dateField","date.insert"); 
    		ariadne.setProperty("LuceneLomCatalog.identifierField","key"); 
    		ariadne.setProperty("LuceneLomRecordFactory.repositoryIdentifier","oaicat.ariadne.org"); 
    		ariadne.setProperty("Lucene2oai_lom.fullLomField","lom"); 
    		ariadne.setProperty("Identify.adminEmail","ariadne@cs.kuleuven.be"); 
    		ariadne.setProperty("Identify.repositoryName","AriadneNext Repository"); 
    		ariadne.setProperty("Identify.earliestDatestamp","1000-01-01T00:00:00Z"); 
    		ariadne.setProperty("Identify.deletedRecord","no"); 
    		ariadne.setProperty("Identify.repositoryIdentifier","oaicat.ariadne.org"); 
    		ariadne.setProperty("Identify.description.1","<description><oai-identifier xmlns=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\\\"><scheme>oai</scheme><repositoryIdentifier>oaicat.ariadne.org</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:oaicat.ariadne.org:hdl:OCLCNo/ocm00000012</sampleIdentifier></oai-identifier></description>"); 
    		ariadne.setProperty("Crosswalks.oai_lom","org.ariadne_eu.oai.server.lucene.crosswalk.Lucene2oai_lom"); 
    		ariadne.setProperty("mace.oai.aloe.target","http://mace.dfki.uni-kl.de/cgi-bin/oai_aloe.pl"); 
    		ariadne.setProperty("mace.oai.aloe.mdprefix","mace_lom"); 
    		ariadne.setProperty("app.baseURL","http://localhost:8080/repository/"); 
    		ariadne.setProperty("app.workspace.title","Ariadne SPI workspace"); 
    		ariadne.setProperty("app.collection.title","Ariadne SPI collection"); 
    		ariadne.setProperty("app.metadataSchema.1","http://ltsc.ieee.org/xsd/LOM/loose"); 
    		ariadne.setProperty("app.metadataSchema.2","http://www.share-tec.eu/validation/ShareTEC/minimal"); 
    		ariadne.setProperty("app.publishMetadata","yes"); 
    		File file = new File("../install/ariadne.properties");                           
            file.createNewFile();
            ariadne.store(new FileOutputStream(file), "");  		
    	}
    }catch(NullPointerException e){
		if (!new File("ariadne.properties").exists()){%>
			<form name="properties" action="index.jsp" method="post">
				<table border="1" align="center">
					<tr>
						<th align="right">repository.username = </th>
						<td align="left"><input  type="text" value="user" name="repository.username" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a username for the repository</td>
					</tr>
					<tr>
						<th align="right">repository.password =</th>
						<td align="left"><input  type="text" value="pass" name="repository.password" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a password for the repository</td>
					</tr>
					<tr>
						<th align="right">repository.log4j.directory =</th>
						<td align="left"><input  type="text" value="/installation/logs/" name="repository.log4j.directory" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a directory path where the logs will be stored (It must exist)</td>
					</tr>
					<tr>
						<th align="right">repository.log4j.filename =</th>
						<td align="left"><input  type="text" value="repository" name="repository.log4j.filename" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a file name for the logs. (All the logs will start with this name)</td>
					</tr>	
					<tr>
						<th align="right">search.lucene.indexdir =</th>
						<td align="left"><input  type="text" value="/installation/index/" name="search.lucene.indexdir" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a directory path where the lucene index will be stored (It must exist)</td>
					</tr>
					<tr>
						<th align="right">mdstore.spifs.dir =</th>
						<td align="left"><input  type="text" value="/installation/store/" name="mdstore.spifs.dir" size="30" maxlength="25" /></td> 
						<td align="left">Please, introduce a directory path where the files will be stored (It must exist)</td>
					</tr>	
					<tr>
						<th align="right">search.lucene.handler =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.utils.lucene.document.CollectionHandler" name="search.lucene.handler" checked/>org.ariadne_eu.utils.lucene.document.CollectionHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.document.GENERICHandler" name="search.lucene.handler"/>org.ariadne_eu.utils.lucene.document.GENERICHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.document.LOMHandler" name="search.lucene.handler"/>org.ariadne_eu.utils.lucene.document.LOMHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.document.CAMHandler" name="search.lucene.handler"/>org.ariadne_eu.utils.lucene.document.CAMHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.document.MACELOMHandler" name="search.lucene.handler"/>org.ariadne_eu.utils.lucene.document.MACELOMHandler
						</td> 
						<td align="left">Please, select the handler for lucene</td>
					</tr>
					<tr>
						<th align="right">search.lucene.reindex =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.utils.lucene.reindex.ReIndexFSImpl" name="search.lucene.reindex" checked/>org.ariadne_eu.utils.lucene.document.CollectionHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.reindex.ReIndexExistDbImpl" name="search.lucene.reindex"/>org.ariadne_eu.utils.lucene.document.GENERICHandler
						<br/><input  type="radio" value="org.ariadne_eu.utils.lucene.reindex.ReIndexIBMDB2DbImpl" name="search.lucene.reindex"/>org.ariadne_eu.utils.lucene.document.LOMHandler
						</td> 
						<td align="left">Please, select the handler for lucene</td>			
					</tr>
					<tr>
						<th align="right">mdstore.insert.implementation =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.metadata.insert.InsertMetadataFSImpl" name="mdstore.insert.implementation" checked/>org.ariadne_eu.metadata.insert.InsertMetadataFSImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl" name="mdstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl" name="mdstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl" name="mdstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl
						</td> 
						<td align="left">Please, select the method for inserting metadata</td>			
					</tr>
					<tr>
						<th align="right">mdstore.delete.implementation =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.metadata.delete.DeleteMetadataFSImpl" name="mdstore.delete.implementation" checked/>org.ariadne_eu.metadata.insert.InsertMetadataFSImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.delete.DeleteMetadataIBMDB2DbImpl" name="mdstore.delete.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.delete.DeleteMetadataExistDbImpl" name="mdstore.delete.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl
						<br/><input  type="radio" value="org.ariadne_eu.metadata.delete.DeleteMetadataOracleDbImpl" name="mdstore.delete.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl
						</td> 
						<td align="left">Please, select the method for deleting metadata</td>			
					</tr>
					<tr>
						<th align="right">cntstore.insert.implementation =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.content.insert.InsertContentFSImpl" name="cntstore.insert.implementation" checked/>org.ariadne_eu.metadata.insert.InsertMetadataFSImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.insert.InsertContentIBMDB2DbImpl" name="cntstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.insert.InsertContentExistDbImpl" name="cntstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.insert.InsertContentOracleDbImpl" name="cntstore.insert.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl
						</td> 
						<td align="left">Please, select the method for storing metadata</td>			
					</tr>
					<tr>
						<th align="right">cntstore.retrieve.implementation =</th>
						<td align="left"><input  type="radio" value="org.ariadne_eu.content.retrieve.RetrieveContentFSImpl" name="cntstore.retrieve.implementation" checked/>org.ariadne_eu.metadata.insert.InsertMetadataFSImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.retrieve.RetrieveContentIBMDB2DbImpl" name="cntstore.retrieve.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.retrieve.RetrieveContentExistDbImpl" name="cntstore.retrieve.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl
						<br/><input  type="radio" value="org.ariadne_eu.content.retrieve.RetrieveContentOracleDbImpl" name="cntstore.retrieve.implementation"/>org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl
						</td> 
						<td align="left">Please, select content store retrieve implementation</td>			
					</tr>
					<tr>
						<th align="right"></th>
						<td align="center"><input  type="submit" value="Create ariadne.properties"/></td> 
						<td align="left"></td>
					</tr>		
				</table>
				<input type="hidden" name="sendForm" value="true"/>
			</form>
		<%}
	}%>
</body>
</html>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.ariadne.config.PropertiesManager"%>

<%@page import="java.util.TreeSet"%>
<%@page import="java.util.StringTokenizer"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne Repository - Web Installer</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="css/install.css" type="text/css" />
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>
  <body>
<%
    pageContext.include("/layout/header.jsp");
%>
<FORM METHOD=POST ACTION="logging.jsp">


<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-off">Pre-installation</div>
<div class="step-off">Store selection</div>
<div class="step-off">Store properties</div>
<div class="step-on">Index properties</div>
<div class="step-off">Admin</div>
<div class="step-off">Finish</div>

</div>

<div id="right">

<div id="step">Store selection</div>

<div class="far-right">
<input name="Button2" type="submit" class="button" value="Next >>">
</div>
<div class="clr"></div>

<h1>Where to save the repository metadata ?</h1>
<%String handler = PropertiesManager.getInstance().getProperty("search.lucene.handler"); 
String analyzer = PropertiesManager.getInstance().getProperty("search.lucene.analyzer");%>

<div class="install-text">
    			<p>Select the store where you want to save the repository metadata</p>
  	   			<p></p>
     			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
    			<div class="form-block">
  							<fieldset><legend>Handler System</legend>
								<table cellpadding="1" cellspacing="1" border="0">

                            		<tr>
										<td><input  type="radio" value="org.ariadne_eu.utils.lucene.document.CollectionHandler" name="handler" <% if (handler.compareTo("org.ariadne_eu.utils.lucene.document.CollectionHandler")==0) out.println("checked"); %>/></td>
										<td><label for="Disk">Collection Handler (Registry)</label></td>
									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.document.GENERICHandler" name="handler" <% if (handler.compareTo("org.ariadne_eu.utils.lucene.document.GENERICHandler")==0) out.println("checked"); %>/></td>
										<td><label for="Lucene">Generic Handler</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.document.LOMHandler" name="handler" <% if (handler.compareTo("org.ariadne_eu.utils.lucene.document.LOMHandler")==0) out.println("checked"); %>/></td>
										<td><label for="SPI">LOM Handler</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.document.CAMHandler" name="handler" <% if (handler.compareTo("org.ariadne_eu.utils.lucene.document.CAMHandler")==0) out.println("checked"); %> /></td>
										<td><label for="CenSoapSPI">CAM Handler</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.document.MACELOMHandler" name="handler" <% if (handler.compareTo("org.ariadne_eu.utils.lucene.document.MACELOMHandler")==0) out.println("checked"); %>/></td>
										<td><label for="CenSoapSPI">MACE LOM Handler</label></td>

									</tr>
								</table>
							</fieldset>
							<fieldset><legend>Search Lucene Analyzer</legend>
								<table cellpadding="1" cellspacing="1" border="0">

                            		<tr>
										<td><input  type="radio" value="org.ariadne_eu.utils.lucene.analysis.CollectionDocumentAnalyzer" name="analyzer" <% if (analyzer.compareTo("org.ariadne_eu.utils.lucene.analysis.CollectionDocumentAnalyzer")==0) out.println("checked"); %>/></td>
										<td><label for="Disk">Collection Handler (Registry)</label></td>
									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.analysis.GENERICDocumentAnalyzer" name="analyzer" <% if (analyzer.compareTo("org.ariadne_eu.utils.lucene.analysis.GENERICDocumentAnalyzer")==0) out.println("checked"); %>/></td>
										<td><label for="Lucene">Generic Handler</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.analysis.LOMDocumentAnalyzer" name="analyzer" <% if (analyzer.compareTo("org.ariadne_eu.utils.lucene.analysis.LOMDocumentAnalyzer")==0) out.println("checked"); %>/></td>
										<td><label for="SPI">LOM Handler</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="org.ariadne_eu.utils.lucene.analysis.CAMDocumentAnalyzer" name="analyzer" <% if (analyzer.compareTo("org.ariadne_eu.utils.lucene.analysis.CAMDocumentAnalyzer")==0) out.println("checked"); %>/></td>
										<td><label for="SPI">CAM Handler</label></td>

									</tr>
								</table>
								
							</fieldset>
					<input type="hidden" name="cntstore" value="<%=request.getParameter("cntstore")%>"/>
					<input type="hidden" name="logSystemDir" value="<%=request.getParameter("logSystemDir")%>"/>
		  	     	<input type="hidden" name="indexSystemDir" value="<%=request.getParameter("indexSystemDir")%>"/>
		  	     	<input type="hidden" name="fileSystemDir" value="<%=request.getParameter("fileSystemDir")%>"/>
		  	     	<input type="hidden" name="nameLogs" value="<%=request.getParameter("nameLogs")%>"/>
    			</div>
    		</div>
</div>

<div class="clr"></div>
</div>

<div class="clr"></div>

<input type="hidden" id="previous" name="previous" value="storeSelect"/>

<div class="ctr">
Ariadne Foundation<br />
<a href="http://www.ariadne-eu.org/" target="_blank">ARIADNE</a> is an European Association open to the World, for Knowledge Sharing and Reuse.<br> The core of the ARIADNE infrastructure is a distributed network of learning repositories.
</div>
</form>
</body>
</html>
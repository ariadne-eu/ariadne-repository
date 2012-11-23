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
<FORM METHOD=POST ACTION="diskConnection.jsp">


<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-off">Pre-installation</div>
<div class="step-on">Store selection</div>
<div class="step-off">Connection</div>
<div class="step-off">Admin</div>
<div class="step-off">Options</div>
<div class="step-off">Finish</div>

</div>

<div id="right">

<div id="step">Store selection</div>

<div class="far-right">
<input name="Button2" type="submit" class="button" value="Next >>">
</div>
<div class="clr"></div>

<h1>Where to save the repository metadata ?</h1>

<%String stores = PropertiesManager.getInstance().getProperty("mdstore.insert.implementation"); %>

<div class="install-text">
    			<p>Select the store where you want to save the repository metadata</p>
  	   			<p></p>
     			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
    			<div class="form-block">
  							<fieldset><legend>Storing System</legend>
								<table cellpadding="1" cellspacing="1" border="0">

                            		<tr>
										<td><input  type="radio" value="fileSystem" name="cntstore" <% if (stores.compareTo("org.ariadne_eu.metadata.insert.InsertMetadataFSImpl")==0) out.println("checked"); %>/></td>
										<td><label for="Disk">File System</label></td>
									</tr>
									<tr>
                                		<td><input  type="radio" value="db2" name="cntstore" <% if (stores.compareTo("org.ariadne_eu.metadata.insert.InsertMetadataIBMDB2DbImpl")==0) out.println("checked"); %>/></td>
										<td><label for="Lucene">DB2</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="existDB" name="cntstore" <% if (stores.compareTo("org.ariadne_eu.metadata.insert.InsertMetadataExistDbImpl")==0) out.println("checked"); %> /></td>
										<td><label for="SPI">Exist DB</label></td>

									</tr>
									<tr>
                                		<td><input  type="radio" value="Oracle" name="cntstore" <% if (stores.compareTo("org.ariadne_eu.metadata.insert.InsertMetadataOracleDbImpl")==0) out.println("checked"); %>/></td>
										<td><label for="CenSoapSPI">Oracle</label></td>

									</tr>
								</table>								
							</fieldset>
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
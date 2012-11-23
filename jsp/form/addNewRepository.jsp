<%@ page import="be.cenorm.www.SqiTargetStub" %>
<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="be.cenorm.www.*" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="org.apache.xpath.XPathAPI" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="net.sf.vcard4j.parser.DomParser" %>
<%@ page import="net.sf.vcard4j.java.VCard" %>
<%@ page import="net.sf.vcard4j.java.AddressBook" %>
<%@ page import="net.sf.vcard4j.java.type.FN" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.*" %>
<%@ page import="org.apache.xerces.dom.DocumentImpl" %>
<%@ page import="net.sf.vcard4j.java.type.N" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.rmi.RemoteException" %>
<%@page import="org.ariadne.config.PropertiesManager"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/install.css" type="text/css" />
<title>Register a new harvester</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  <body>
<%
      pageContext.include("/layout/header.jsp");
%>
<form METHOD=POST name=form id=form ACTION="addSpiTarget.jsp" onsubmit="return check();">
<div id="ctr" align="center">
	<div class="install">
		<div id="stepbar">
			<div class="step-on">New Repository</div>
			<div class="step-off">SPI Target</div>
			<div class="step-off">OAI Target</div>
			<div class="step-off">SQI Target</div>
			<div class="step-off">Finish</div>
		</div>

		<div id="right">

			<div id="step">General Information about the <br/>repository</div>

			<div class="far-right">
				<input name="Button2" type="submit" class="button" value="Next >>" />
			</div>
			<div class="clr"></div>

			<h1>Introduce the information for the new repository</h1>

			<div class="install-text">
    			<p>Introduce the name of the repository, it will be use to create the id of the repository and its description:</p>

       			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
  	   			<div class="form-block">
  	     			<table class="content2">
  	     			<tr>
  		    			<td>Catalog:<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="catalog" value=""/></td>
  		    		</tr>
  	     			<tr>
  		    			<td>Repository name:<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="repositoryName" value=""/></td>
  		    		</tr>
  	     			<tr>
  		    			<td>Description: <br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="description" value=""/></td>
  		    		</tr>  	
  		    		<tr>
  		    			<td>Contact Person Email: <br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="email" value="<%if (request.getAttribute("username")!=null && request.getAttribute("openid")!=null) out.println(request.getAttribute("username"));  %>"/></td>
  		    		</tr>  	
		  	        </table>
  				</div>
			</div>

			<div class="clr"></div>
		</div>
	<div class="clr"></div>
	</div>
<div class="clr"></div>
</div>
</form>
<%     pageContext.include("/layout/footer.jsp"); %>
</body>
</html>

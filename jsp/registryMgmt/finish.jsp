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
<%@page import="java.util.Properties" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%><html>
<head>
<link rel="stylesheet" href="css/install.css" type="text/css" />
<title>Register a new harvester</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>
  <body>
<%
    pageContext.include("/layout/header.jsp");

	PropertiesManager properties = new PropertiesManager(); 
	 
	File file = new File(application.getRealPath("registryMgmt/"+"harvesters.properties"));                           
	properties.init(file); 
	if (!properties.getProperty("harvesters.list").contains(request.getParameter("id")+";"))
	properties.saveProperty("harvesters.list", properties.getProperty("harvesters.list")+request.getParameter("id")+";"); 	
	properties.saveProperty(request.getParameter("id")+".title",request.getParameter("title"));
	properties.saveProperty(request.getParameter("id")+".id",request.getParameter("id"));
	properties.saveProperty(request.getParameter("id")+".url",request.getParameter("url"));
	properties.saveProperty(request.getParameter("id")+".user",request.getParameter("user"));
	properties.saveProperty(request.getParameter("id")+".password",request.getParameter("pass"));

%>

<div id="ctr" align="center">
	<div class="install">
		<div id="stepbar">
			<div class="step-off">New Harvester</div>
			<div class="step-on">Finish</div>
		</div>

		<div id="right">

			<div id="step">Connection Information</div>

			<div class="clr"></div>

			<h1>Harvester Added Succesfully</h1>		

			<div class="clr"></div>
		</div>
	<div class="clr"></div>
	</div>
</div>
<div class="clr"></div>
</form>
<%     pageContext.include("/layout/footer.jsp"); %>
</body>
</html>

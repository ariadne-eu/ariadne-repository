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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
%>
<form METHOD=POST name=form id=form ACTION="finish.jsp" onsubmit="return check();">
<div id="ctr" align="center">
	<div class="install">
		<div id="stepbar">
			<div class="step-on">New Harvester</div>
			<div class="step-off">Finish</div>
		</div>

		<div id="right">

			<div id="step">General Information about the <br/>harvester</div>

			<div class="far-right">
				<input name="Button2" type="submit" class="button" value="Next >>" />
			</div>
			<div class="clr"></div>

			<h1>Introduce the information for the new <br/>harvester</h1>

			<div class="install-text">
    			<p>Introduce the information like URL service, user and password:</p>

       			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
  	   			<div class="form-block">
  	     			<table class="content2">
  	     			<tr>
  		    			<td>Title :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="title" value="<% 
  		    			try{
  		    				if (request.getParameter("title")!=null) out.println(request.getParameter("title"));
  		    			}catch(Exception e){}%>"/></td>
  		    		</tr>
  	     			<tr>
  		    			<td>ID :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="id" value="<%
  		    			try{
  		    				if (request.getParameter("id")!=null) out.println(request.getParameter("id"));
  		    			}catch(Exception e){}%>"/></td>
  		    		</tr>
  		  			<tr>
  		    			<td>URL :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="url" value="<%
  		    			try{
  		    				if (request.getParameter("url")!=null) out.println(request.getParameter("url"));
  		    			}catch(Exception e){}%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>User :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="user" value="<%
  		    			try{
  		    				if (request.getParameter("user")!=null) out.println(request.getParameter("user"));
  		    			}catch(Exception e){}%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>Password :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="pass" value="<%
  		    			try{
  		    				if (request.getParameter("password")!=null) out.println(request.getParameter("password"));
  		    			}catch(Exception e){}%>"/></td>
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

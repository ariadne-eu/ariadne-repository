<%@page import="org.ariadne.config.PropertiesManager"%>
<%@page import="java.util.*"%>
<%@page import="java.net.Authenticator"%>
<%@page import="java.net.PasswordAuthentication"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>





<%@page import="java.io.File"%><html>

<html>
  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Search page</title>
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
	
	String list = properties.getProperty("harvesters.list");
	String[] vector = list.split(";");
%>


<div class="searchResults">
  <center>
      <form action="index.jsp">
        <table align="center">
            <tr>
                <td>
                    <div class="box">
                        <div>

                              <table>
                                  <tr>
                                      <td><p>Select which harvester you want to configure:</p></td>
                                  </tr>
                                  <% for (int i=0;i<vector.length;i++){ %>
                                  <tr>
                                      <td><a href="search.jsp?query=http&id=<%=properties.getProperty(vector[i]+".id")%>"><%=properties.getProperty(vector[i]+".title")%></a> <a href="registerNewHarvester.jsp?id=<%=properties.getProperty(vector[i]+".id")%>&url=<%=properties.getProperty(vector[i]+".url")%>&title=<%=properties.getProperty(vector[i]+".title")%>&user=<%=properties.getProperty(vector[i]+".user")%>&password=<%=properties.getProperty(vector[i]+".password")%>">Edit Harvester</a></a></td>
                                  </tr>    
                                  <%} %>                                                              

                              </table>


                            <p class="last">&nbsp;</p>

                        </div>
                    </div>
                </td>
            </tr>
        </table>
      </form>
  </center>
</div>
</body>
</html>

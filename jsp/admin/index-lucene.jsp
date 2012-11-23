<%@ page import="org.ariadne_eu.utils.lucene.IndexOperations"  %>
<%@ page import="org.ariadne_eu.utils.lucene.reindex.ReIndexImpl" %>
<%@ page import="org.ariadne_eu.utils.lucene.reindex.ReIndexFactory" %>
<%--
  Created by IntelliJ IDEA.
  User: ben
  Date: 26-aug-2007
  Time: 10:43:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String rebuild = request.getParameter("rebuild");
    boolean rebuildSuccess = false;
    String message = null;
    try {
        if (rebuild != null && rebuild.length() > 0) {
        	ReIndexImpl reindex= ReIndexFactory.getReIndexImpl();
        	reindex.reIndexMetadata();
        	//IndexOperations.regenerateIndex();
            rebuildSuccess = true;
        }
    } catch (Exception e) {
        rebuildSuccess = false;
        message = e.getMessage();
    }
%>
<html> 
  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Rebuild the lucene index</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>
  <body>
<%
    pageContext.include("/layout/header.jsp");
%>
    <div class="page">
<%
    if (rebuildSuccess) {
%>
        <p>The Lucene index has been rebuild successfully</p>
<%
    } else {
        if (message != null) {
%>
            <p>An exception occured while trying to rebuild the Lucene index.</p>
            <p><%=message%></p>
<%
        }
%>
        <form action="index-lucene.jsp">
            <input type="submit" value="Rebuild" name="rebuild">
        </form>
<%
    }
%>
    </div>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>
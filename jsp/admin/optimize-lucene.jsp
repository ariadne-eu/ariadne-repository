<%@ page import="org.ariadne_eu.utils.lucene.IndexOperations" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String optimize = request.getParameter("optimize");
    boolean optimizeSuccess = false;
    String message = null;
    try {
        if (optimize != null && optimize.length() > 0) {
        	IndexOperations.optimize();
        	optimizeSuccess = true;
        }
    } catch (Exception e) {
    	optimizeSuccess = false;
        message = e.getMessage();
    }
%>
<html> 
  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Optimize the lucene index</title>
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
    if (optimizeSuccess) {
%>
        <p>The Lucene index has been optimized successfully</p>
<%
    } else {
        if (message != null) {
%>
            <p>An exception occured while trying to optimiz the Lucene index.</p>
            <p><%=message%></p>
<%
        }
%>
        <form action="optimize-lucene.jsp">
            <input type="submit" value="Optimize" name="optimize">
        </form>
<%
    }
%>
    </div>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>
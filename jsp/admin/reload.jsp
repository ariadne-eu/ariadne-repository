<%@ page import="org.ariadne_eu.utils.config.servlets.InitServlet" %>
<%--
  Created by IntelliJ IDEA.
  User: ben
  Date: 24-mrt-2007
  Time: 12:50:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String reload = request.getParameter("reload");
    boolean reloadSuccess = false;
    String message = null;
    try {
        if (reload != null && reload.length() > 0) {
            InitServlet.initializePropertiesManager();
            InitServlet.initializeServices();
            reloadSuccess = true;
        }
    } catch (Exception e) {
        reloadSuccess = false;
        message = e.getMessage();
    }
%>
<html> 
  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Reload the store</title> 
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
    if (reloadSuccess) {
%>
        <p>The Ariadne Store has been reloaded successfully</p>
<%
    } else {
        if (message != null) {
%>
            <p>An exception occured while trying to reload the Ariadne Store.</p>
            <p><%=message%></p>
<%
        }
%>
        <form action="reload.jsp">
            <input type="submit" value="Reload" name="reload">
        </form>
<%
    }
%>
    </div>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="org.ariadne_eu.utils.config.RepositoryConstants" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean wasLoggedIn = (request.getSession().getAttribute("login") != null && request.getSession().getAttribute("login").equals("true") && 	request.getSession().getAttribute("username") != null);
    request.getSession().setAttribute("login", "false");
    request.getSession().removeAttribute("username");

%>
<html>


  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Logged out</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>


  <body>

<%
    pageContext.include("/layout/header.jsp");
%>

<div class="page">

    <table align="center"><tr><td>
    <div class="box">
        <div>
<%
    if (wasLoggedIn) {
%>
            <h2>Thank you for logging out.
<%
    } else {
%>
            <h2>You were not logged in.
<%
    }
%>
            <br/><br/>You can:</h2>
            <ul><% if (!PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_AUTH).contains("openid")){%> 
                <li><a href="<%=request.getContextPath()%>/login/login.jsp">log in<%=wasLoggedIn ? " as another user" : ""%></a></li>
                <%} %>
                <li><a href="<%=request.getContextPath()%>/search/">search repository</a></li>
            </ul>
            <p class="last">&nbsp;</p>
        </div>
    </div>
    </td></tr></table>


</div>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>
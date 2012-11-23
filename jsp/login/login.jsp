<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="org.ariadne_eu.utils.config.RepositoryConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    boolean loginAttempt = false;
    if (username != null || password != null)
        loginAttempt = true;
    boolean loginSuccess = false;
    if (username != null && username.equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME)) &&
            password != null && password.equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD))) {
        loginSuccess = true;
        request.getSession().setAttribute("login", "true");
        request.getSession().setAttribute("username", username);
    }
%>

<html>


  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Login <%=loginSuccess ? "Successfull" : (loginAttempt ? "Failed" : "Page")%></title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>



  <body>

<%
    pageContext.include("/layout/header.jsp");
%>




<%
    if (loginSuccess) {
%>
<table align="center"><tr><td>
<div class="box">
    <div>
        <h2>You have successfully logged in.
            <br/><br/>Now you can:</h2>
        <ul>
            <li><a href="<%=request.getContextPath()%>/status/">view status</a></li>
            <li><a href="<%=request.getContextPath()%>/insert/">insert metadata &amp; files</a></li>
            <li><a href="<%=request.getContextPath()%>/obtain/">obtain metadata &amp; files</a></li>
            <li><a href="<%=request.getContextPath()%>/search/">search repository</a></li>
        </ul>
        <p class="last">&nbsp;</p>
    </div>
</div>
</td></tr></table>


<%
    } else {
        if (loginAttempt) {
%>
            <p>Your login has failed.</p>
<%
        }
%>
    <div align="center" id="ctr">
        <div class="login">
            <div class="login-form">
                <%--<img alt="Login" src="<%=request.getContextPath()%>/images/login.gif"/>--%>
                <form method="post" action="<%= request.getContextPath() %>/login/login.jsp">
                <div class="form-block">
                    <div class="inputlabel">Username</div>
                    <div>
                        <!--<input type="text" size="15" class="inputbox" name="userid"/>-->
                        <input type="text" size="15" class="inputbox" name="username" />
                    </div>
                    <div class="inputlabel">Password</div>
                    <div>
                        <!--<input type="password" size="15" class="inputbox" name="password"/>-->
                        <input type="password" size="15" class="inputbox" name="password" />
                    </div>
                    <div align="left">
                        <input type="submit" value="Login" class="button" name="login_submit" style="cursor:pointer;"/>
                            <!--<input type="submit" name="login_submit" value="Log In" />                    -->
                    </div>
                </div>
                </form>
            </div>
            <div class="login-text">
                <p>Welcome to <br/> <i>&lt;&lt;insert new name here&gt;&gt;</i></p>
                <p>Use a valid username and password to gain access.</p>
                <div class="ctr">
                    <img width="64" height="64" alt="security" src="<%=request.getContextPath()%>/images/security.png"/>
                    <img alt="Login" src="<%=request.getContextPath()%>/images/login.gif"/>
                </div>
                <%--<div class="ctr"><img alt="Login" src="<%=request.getContextPath()%>/images/login.gif"/></div>--%>
            </div>
            <div class="clr"/>
        </div>
    </div>
<%
    }
%>
<%
    pageContext.include("/layout/footer.jsp");
%>
  </body>
</html>
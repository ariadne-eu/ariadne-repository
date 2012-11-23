
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>

  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Insert metadata</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>

  <body>
<%
    pageContext.include("/layout/header.jsp");
%>


<div class="page">


    <center>

<div class="container">
    <div>
        <h2>To upload metadata &amp; a file you need to:</h2>
        <ol>
            <li><a href="<%=request.getContextPath()%>/insert/file.jsp">(upload a file &amp; remember the given identifier)</a></li>
            <li>(insert identifier in the matadata manually)</li>
            <li><a href="<%=request.getContextPath()%>/insert/metadata.jsp">upload or insert metadata</a></li>
        </ol>
    </div>
</div>

    </center>


</div>



<%
    pageContext.include("/layout/footer.jsp");
%>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>


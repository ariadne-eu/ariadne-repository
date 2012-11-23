<%@ page import="be.cenorm.www.SqiTargetStub" %>
<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="be.cenorm.www.*" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean queryFailed = false;
    String stacktrace = null;
    String message = null;

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    try {
        SqiTargetStub sqi = new SqiTargetStub(axis2_url + "/SqiTarget");
        SqiSessionManagementStub sm = new SqiSessionManagementStub(axis2_url + "/SqiSessionManagement");

        CreateAnonymousSessionResponse sessionM = sm.createAnonymousSession(new CreateAnonymousSession());

        SetQueryLanguage queryLanguage = new SetQueryLanguage();
        queryLanguage.setQueryLanguageID("plql0");
        queryLanguage.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
        sqi.setQueryLanguage(queryLanguage);

        SetResultsSetSize resultsSetSize = new SetResultsSetSize();
        resultsSetSize.setResultsSetSize(5);
        resultsSetSize.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
        sqi.setResultsSetSize(resultsSetSize);

        String query = "en";

        GetTotalResultsCount getTotalResultsCount = new GetTotalResultsCount();
        getTotalResultsCount.setQueryStatement(query);
        getTotalResultsCount.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
        GetTotalResultsCountResponse countResponse = sqi.getTotalResultsCount(getTotalResultsCount);


        SynchronousQuery synchronousQuery = new SynchronousQuery();
        synchronousQuery.setQueryStatement(query);
        synchronousQuery.setStartResult(1);
        synchronousQuery.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
        SynchronousQueryResponse result = sqi.synchronousQuery(synchronousQuery);
    } catch (Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        stacktrace = stringWriter.toString();
        message = e.getMessage();
        queryFailed = true;
    }

%>

<html>
  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Status page</title>
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
    if (queryFailed) {
%>
        <p>Warning: Test query failed.</p>
        <p><%=message%></p>
        <!--
        <%=stacktrace%>
        -->
<%
    } else {
%>
        <p>Test query executed successfully.</p>
<%
    }
%>
    </div>
    <%     pageContext.include("/layout/footer.jsp"); %> </body>
</html>
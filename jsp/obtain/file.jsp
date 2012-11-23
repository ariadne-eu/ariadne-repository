<%--
  Created by IntelliJ IDEA.
  User: ben & lieven
--%>

<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="be.cenorm.www.*" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="org.w3.www._2005._05.xmlmime.Base64Binary" %>
<%@ page import="javax.activation.DataHandler" %>
<%@ page import="org.ariadne_eu.coi.COIStub" %>
<%@ page import="org.ariadne_eu.coi.GetResource" %>
<%@ page import="org.ariadne_eu.coi.GetResourceResponse" %>
<%@ page import="org.ariadne_eu.coi.GetResourceName" %>
<%@ page import="org.ariadne_eu.coi.GetResourceNameResponse" %>
<%@ page import="org.ariadne_eu.utils.config.RepositoryConstants" %>
<%@ page import="java.io.*" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String identifier = request.getParameter("content");

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    if (identifier != null) {
        try {
            SqiSessionManagementStub sm = new SqiSessionManagementStub(axis2_url + "/SqiSessionManagement");
            CreateSession createSession = new CreateSession();
            createSession.setUserID(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME));
            createSession.setPassword(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD));
            CreateSessionResponse sessionM = sm.createSession(createSession);

            COIStub coi = new COIStub(axis2_url + "/COI");
            GetResource resource = new GetResource();
            resource.setIdentifier(identifier);
            resource.setTargetSessionID(sessionM.getCreateSessionReturn());
            GetResourceResponse getResourceResponse = coi.getResource(resource);
            Base64Binary binary = getResourceResponse.getBinaryData();
            DataHandler dataHandler = binary.getBase64Binary();

			GetResourceName rname = new GetResourceName();
			rname.setIdentifier(identifier);
			rname.setTargetSessionID(sessionM.getCreateSessionReturn());
			GetResourceNameResponse nameResponse = coi.getResourceName(rname);

            response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","Content-Disposition: attachment; filename=" + nameResponse.getName());


            final BufferedInputStream input = new BufferedInputStream(dataHandler.getInputStream());
            final BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
            final int BUFFER_SIZE = 1024 * 4;
            final byte[] buffer = new byte[BUFFER_SIZE];

            while (true)
            {
                final int count = input.read(buffer, 0, BUFFER_SIZE);

                if (-1 == count)
                {
                    break;
                }

                // write out those same bytes
                output.write(buffer, 0, count);
            }
            output.flush();

            dataHandler.getInputStream().close();
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            e.printStackTrace(writer);
        }
    } else {

%>

<html>

  <head>
      <link media="all" href="<%=request.getContextPath()%>/style.css" type="text/css" rel="stylesheet">
      <title>Obtain content</title>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>

  <body>
<%
    pageContext.include("/layout/header.jsp");
%>
    <div class="page">
        
  <div id="break"></div>
      <center>
          <form action="" method="post">
              <table>
                  <tr>
                      <td>
                          Identifier: <input type="text" name="content" size="50">
                      </td>
                      <td>
                          <input type="submit" name="submit" value="Download" />
                      </td>
                  </tr>
              </table>
          </form>
      </center>

    </div>
<%
    pageContext.include("/layout/footer.jsp");
%>
  </body>
</html>
<%
    }
%>
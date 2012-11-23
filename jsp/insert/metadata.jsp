<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="be.cenorm.www.*" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="org.ariadne_eu.spi.SPIStub" %>
<%@ page import="org.ariadne_eu.spi.CreateIdentifier" %>
<%@ page import="org.ariadne_eu.spi.CreateIdentifierResponse" %>
<%@ page import="org.ariadne_eu.spi.SubmitMetadataRecord" %>
<%@ page import="org.ariadne_eu.utils.FileUploadRequest" %>
<%@ page import="org.ariadne_eu.utils.config.RepositoryConstants" %>
<%@ page import="java.io.*" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String metadata = request.getParameter("metadata");

    String contentType = request.getContentType();
    if (metadata == null && contentType != null && contentType.indexOf("multipart/form-data") != -1) {
        FileUploadRequest wrapper = new FileUploadRequest(request);
        File file = wrapper.getFile("metadata");

        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        metadata = fileData.toString();
    }

    if (metadata != null) {
        metadata = new String(metadata.getBytes("iso-8859-1"), "UTF-8");
    }

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    CreateIdentifierResponse identifier = null;
    if (metadata != null && metadata.length() > 0) {
        try {
            SqiSessionManagementStub sm = new SqiSessionManagementStub(axis2_url + "/SqiSessionManagement");
            CreateSession createSession = new CreateSession();
            createSession.setUserID(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME));
            createSession.setPassword(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD));
            CreateSessionResponse sessionM = sm.createSession(createSession);

            SPIStub spi = new SPIStub(axis2_url + "/SPI");
            CreateIdentifier createIdentifier = new CreateIdentifier();
            createIdentifier.setCatalog("ARIADNE");
            createIdentifier.setTargetSessionID(sessionM.getCreateSessionReturn());
            identifier = spi.createIdentifier(createIdentifier);

            SubmitMetadataRecord submitMetadataRecord = new SubmitMetadataRecord();
            submitMetadataRecord.setGlobalIdentifier(identifier.getLocalIdentifier());
            submitMetadataRecord.setTargetSessionID(sessionM.getCreateSessionReturn());
            submitMetadataRecord.setMetadata(metadata);
            spi.submitMetadataRecord(submitMetadataRecord);
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            e.printStackTrace(writer);
        }
    }

%>


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
            <table align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <h2>To upload metadata &amp; a file you need to:</h2>
                        <ol>
                            <li><a href="<%=request.getContextPath()%>/insert/file.jsp">(upload a file &amp; remember the given identifier)</a></li>
                            <li>(insert identifier in the matadata manually)</li>
                            <li><a href="<%=request.getContextPath()%>/insert/metadata.jsp">upload or insert metadata</a></li>
                        </ol>
                    </td>
                </tr>
            </table>
          </div>

      </center>


      

  <center>


<div class="container">
    <div>

<%
    if (identifier != null) {
%>
        <p style="font-weight:bold; font-size:120%;">The submitted metadata has identifier <%=identifier.getLocalIdentifier()%></p>
        <br />
        <br />
<%
    }
%>
        
      <form action="" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
          <table>
              <tr>
                  <td>
                      <input type="file" name="metadata" size="50">
                  </td>
              </tr>
              <tr>
                  <td>
                      <input type="submit" name="submit" value="Insert metadata file" />
                  </td>
              </tr>
          </table>
      </form>
      <p align="center" style="font-size:20pt;">OR</p>
      <form action="metadata.jsp" method="post" enctype="application/x-www-form-urlencoded" accept-charset="UTF-8">
          <table>
              <tr>
                  <td>
                      <textarea rows="30" cols="65" name="metadata" style="font-size:9pt; font-family:monospace;" wrap="off" ></textarea>
                  </td>
              </tr>
              <tr>
                  <td>
                      <input type="submit" name="submit" value="Insert metadata text" />
                  </td>
              </tr>
          </table>
      </form>
    </div>
</div>
      

  </center>

  </div>

    <%     pageContext.include("/layout/footer.jsp"); %> </body>

</html>
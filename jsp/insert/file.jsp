<%@ page import="java.io.File" %>

<%@ page import="javax.activation.DataHandler" %>
<%@ page import="javax.activation.FileDataSource" %>

<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="org.ariadne_eu.spidev.SPIDevStub" %>
<%@ page import="org.ariadne_eu.spidev.SpiDevFaultException" %>
<%@ page import="org.ariadne_eu.spidev.SubmitResource" %>


<%@ page import="be.cenorm.www.*" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>

<%@ page import="org.ariadne_eu.spidev.CreateIdentifier" %>
<%@ page import="org.ariadne_eu.spidev.CreateIdentifierResponse" %>
<%@ page import="org.ariadne_eu.utils.FileUploadRequest" %>
<%@ page import="org.ariadne_eu.utils.config.RepositoryConstants" %>


<%@ page import="org.w3.www._2005._05.xmlmime.Base64Binary" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String contentType = request.getContentType();

    File content = null;
    if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1)) {
        FileUploadRequest wrapper = new FileUploadRequest(request);
        content = wrapper.getFile("content");
    }

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    CreateIdentifierResponse identifier = null;
    if (content != null) {
        try {
            SqiSessionManagementStub sm = new SqiSessionManagementStub(axis2_url + "/SqiSessionManagement");
            CreateSession createSession = new CreateSession();
            createSession.setUserID(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME));
            createSession.setPassword(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_PASSWORD));
            CreateSessionResponse sessionM = sm.createSession(createSession);

            SPIDevStub spi = new SPIDevStub(axis2_url + "/SPIDev");
            CreateIdentifier createIdentifier = new CreateIdentifier();
            createIdentifier.setCatalog("ARIADNE");
            createIdentifier.setTargetSessionID(sessionM.getCreateSessionReturn());
            identifier = spi.createIdentifier(createIdentifier);
			
            SubmitResource resource = new SubmitResource();
            resource.setGlobalIdentifier(identifier.getLocalIdentifier());
            resource.setTargetSessionID(sessionM.getCreateSessionReturn());
            Base64Binary binary = new Base64Binary();
            binary.setBase64Binary(new DataHandler(new FileDataSource(content)));
            resource.setBinaryData(binary);
			
			resource.setFileName(content.getName());
			resource.setFileType("application/pdf");

            spi.submitResource(resource);
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
      <title>Insert content</title>
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




<%
    if (identifier != null) {
%>
        <table>
            <tr>
                <td align="right">
                    The submitted content has identifier <%=identifier.getLocalIdentifier()%>
                </td>
            </tr>
        </table>
<%
    }
%>


  <center>


<div class="container">
    <div>
      <form action="" method="post" enctype="multipart/form-data">
          <table width="100%">
              <tr>
                  <td style="text-align:center;">
                      <input type="file" name="content" size="50">
                  </td>
              </tr>
              <tr><td>&nbsp;</td></tr><tr>
                  <td style="text-align:center;">
                      <input type="submit" name="submit" value="Upload" />
                  </td>
              </tr>
          </table>
      </form>

        <p class="last">&nbsp;</p>

    </div>
</div>

      
  </center>


  </div>

    <%     pageContext.include("/layout/footer.jsp"); %> </body>

</html>


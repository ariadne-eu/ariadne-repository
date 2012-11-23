<%@ page import="be.cenorm.www.SqiTargetStub" %>
<%@ page import="be.cenorm.www.SqiSessionManagementStub" %>
<%@ page import="be.cenorm.www.*" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="org.apache.xpath.XPathAPI" %>
<%@ page import="org.ariadne.config.PropertiesManager" %>
<%@ page import="net.sf.vcard4j.parser.DomParser" %>
<%@ page import="net.sf.vcard4j.java.VCard" %>
<%@ page import="net.sf.vcard4j.java.AddressBook" %>
<%@ page import="net.sf.vcard4j.java.type.FN" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.*" %>
<%@ page import="org.apache.xerces.dom.DocumentImpl" %>
<%@ page import="net.sf.vcard4j.java.type.N" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String query = request.getParameter("query");
    GetTotalResultsCountResponse countResponse = null;
    SynchronousQueryResponse synchronousQueryResponse = null;
    int resultSize = 10;
    int startResult = 1;
    try {
        if (request.getParameter("next") != null)
            startResult = Integer.parseInt(request.getParameter("start_result")) + resultSize;
    } catch (Exception e) {
    }

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    if (query != null && query.length() > 0) {
        try {
            SqiTargetStub sqi = new SqiTargetStub(axis2_url + "/SqiTarget");
            SqiSessionManagementStub sm = new SqiSessionManagementStub(axis2_url + "/SqiSessionManagement");

            CreateAnonymousSessionResponse sessionM = sm.createAnonymousSession(new CreateAnonymousSession());

            SetQueryLanguage queryLanguage = new SetQueryLanguage();
            queryLanguage.setQueryLanguageID("http://www.prolearn-project.org/PLQL/l1");
//            queryLanguage.setQueryLanguageID("vsql");
            queryLanguage.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
            sqi.setQueryLanguage(queryLanguage);

            SetResultsFormat resultsFormat = new SetResultsFormat();
//            resultsFormat.setResultsFormat("http://www.prolearn-project.org/PLRF/2/lom");
            resultsFormat.setResultsFormat("http://ltsc.ieee.org/xsd/LOM");
            resultsFormat.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
            sqi.setResultsFormat(resultsFormat);

            SetResultsSetSize resultsSetSize = new SetResultsSetSize();
            resultsSetSize.setResultsSetSize(resultSize);
            resultsSetSize.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
            sqi.setResultsSetSize(resultsSetSize);


            GetTotalResultsCount getTotalResultsCount = new GetTotalResultsCount();
            getTotalResultsCount.setQueryStatement(query);
            getTotalResultsCount.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
            countResponse = sqi.getTotalResultsCount(getTotalResultsCount);

            SynchronousQuery synchronousQuery = new SynchronousQuery();
            synchronousQuery.setQueryStatement(query);
            synchronousQuery.setStartResult(startResult);
            synchronousQuery.setTargetSessionID(sessionM.getCreateAnonymousSessionReturn());
            synchronousQueryResponse = sqi.synchronousQuery(synchronousQuery);
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
      <title>Search page</title>
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
      <form action="index.jsp">


        <table align="center">
            <tr>
                <td>
                    <div class="box">
                        <div>

                              <table>
                                  <tr>
                                      <td><p>Enter search query:</p></td>
                                  </tr>
                                  <tr>
                                      <td><input type="text" name="query" value="<%=query != null ? StringEscapeUtils.escapeHtml(query) : ""%>" /></td>
                                  </tr>
                                  <tr>
                                      <td><input type="submit" name="search" value="search" /></td>
                                  </tr>
                              </table>


                            <p class="last">&nbsp;</p>

                        </div>
                    </div>
                </td>
            </tr>
        </table>

          

      </form>
  </center>





<%
    if (countResponse != null && synchronousQueryResponse != null) {
%>



  <center>
  
<%
        int nbResults = countResponse.getGetTotalResultsCountReturn();
        if (nbResults > 0)
        {
%>





<div class="container" style="width:85%;">
    <div>
        <center>
            <p>Showing results <%=startResult%> to <%=Math.min(resultSize + startResult - 1, countResponse.getGetTotalResultsCountReturn())%> of <%=countResponse.getGetTotalResultsCountReturn()%>.</p>
            <form action="index.jsp">
                <input type="hidden" name="start_result" value="<%=startResult%>" />
                <input type="hidden" name="query" value="<%=query%>" />
                <input type="submit" name="next" value="next >>" />
            </form>
        </center>


        <table class="searchResults" cellpadding="0" cellspacing="0">

<%

    StringReader stringReader = new StringReader(synchronousQueryResponse.getSynchronousQueryReturn());
    InputSource input = new InputSource(stringReader);

    try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

        Node result = doc.getFirstChild();
        NodeList nl = result.getChildNodes();
        int currentResultCounter = 0;
        for (int i = 0; i < nl.getLength(); i++)
        {
            try {
                Element theNode = ((Element) nl.item(i));

                NodeList nl2;

                String title = "Untitled";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "general/title/string/text()");
                    title = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String description = "";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "general/description/string/text()");
                    description = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String date = "";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "lifeCycle/contribute/date/dateTime/text()");
                    date = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String contributorRole = "";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "lifeCycle/contribute/role/value/text()");
                    contributorRole = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String contributorVCard = "";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "lifeCycle/contribute/entity/text()");
                    contributorVCard = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String ref = "";
                try {
                    String res = "";
                    nl2 = XPathAPI.selectNodeList(theNode, "technical/location/text()");
                    if (nl2.getLength() != 0) {
                        res = nl2.item(0).getNodeValue();
                    } else {
                        nl2 = XPathAPI.selectNodeList(theNode, "general/identifier/entry/text()");
                        if (nl2.getLength() != 0) {
                            res = nl2.item(0).getNodeValue();
                        }
                    }
                    ref = res;
                } catch (Exception e) {
                }


%>





            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td>
<%
    if (ref != null && ref.length() > 0) {
%>
                    <a href="<%=ref%>" class="searchResultsUrl"><%=title%></a>
<%
    } else {
%>
                    <%=title%>
<%
    }
%>
                </td> 
                <td class="searchResultsDate"><%=date.substring(0,date.indexOf('T'))%></td>
            </tr>
<%
    if (description != null && description.length()>0)
    {
%>
            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td colspan="2" class="searchResultsDescription"><b>description:</b><br /><%=description%></td>
            </tr>
<%
    }
%>
<%
    if (contributorRole != null && contributorRole.length()>0 && contributorVCard != null && contributorVCard.length()>0)
    {
%>
            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td colspan="2" class="searchResultsDescription">
                    <b><%=contributorRole%>:</b><br /><%=getVCardN(contributorVCard)%>, <%=getVCardFN(contributorVCard)%>
                </td>
            </tr>
<%
    }
%>
<%
    if (contributorRole != null && contributorRole.length()>0 && contributorVCard != null && contributorVCard.length()>0)
    {
%>
            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td colspan="2" class="searchResultsDescription"><b>VCARD:</b><br /><%=contributorVCard%></td>
            </tr>
<%
    }
%>






<%
            currentResultCounter++;
            } catch (Exception e) {}
        }
    } catch (Exception e) {}
%>


        </table>
        </div>
    </div>








<%
        }
        else
        {
%>





<div class="container">
    <div>
        <center>
            <p>Nothing found</p>
        </center>
    </div>
</div>







<%
        }
%>
      </center>




<%
    }
%>


</div>



    <%     pageContext.include("/layout/footer.jsp"); %> </body>


</html>


<%!
    public String getVCardFN(String vcardString) {

        try {
            DomParser parser = new DomParser();
            Document document = new DocumentImpl();
            parser.parse(new StringReader(vcardString), document);

            AddressBook addressBook = new AddressBook(document);
            for (Iterator vcards = addressBook.getVCards(); vcards.hasNext();) {
                VCard vcard = (VCard) vcards.next();
                FN fn = (FN) vcard.getTypes("FN").next();
                return fn.get();
//                System.out.println(fn.get() + ":");
//                for (Iterator tels = vcard.getTypes("TEL"); tels.hasNext();) {
//                    TEL tel = (TEL) tels.next();
//                    if (((TEL.Parameters) tel.getParameters()).containsTYPE(TEL.Parameters.TYPE_CELL)) {
//                        System.out.println("  Tel (gsm): " + tel.get());
//                    } else {
//                        System.out.println("  Tel      : " + tel.get());
//                    }
//                }
//                for (Iterator emails = vcard.getTypes("EMAIL"); emails.hasNext();) {
//                    EMAIL email = (EMAIL) emails.next();
//                    System.out.println("  E-Mail   : " + email.get());
//                }
//                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVCardN(String vcardString) {

        try {
            DomParser parser = new DomParser();
            Document document = new DocumentImpl();
            parser.parse(new StringReader(vcardString), document);

            AddressBook addressBook = new AddressBook(document);
            for (Iterator vcards = addressBook.getVCards(); vcards.hasNext();) {
                VCard vcard = (VCard) vcards.next();
                N n = (N) vcard.getTypes("N").next();
                return n.getFamily();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
%>








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
<%@ page import="java.util.Vector" %>
<%@ page import="java.io.*" %>
<%@ page import="net.sf.vcard4j.java.type.N" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="javax.xml.transform.*" %>
<%@ page import="javax.xml.transform.dom.*" %>
<%@ page import=" javax.xml.transform.stream.*" %>

<%! static SqiSessionManagementStub sqiSessionStub; %>
<%! static SqiTargetStub sqiStub; %>
<%! String result; %>
<%! String metadata_temp; %>
<%! String result_protocol; %>
<%! String query; %>
<%! String query_protocol; %>
<%! String sessionId; %>
<%! static GetTotalResultsCountResponse countResponse = null; %>
<%! static SynchronousQueryResponse synchronousQueryResponse = null; %>

<%

	String format, language;
	int resultSize, startResult;
	Vector metadataFormats = new Vector();
	
	try{
        if (request.getParameter("query") != null) query = request.getParameter("query");
        else{ 
                query = null;
                countResponse = null;
                synchronousQueryResponse = null;
        }
	}catch(Exception e){
	        query = null;
	        countResponse = null;
	        synchronousQueryResponse = null; 
	}
	resultSize = 10;
    startResult = 1;    
    
    try {
        if (request.getParameter("next") != null)
            startResult = Integer.parseInt(request.getParameter("start_result")) + resultSize;
    } catch (Exception e) {
    }
    try {
        if (request.getParameter("previous") != null)
            startResult = Integer.parseInt(request.getParameter("start_result")) - resultSize;
    } catch (Exception e) {
    }

    String axis2_url = PropertiesManager.getInstance().getProperty("axis2.url");
    if (axis2_url == null)
        axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/services";

    if (query != null && query.length() > 0) {
        try {
        	sessionId = createAnonymousSession(axis2_url +  "/SqiSessionManagement");
			sqiStub = new SqiTargetStub(axis2_url + "/SqiTarget");
			language = "plql1";
			setQueryLanguage(sessionId, language);
			setResultSetSize(sessionId, resultSize);
			format = "lom";
			setResultSetFormat(sessionId, format);

			result = query(sessionId, query, startResult);
			
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
      <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
      pageContext.include("/layout/headLinks.jsp");
%>

	<script language="Javascript">
	
	function showMetadata(index){
	    document.getElementById("showMetadata").innerHTML = "<textarea style=\"width: 100%;\" rows=\"50\">"+xmlToString((Node)metadataFormats())+"</textarea>";
	}
	</script>

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
                                  <tr>
                                      <td align="center"><A href="index.jsp?query=http&search=search">Show all the content</A></td>
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
    if (synchronousQueryResponse != null) {
%>



  <center>
  
<%
        int nbResults = countResults(sessionId, query);
        if (nbResults > 0)
        {
%>





<div class="container" style="width:85%;">
    <div>
        <center>
            <p>Showing results <%=startResult%> to <%=Math.min(resultSize + startResult - 1, countResponse.getGetTotalResultsCountReturn())%> of <%=countResponse.getGetTotalResultsCountReturn()%>.</p>
            <form action="index.jsp">
                <input type="hidden" name="start_result" value="<%=startResult%>" />
                <input type="hidden" name="query" value="<%=query != null ? StringEscapeUtils.escapeHtml(query) : ""%>" />
                <%if(startResult>1){ %>
                <input type="submit" name="previous" value="<< previous" />
                <%}
                if (startResult<nbResults-resultSize){%>
                <input type="submit" name="next" value="next >>" />
                <%}%>
            </form>
        </center>

</div>
<table class="searchResults" cellpadding="0" cellspacing="0">

<%

    StringReader stringReader = new StringReader(result);
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
                    nl2 = XPathAPI.selectNodeList(theNode, "identifier/entry/text()");
                    title = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }

                String description = "";
                try {
                    nl2 = XPathAPI.selectNodeList(theNode, "description/string/text()");
                    description = nl2.item(0).getNodeValue();
                } catch (Exception e) {
                }
            

%>
            <textarea style="width: 100%;" rows="50"><%=xmlToString(theNode)%>"</textarea>
            
            </table>
            </div>
            </div>

            <%
                    } catch (Exception e) { }}
                        
                } catch (Exception e) {
                }}
            %>
                  </center>




<%
	closeSession(sessionId);
    }
%>


</div>



    <%     pageContext.include("/layout/footer.jsp"); %> </body>


</html>


<%!
   /* public String getVCardFN(String vcardString) {

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
    }*/

    /*public String getVCardN(String vcardString) {

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
    }*/
    
    //Copy of functions from SqiTest
    
    public static String createAnonymousSession(String target){
		try {
			sqiSessionStub = new SqiSessionManagementStub(target);
			CreateAnonymousSession createASession = new CreateAnonymousSession();
			CreateAnonymousSessionResponse sessionResponse = sqiSessionStub.createAnonymousSession(createASession);
			String sessionId = sessionResponse.getCreateAnonymousSessionReturn();
			return sessionId;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
	}
	
	public static void setQueryLanguage(String sessionId, String language) throws RemoteException, _SQIFaultException {
		SetQueryLanguage queryLanguage = new SetQueryLanguage();
		queryLanguage.setQueryLanguageID(language);
		queryLanguage.setTargetSessionID(sessionId);
		sqiStub.setQueryLanguage(queryLanguage);

	}
	
	public static void setResultSetSize(String sessionId, int resultSize) throws RemoteException, _SQIFaultException {
		SetResultsSetSize resultsSetSize = new SetResultsSetSize();
		resultsSetSize.setResultsSetSize(resultSize);
		resultsSetSize.setTargetSessionID(sessionId);
		sqiStub.setResultsSetSize(resultsSetSize);
	}
	
	public static void setResultSetFormat(String sessionId, String format) throws RemoteException, _SQIFaultException {
		SetResultsFormat resultsSetFormat = new SetResultsFormat();
		resultsSetFormat.setResultsFormat(format);
		resultsSetFormat.setTargetSessionID(sessionId);
		sqiStub.setResultsFormat(resultsSetFormat);
	}
	
	public static String query(String sessionId, String query, int startResult) throws RemoteException, _SQIFaultException {
		SynchronousQuery syncQuery = new SynchronousQuery();
		syncQuery.setQueryStatement(query);
		syncQuery.setStartResult(startResult);
		syncQuery.setTargetSessionID(sessionId);
		
		synchronousQueryResponse = sqiStub.synchronousQuery(syncQuery);
		String synchronousQueryReturn = synchronousQueryResponse.getSynchronousQueryReturn();
		return synchronousQueryReturn;
		
	}
	
	public static int countResults(String sessionId, String query) throws RemoteException, _SQIFaultException {
		GetTotalResultsCount getTotalResultsCount = new GetTotalResultsCount();
		getTotalResultsCount.setQueryStatement(query);
		getTotalResultsCount.setTargetSessionID(sessionId);
		countResponse = sqiStub.getTotalResultsCount(getTotalResultsCount);
		return countResponse.getGetTotalResultsCountReturn();
	}
	
	public static String xmlToString(Element node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void closeSession(String sessionId){
	       if(!sessionId.equals("")) {
	           DestroySession destroySession = new DestroySession();
	           destroySession.setSessionID(sessionId);
	           try {
	               sqiSessionStub.destroySession(destroySession);
	           } catch (RemoteException e) {
	               e.printStackTrace();
	           } catch (_SQIFaultException e) {
	               e.printStackTrace();
	           }
	           finally {
	               sessionId = "";
	           }
	       }
	   }
	
%>








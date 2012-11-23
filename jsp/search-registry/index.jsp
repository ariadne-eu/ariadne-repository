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
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.apache.xerces.dom.DocumentImpl" %>
<%@ page import="net.sf.vcard4j.java.type.N" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="javax.xml.transform.*" %>
<%@ page import="javax.xml.transform.dom.*" %>
<%@ page import=" javax.xml.transform.stream.*" %>
<%@ page import="org.ariadne.config.PropertiesManager"%>
<%@ page import="org.ariadne_eu.utils.registry.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
	String logSystemDir = PropertiesManager.getInstance().getProperty("repository.log4j.directory");
	if (logSystemDir.compareTo("")==0){
		response.sendRedirect("../init/index.jsp");
	}
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



<%@page import="org.ariadne_eu.utils.config.RepositoryConstants"%><html>


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


        <table class="searchResults" cellpadding="0" cellspacing="0">

<%

    	Results results = new Results();
		results.parseXMLResults(result);
		List<MetadataCollection> list = results.getResults();
	
        int currentResultCounter = 0;
        for (int i = 0; i < list.size(); i++)
        {
            MetadataCollection metadataCollection = list.get(i);

%>			
            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td>

                    <b>Identifier: </b><%=metadataCollection.getIdentifier().getEntry()%>

                </td><td/>                 
            </tr>
<%
    		if (metadataCollection.getDescription().getString() != null && metadataCollection.getDescription().getString().length()>0)
   			{
%>
            <tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
                <td colspan="2" class="searchResultsDescription"><b>Description:</b><br /><%=metadataCollection.getDescription().getString()%></td>
            </tr>
<%
    		}
%>


<%
			List<TargetDescription> targets = metadataCollection.getTarget();
            for (int node=0;node<targets.size();node++){
					TargetDescription targetDescription = targets.get(node);					
%>
<%
					if (targetDescription.getIdentifier().getEntry() != null && targetDescription.getIdentifier().getEntry().length()>0)
					{				
%>
					<tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
						<td colspan="1" class="searchResultsDescription" align="right"><b>Target <%=node%>:</b>
					</td><td/></tr>
					<tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>">
						<td colspan="1"/><td colspan="1" class="searchResultsDescription"><b>Entry: </b><%=targetDescription.getIdentifier().getEntry()%>				
<%					}%>
<%
					if (targetDescription.getIdentifier().getCatalog() != null && targetDescription.getIdentifier().getCatalog().length()>0)
					{				
%>					
						<br/><b>Catalog :</b> <%=targetDescription.getIdentifier().getCatalog()%>				
<%					}%>
<%
					if (targetDescription.getLocation() != null && targetDescription.getLocation().length()>0)
					{				
%>					
						<br/><b>Location :</b> <a href="<%=targetDescription.getLocation()%>"><%=targetDescription.getLocation()%></a>						
<%					}%>
<%
					if ((targetDescription.getProtocolIdentifier().getEntry() != null && targetDescription.getProtocolIdentifier().getEntry().length()>0) && (targetDescription.getProtocolIdentifier().getCatalog() != null && targetDescription.getProtocolIdentifier().getCatalog().length()>0))
					{	
						try{
							query_protocol = "(protocol.identifier.entry = \""+targetDescription.getProtocolIdentifier().getEntry()+"\") and (protocol.identifier.catalog= \""+targetDescription.getProtocolIdentifier().getCatalog()+"\")";	
							
							sqiStub = new SqiTargetStub(axis2_url + "/SqiTarget");
							int startResult_protocol=1;
							language = "plql1";
							setQueryLanguage(sessionId, language);
							setResultSetSize(sessionId, resultSize);
							format = "lom";
							setResultSetFormat(sessionId, format);
	
							result_protocol = query(sessionId, query_protocol, startResult_protocol);
							
							targetDescription.parseXMLProtocol(result_protocol);
						if (targetDescription.getProtocol().getName() != null && targetDescription.getProtocol().getName().length()>0)
						{%>					
								<br/><b>Protocol Name :</b> <%=targetDescription.getProtocol().getName()%>				
<%						}
						if (targetDescription.getProtocol().getProtocolDescriptionBindingNamespace() != null && targetDescription.getProtocol().getProtocolDescriptionBindingNamespace().length()>0)
						{%>					
								<br/><b>Protocol Description Binding Name Space :</b> <%=targetDescription.getProtocol().getProtocolDescriptionBindingNamespace()%>	
<%						}	
								
						if (targetDescription.getProtocol().getProtocolDescriptionBindingLocation() != null && targetDescription.getProtocol().getProtocolDescriptionBindingLocation().length()>0)
						{%>					
								<br/><b>Protocol Description Binding Location :</b> <%=targetDescription.getProtocol().getProtocolDescriptionBindingLocation()%>	
<%						}
						}catch(Exception e){}
				
					}


%>
					</td></tr>					
<%
				}%><tr class="searchResultsRow<%=(currentResultCounter%2==1) ? "Odd" : "Even"%>"><td><%out.println("<A href=\"showMetadata.jsp?query&#61;metadatacollection.identifier.entry%3D&#34;"+metadataCollection.getIdentifier().getEntry()+"&#34;&#38;search&#61;search\">Show all metadata</A>");%></td><td align="right"><A href=<%
				
				if (PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REG_AUTH).contains("openid")){
					out.println("\"../admin-ariadne-registry/updateMetadata.jsp?id="+metadataCollection.getIdentifier().getEntry()+"&getOrPublishField=get\"");
				}else{
					out.println("\"../admin/updateMetadata.jsp?id="+metadataCollection.getIdentifier().getEntry()+"&getOrPublishField=get\"");
				}
				%>>Edit Metadata</A></td></tr><tr><td/></tr>
<%				
				currentResultCounter++;

        }%>


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
	closeSession(sessionId);
    }
%>


</div>



    <%     pageContext.include("/layout/footer.jsp"); %> </body>


</html>


<%!
    /*public String getVCardFN(String vcardString) {

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








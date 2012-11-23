<?xml version="1.0" encoding="UTF-8" ?>
<%@page contentType="text/xml; charset=UTF-8"%>
<%@page import="be.cenorm.www.*" %>
<%@page import="be.cenorm.www._SQIFaultException"%>
<%@page import="be.cenorm.www.SqiTargetStub"%>
<%@page import="be.cenorm.www.SqiSessionManagementStub"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="uiuc.oai.*" import="java.util.StringTokenizer" import="java.util.Vector"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.net.URLEncoder"%>
<%@page import="org.ariadne.exception.IllegalArgException"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.ariadne_eu.utils.registry.query.Query"%>
<%@page import="org.ariadne_eu.utils.registry.Results"%>
<%@page import="java.util.List"%>
<%@page import="org.ariadne_eu.utils.registry.MetadataCollection"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.File"%>
<%@page import="com.sun.syndication.feed.synd.*"%>
<%@page import="com.sun.syndication.io.SyndFeedOutput"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.Writer"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.axis2.context.ConfigurationContext"%>
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

<%// Create file 
		DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
		try {
            String feedType = "rss_2.0";
            String fileName = application.getRealPath("rss/"+"reportStateSQI.xml");
			
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);

            feed.setTitle("Availability of SQI-targets");
            feed.setLink(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rss/reportState.xml");
            feed.setDescription("This webfeed notifies the last state of the targets in the registry.");

            List entries = new ArrayList();
            SyndEntry entry;
            SyndContent description;
            
            String query_string="metadataCollection.target.targetDescription.protocolidentifier.entry=\"sqi-v1\"";
            //String query_string = "http";
    	    String axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    	    sessionId = createAnonymousSession(axis2_url +  "/services/SqiSessionManagement");
			sqiStub = new SqiTargetStub(axis2_url + "/services/SqiTarget");
			String language = "plql1";
			setQueryLanguage(sessionId, language);
			setResultSetSize(sessionId, 100);
			String format = "lom";
			setResultSetFormat(sessionId, format);

			String result = query(sessionId, query_string, 1);
    	    Results results = new Results();
    	    results.parseXMLResults(result);
    		List<MetadataCollection> list = results.getResults();
    		for (int i = 0; i < list.size(); i++)
    	    {
    	        MetadataCollection metadataCollection = list.get(i);
    	        boolean contentSQI = false;
    			for (int iterator=0;iterator<metadataCollection.getTarget().size();iterator++){
    				if (metadataCollection.getTarget().get(iterator).getProtocolIdentifier().getEntry().contains("sqi")){
    					contentSQI = true;
    					entry = new SyndEntryImpl();
    					description = new SyndContentImpl(); 
    					try{
    						
    						Calendar timeInit = Calendar.getInstance();
    						int minuteInit= timeInit.get(Calendar.MINUTE);
    						int secondInit= timeInit.get(Calendar.SECOND);    						
    			            entry.setTitle(metadataCollection.getIdentifier().getEntry());				
    			            entry.setLink(axis2_url+"/search/index.jsp?query=\""+metadataCollection.getIdentifier().getEntry()+"\"");
    			            entry.setPublishedDate(DATE_PARSER.parse(Calendar.getInstance().get(Calendar.YEAR)+"-"+Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
    			            sqiStub = new SqiTargetStub(metadataCollection.getTarget().get(iterator).getLocation());    			        
    						Calendar timeEnd = Calendar.getInstance();
    						int minuteEnd = timeEnd.get(Calendar.MINUTE);
    						int secondEnd= timeEnd.get(Calendar.SECOND);	    						   			            
    			            description.setValue("OK - Time Response for creating a SQI client = "+((minuteEnd-minuteInit)*60)+(secondEnd-secondInit)+" seconds");
    			            entry.setDescription(description);    					    
    					}catch(Exception e){
    						description.setType("text/plain");
    						description.setValue("Exception: "+e.getMessage());
    			            entry.setDescription(description);			
    					}	
    					entries.add(entry);
    					
    				}
    			}
    	    }       

            feed.setEntries(entries);

            Writer writer = new FileWriter(fileName);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed,writer);
            writer.close();

            out.println("The feed has been written to the file ["+fileName+"]");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            out.println("ERROR: "+ex.getMessage());
        }

		response.sendRedirect("reportStateSQI.xml");
	//}

%>

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

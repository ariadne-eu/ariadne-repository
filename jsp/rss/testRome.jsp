<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/xml; charset=UTF-8"%>
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


<%// Create file 
		DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
		try {
            String feedType = "rss_2.0";
            String fileName = application.getRealPath("rss/"+"reportState.xml");

            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);

            feed.setTitle("Availability of OAI-targets");
            feed.setLink(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/rss/reportState.xml");
            feed.setDescription("This webfeed notifies the last state of the targets in the registry.");

            List entries = new ArrayList();
            SyndEntry entry;
            SyndContent description;
			
            String query_string="http";
    	    String axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    	    String result = Query.doQueryLucene();
    	    Results results = new Results();
    	    results.parseXMLResults(result);
    		List<MetadataCollection> list = results.getResults();
    		for (int i = 0; i < list.size(); i++)
    	    {
    	        MetadataCollection metadataCollection = list.get(i);
    	        boolean contentOAI = false;
    			for (int iterator=0;iterator<metadataCollection.getTarget().size();iterator++){
    				if (metadataCollection.getTarget().get(iterator).getProtocolIdentifier().getEntry().contains("oai-pmh")){
    					contentOAI = true;
    					entry = new SyndEntryImpl();
    					description = new SyndContentImpl(); 
    					try{
    						OAIRepository oairepository = new OAIRepository();
    						Calendar timeInit = Calendar.getInstance();
    						int minuteInit= timeInit.get(Calendar.MINUTE);
    						int secondInit= timeInit.get(Calendar.SECOND);    						
    			            entry.setTitle(metadataCollection.getIdentifier().getEntry());				
    			            entry.setLink(axis2_url+"/search/index.jsp?query=\""+metadataCollection.getIdentifier().getEntry()+"\"");
    			            entry.setPublishedDate(DATE_PARSER.parse(Calendar.getInstance().get(Calendar.YEAR)+"-"+Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
    						oairepository.setBaseURL(metadataCollection.getTarget().get(iterator).getLocation());
    						Calendar timeEnd = Calendar.getInstance();
    						int minuteEnd = timeEnd.get(Calendar.MINUTE);
    						int secondEnd= timeEnd.get(Calendar.SECOND);	    						   			            
    			            description.setValue("OK - Time Response = "+((minuteEnd-minuteInit)*60)+(secondEnd-secondInit)+" seconds");
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

		response.sendRedirect("reportState.xml");
	//}

%>

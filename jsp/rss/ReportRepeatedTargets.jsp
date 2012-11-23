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
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Enumeration"%>


<%// Create file 
		DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
		try {
            String feedType = "rss_2.0";
            String fileName = application.getRealPath("rss/"+"reportRepeatedTargets.xml");

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
    	    String result = Query.doQuery(query_string, axis2_url, 100);
    	    Results results = new Results();
    	    results.parseXMLResults(result);
    		List<MetadataCollection> list = results.getResults();
    		Hashtable urls = new Hashtable();
    		Hashtable identifiers = new Hashtable();
    		for (int i = 0; i < list.size(); i++)
            {
                MetadataCollection metadataCollection = list.get(i);
                for (int j = 0; j < metadataCollection.getTarget().size(); j++)
                    {
                        if (urls.get(metadataCollection.getTarget().get(j).getLocation())!=null){
                                urls.put(metadataCollection.getTarget().get(j).getLocation(), ((Integer)urls.get(metadataCollection.getTarget().get(j).getLocation()))+1);
                                identifiers.put(metadataCollection.getTarget().get(j).getLocation(), ((String)identifiers.get(metadataCollection.getTarget().get(j).getLocation()))+"-Catalog:"+metadataCollection.getIdentifier().getCatalog()+"\n-Identifier:"+metadataCollection.getIdentifier().getEntry()+"\n");
                        }else{
                                urls.put(metadataCollection.getTarget().get(j).getLocation(), 1);
                                identifiers.put(metadataCollection.getTarget().get(j).getLocation(), "Catalog: "+metadataCollection.getIdentifier().getCatalog()+"\n-Identifier:"+metadataCollection.getIdentifier().getEntry()+"\n");
                        }
                    }
            }

    		
    		Enumeration e = urls.keys();

    		while( e. hasMoreElements() ){
    			String url = (String)e.nextElement();
    			if ((Integer)urls.get(url)>1){
    				entry = new SyndEntryImpl();
					description = new SyndContentImpl(); 
					entry.setTitle(url);
					description.setValue((Integer)urls.get(url)+" times repeated. Identifiers: "+identifiers.get(url));
		            entry.setDescription(description);
		            entries.add(entry);
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

		response.sendRedirect("reportRepeatedTargets.xml");
	//}

%>



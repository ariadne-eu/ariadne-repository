<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="uiuc.oai.*" import="java.util.StringTokenizer" import="java.util.Vector"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.ariadne.exception.IllegalArgException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.ariadne_eu.utils.registry.query.Query"%>
<%@ page import="org.ariadne_eu.utils.registry.Results"%>
<%@ page import="java.util.List"%>
<%@ page import="org.ariadne_eu.utils.registry.MetadataCollection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="java.io.BufferedWriter"%>
<%@ page import="java.io.File"%>
<%@ page import="com.sun.syndication.feed.synd.*"%>
<%@ page import="com.sun.syndication.io.SyndFeedOutput"%>

<%@ page import="java.io.FileWriter"%>
<%@ page import="java.io.Writer"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>


<%
        DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
        try {
            
            String query_string="http";
            String axis2_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String result = Query.doQuery(query_string, axis2_url, 100);
            Results results = new Results();
            results.parseXMLResults(result);
            List<MetadataCollection> list = results.getResults();
            Hashtable catalogs = new Hashtable();
            Hashtable protocols = new Hashtable();
            for (int i = 0; i < list.size(); i++)
            {
                MetadataCollection metadataCollection = list.get(i);
                if (catalogs.get(metadataCollection.getIdentifier().getCatalog())!=null) catalogs.put(metadataCollection.getIdentifier().getCatalog(), 
((Integer)catalogs.get(metadataCollection.getIdentifier().getCatalog()))+1);                    
                else catalogs.put(metadataCollection.getIdentifier().getCatalog(), 1);
                for (int j = 0; j < metadataCollection.getTarget().size(); j++)
                {
                	if (protocols.get(metadataCollection.getTarget().get(j).getProtocolIdentifier().getEntry())!=null) 
protocols.put(metadataCollection.getTarget().get(j).getProtocolIdentifier().getEntry(), 
((Integer)protocols.get(metadataCollection.getTarget().get(j).getProtocolIdentifier().getEntry()))+1);
                	else protocols.put(metadataCollection.getTarget().get(j).getProtocolIdentifier().getEntry(), 1);
                }
            }

            
                /*String url = (String)e.nextElement();
                if ((Integer)catalogs.get(url)>1){
  	              entry = new SyndEntryImpl();
                  description = new SyndContentImpl();
                  entry.setTitle(url);
                  description.setValue((Integer)catalogs.get(url)+" times repeated. Identifiers: "+identifiers.get(url));
                  entry.setDescription(description);
                  entries.add(entry);
             	}*/


            //feed.setEntries(entries); 

            /*Writer writer = new FileWriter(fileName);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed,writer);
            writer.close();

            out.println("The feed has been written to the file ["+fileName+"]");*/
        

                //response.sendRedirect("reportRepeatedTargets.xml");
        //}

%>
                
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Catalog');
<%

		Enumeration e = protocols.keys();
		while(e.hasMoreElements()){
%>      
			data.addColumn('number', '<% out.print((String)e.nextElement());%>');
<%		}
		Enumeration catalog_e = catalogs.keys();%>
		data.addRows(<% out.print(catalogs.size());%>);
<%
	
		int row = 0;
		while(catalog_e.hasMoreElements()){
			e = protocols.keys();
			int column = 0;
			String catalog = (String)catalog_e.nextElement();%>
			data.setValue(<% String catalog_text = Integer.toString(row)+", "+Integer.toString(column)+", '"+catalog + "');";
							out.print(catalog_text);%>
							
	<%
			while(e.hasMoreElements()){
				column++;
				String protocol = (String)e.nextElement();
				
				String query_string2 = "metadatacollection.target.targetdescription.protocolidentifier.entry=\"";
				query_string2 += protocol;
				query_string2 += "\"";
				query_string2 += " and metadatacollection.identifier.catalog=";
				query_string2 += "\"";
				query_string2 += catalog+"\"";
				result = Query.doQuery(query_string2, axis2_url, 100);
	            results = new Results();
	            results.parseXMLResults(result);
	            int length = results.getResults().size();%>
	    		data.setValue(<% String text = row+","+ column+","+ length+");";
	    				out.print(text);%>
	    		
	    		<%
	    		
			}
			row++;
		}
		}
    catch (Exception ex) {
    	ex.printStackTrace();
        out.println("ERROR: "+ex.getMessage());
    }%>
        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 1000, height: 600, title: 'Targets per catalog',
                          hAxis: {title: 'Catalog', titleTextStyle: {color: 'red'}}
                         });
      }
    </script>
    
    <%
      pageContext.include("/layout/headLinks.jsp");
	%>
  </head>
                                
  <body>
	<%
	    pageContext.include("/layout/header.jsp");
	%>
    <div id="chart_div"></div>
  </body>
</html>
                
                        

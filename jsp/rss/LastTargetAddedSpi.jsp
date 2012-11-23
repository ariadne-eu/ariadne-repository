<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/xml; charset=UTF-8"%>
<%@ page import="uiuc.oai.*" import="java.util.StringTokenizer" import="java.util.Vector"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.net.URLEncoder"%>
<%@page import="org.ariadne.exception.IllegalArgException"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.ariadne_eu.utils.registry.query.Query"%>
<%@page import="org.ariadne_eu.utils.registry.Results"%>
<%@page import="java.util.List"%>
<%@page import="org.ariadne_eu.utils.registry.MetadataCollection"%><rss version="2.0">
  <channel>
    <title><%=request.getContextPath().replaceAll("/","").toUpperCase()%></title>
    <link><%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>/rss/TargetListRss.jsp</link>
 <ttl>1</ttl>   
<description>This webfeed notifies the last Targets, which support SPI, added to the registry.</description><%
    response.setContentType("text/xml");
    String query_string="date.insert=";
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
			if (metadataCollection.getTarget().get(iterator).getProtocolIdentifier().getEntry().contains("spi")){
				contentOAI = true;
			}
		}
		if (contentOAI){
		%>   <item>
		
		     <title><%=metadataCollection.getIdentifier().getEntry()%></title>
		     <link><%=axis2_url+"/search/index.jsp?query=\""+metadataCollection.getIdentifier().getEntry()+"\""%></link>
			 <pubDate><%=Query.doQueryLuceneDate(metadataCollection.getIdentifier().getEntry())%></pubDate>
		     <description><%=metadataCollection.getDescription().getString()%></description></item><%
		}        
    }

%></channel>
</rss>
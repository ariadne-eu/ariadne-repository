

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.ariadne.config.PropertiesManager"%>

<%@page import="java.io.File"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne Registry - Web Installer</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="css/install.css" type="text/css" />
<script type="text/javascript">

function check()
{
	// form validation check
	var f = document.form;
	var path = f.fileSystemDir.value;
	if ( path == '') {
		alert('Please enter a directory to store the data to');
		f.fileSystemDir.focus();
		return false;
	}

	return true;
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
<FORM METHOD=POST name=form id=form ACTION="indexHandler.jsp" onsubmit="return check();">
<input type="hidden" name="database" value="database">


<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-off">Pre-installation</div>
<div class="step-off">Store selection</div>
<div class="step-on">Store properties</div>
<div class="step-off">Index properties</div>
<div class="step-off">Admin</div>
<div class="step-off">Finish</div>

</div>

<div id="right">

<div id="step">Connection Information</div>

<div class="far-right">
<input name="Button2" type="submit" class="button" value="Next >>" />
</div>
<div class="clr"></div>

<h1>What directory should be used on the File System</h1>

<%String logSystemDir = PropertiesManager.getInstance().getProperty("repository.log4j.directory");
String repository = PropertiesManager.getInstance().getProperty("repository.log4j.filename");
String indexSystemDir = PropertiesManager.getInstance().getProperty("search.lucene.indexdir");
String fileSystemDir = PropertiesManager.getInstance().getProperty("mdstore.spifs.dir");%>

<div class="install-text">
    			<p>To use the File System to store the metadata you have to provide the following information:</p>
  	   			<p>* The targetted directory on the File System</p>
       			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
  	   			<div class="form-block">
  	     			<table class="content2">
  		  			<tr>
  		    			<td>Storing Log Location :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="logSystemDir" value="<%if (logSystemDir.compareTo("")!=0) out.println (logSystemDir); else out.println(application.getRealPath("installation/logs")+ File.separator);%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>Name of logs :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="nameLogs" value="<%if (repository.compareTo("")!=0) out.println (repository); else out.println("repository");%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>Storing Index Location :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="indexSystemDir" value="<%if (indexSystemDir.compareTo("")!=0) out.println (indexSystemDir); else out.println(application.getRealPath("installation/index")+ File.separator);%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>Storing Metadata Location :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="fileSystemDir" value="<%if (fileSystemDir.compareTo("")!=0) out.println (fileSystemDir); else out.println(application.getRealPath("installation/store")+ File.separator);%>"/></td>
  		    		</tr>
		  	     	</table>
		  	     	<input type="hidden" name="cntstore" value="<%=request.getParameter("cntstore")%>>"/>
		  	     	
  				</div>
			</div>

<div class="clr"></div>
</div>
<div class="clr"></div>
</div>
<div class="clr"></div>

<input type="hidden" id="previous" name="previous" value="harvestToDisk"/>

<div class="ctr">
Ariadne Foundation<br />
<a href="http://www.ariadne-eu.org/" target="_blank">ARIADNE</a> is an European Association open to the World, for Knowledge Sharing and Reuse.<br> The core of the ARIADNE infrastructure is a distributed network of learning repositories.
</div>
</form>
</body>
</html>
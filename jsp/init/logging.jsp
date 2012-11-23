
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.ariadne.config.PropertiesManager"%>
<%@page import="java.io.File"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne OAI Harvester - Web Installer</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="css/install.css" type="text/css" />

<script type="text/javascript">

function check()
{
	// form validation check
	var formValid=false;
	var f = document.form;
	var path = f.logFilesDir.value;
	if ( path == '') {
		alert('Please enter a valid path');
		f.logFilesDir.focus();
		formValid=false;
	}
	else{
		formValid=true;
	}

	return formValid;
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
<FORM METHOD=POST name=form id=form ACTION="finish.jsp" onsubmit="return check();">
<input type="hidden" name="database" value="database">


<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-off">Pre-installation</div>
<div class="step-off">Store selection</div>
<div class="step-off">Store properties</div>
<div class="step-off">Index properties</div>
<div class="step-on">Admin</div>
<div class="step-off">Finish</div>

</div>

<%String user = PropertiesManager.getInstance().getProperty("repository.username");
String pass = PropertiesManager.getInstance().getProperty("repository.password");%>

<div id="right">

<div id="step">Logging Information</div>

<div class="far-right">
<input name="Button2" type="submit" class="button" value="Next >>" />
</div>
<div class="clr"></div>

<h1>Where to put the log files?</h1>


<div class="install-text">
  	   			<p>Enter the directory where the logs should be saved.</p>
     			<p><font color=FF0000><b>
                </b></font></p>
  			</div>
			<div class="install-form">
  	   			<div class="form-block">
  	     			<table class="content2">
  		  			<tr>
  		    			<td>User :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="user" value="<%if (user.compareTo("")!=0) out.println (user); else out.println("user");%>"/></td>
  		    		</tr>
  		    		<tr>
  		    			<td>Password :<br/><input class="inputbox" style="width:100%;" type="text" class="inputboxadd" name="pass" value="<%if (pass.compareTo("")!=0) out.println (pass); else out.println("pass");%>"/></td>
  		    		</tr>
		  	     	</table>
  				</div>
			</div>
			<input type="hidden" name="cntstore" value="<%=request.getParameter("cntstore")%>"/>
			<input type="hidden" name="logSystemDir" value="<%=request.getParameter("logSystemDir")%>"/>
  	     	<input type="hidden" name="indexSystemDir" value="<%=request.getParameter("indexSystemDir")%>"/>
  	     	<input type="hidden" name="fileSystemDir" value="<%=request.getParameter("fileSystemDir")%>"/>
  	     	<input type="hidden" name="nameLogs" value="<%=request.getParameter("nameLogs")%>"/>
  	     	<input type="hidden" name="handler" value="<%=request.getParameter("handler")%>"/>
		  	<input type="hidden" name="analyzer" value="<%=request.getParameter("analyzer")%>"/>

<div class="clr"></div>
</div>
<div class="clr"></div>
</div>
<div class="clr"></div>


<div class="ctr">
Ariadne Foundation<br />
<a href="http://www.ariadne-eu.org/" target="_blank">ARIADNE</a> is an European Association open to the World, for Knowledge Sharing and Reuse.<br> The core of the ARIADNE infrastructure is a distributed network of learning repositories.
</div>
</form>
</body>
</html>
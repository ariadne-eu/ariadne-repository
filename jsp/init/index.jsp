

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Ariadne Installer - Web Installer</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/install.css" type="text/css" />
<script type="text/javascript">
<!--

//-->
</script>
<%
      pageContext.include("/layout/headLinks.jsp");
%>
  </head>
  <body>
<%
    pageContext.include("/layout/header.jsp");
%>


<div id="ctr" align="center">
<div class="install">
<div id="stepbar">
<div class="step-on">Pre-installation</div>
<div class="step-off">Store selection</div>
<div class="step-off">Store properties</div>
<div class="step-off">Index properties</div>
<div class="step-off">Admin</div>
<div class="step-off">Finish</div>

</div>

<div id="right">

<div id="step">Pre-installation</div>

<div class="far-right">
<input name="Button2" type="submit" class="button" value="Next >>" onclick="window.location='storeSelect.jsp';" />
</div>
<div class="clr"></div>

<h1>You are about to install the OAI Harvester</h1>
<div class="install-text">
Before you proceed you should meet these requirements.
<div class="ctr"></div>

</div>

<div class="install-form">
<div class="form-block">

<table class="content">
<tr>
	<td valign="top" class="item">
	You should know where you want to store the repository metadata.<br><br>
	You should have the necessary information to reach the "store".
	</td>
	

</tr>

</table>

</div>
</div>

<div class="clr"></div>
</div>
<div class="clr"></div>
</div>
<div class="clr"></div>
</div>


<div class="ctr">
Ariadne Foundation<br />
<a href="http://www.ariadne-eu.org/" target="_blank">ARIADNE</a> is an European Association open to the World, for Knowledge Sharing and Reuse.<br> The core of the ARIADNE infrastructure is a distributed network of learning repositories.
</div>

</body>
</html>
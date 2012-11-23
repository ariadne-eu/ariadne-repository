<%--
  - Main stylesheet
  - This is a JSP so it can be tailored for different browser types
  --%>
<%
    // Make sure the browser knows we're a stylesheet
    response.setContentType("text/css");

    String imageUrl = request.getContextPath() + "/images/";

    // Netscape 4.x?
    boolean usingNetscape4 = false;
    boolean usingIE = false;
    String userAgent = request.getHeader( "User-Agent" );
    if(userAgent != null && userAgent.startsWith( "Mozilla/4" ) )
    {
        usingNetscape4 = true;
    }
    if(userAgent != null && userAgent.indexOf("MSIE") != -1)
    {
        usingIE = true;
    }
%>

body {
	margin: 0px;
	padding: 0px;
	color : #333;
	background-color : #FFF;
	font-size : 11px;
	font-family : Arial, Helvetica, sans-serif;
}

#wrapper {
        border: 0px;
        margin: 0px;
        margin-left: auto;
        margin-right: auto;
        padding: 0px;
}

#header {
        background-color: #FFF;
        background-position: right top;
        border-bottom: 4px solid #80B5D7;
}

#mambo {
        position: relative;
        width: 100%;
        background: #003366;
        color : #FFF;
        font-size : 50px;
        background-position: top right;
        margin: 0px;
        padding: 0px;
}

#break {
	height: 50px;
}

form {
    margin: 0px;
}



.button {
	border : solid 1px #cccccc;
	background: #E9ECEF;
	color : #666666;
	font-weight : bold;
	font-size : 11px;
	padding: 4px;
}

.login {
	margin-left: auto;
	margin-right: auto;
	margin-top: 6em;
	padding: 15px;
	border: 1px solid #cccccc;
	width: 429px;
	background: #F1F3F5;
}
	
.login h1 {
	background: url(../images/login_header.png) no-repeat;
	background-position: left top;
	color: #333;
	margin: 0px;
	height: 50px;
	padding: 15px 4px 0 50px;
 	text-align: left;
	font-size: 1.5em;
}

.login p {
	padding: 0 1em 0 1em;
	}
	
.form-block {
	border: 1px solid #cccccc;
	background: #E9ECEF;
	padding-top: 15px;
	padding-left: 10px;
	padding-bottom: 10px;
	padding-right: 10px;
}

.login-form {
	text-align: left;
	float: right;
	width: 60%;
}

.login-text {
	text-align: left;
	width: 40%;
	float: left;
}



/* ======================================= */
.inputlabel {
	font-weight: bold;
	text-align: left;
    font-size:12px;
    padding:3px;
    }

.inputbox {
	width: 150px;
	margin: 0 0 1em 0;
	border: 1px solid #cccccc;
    padding: 3px;
    background: #FFFFFF;
    font-family: Arial,Helvetica,sans-serif;
    font-size:12px;    
    }


input, textarea, select {
    color: #000000;
    font-size: 12px;
    z-index:1;
}

/* ======================================= */




.clr {
    clear:both;
    }

.ctr {
	text-align: center;
}

.version {
	font-size: 0.8em;
}








/* ======================================================================================= */



/*MAMBO CUSTOMIZATIONS*/

.ThemeOfficeMainFolderText{
    font-weight:bold;
    /*font-size:12px;*/
}

.menubackgr {
    background: url(<%=imageUrl%>chromebg.gif) center center repeat-x;
}

.ThemeOfficeMainItemHover,.ThemeOfficeMainItemActive
{
	/*background-color:	#B5CDE8;*/
    background: url(<%=imageUrl%>chromebg2.gif) center center repeat-x;

}



/* ======================================================================================= */



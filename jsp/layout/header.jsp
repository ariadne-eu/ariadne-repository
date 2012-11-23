
<%@page import="org.ariadne.config.PropertiesManager"%>
<%@page import="org.ariadne_eu.utils.config.RepositoryConstants"%>

<div id="wrapper">
    <div id="header">
        <div id="mambo">
            <img alt="Logo" src="<%=request.getContextPath()%>/images/header_text.png" />
        </div>
    </div>
</div>




<%
    boolean isLoggedIn = (request.getSession().getAttribute("login") != null && request.getSession().getAttribute("login").equals("true") && request.getSession().getAttribute("username") != null);
	request.getSession().setAttribute("contextURL",request.getRequestURI());
%>


<table width="100%" class="menubar" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td class="menubackgr">&nbsp;</td>
    <td class="menubackgr">

        <div id="myMenuID"></div>

        <!--* view status-->
        <!--* insert metadata & files-->
        <!--* obtain metadata & files-->
        <!--* search repository-->
        <!--* log in-->
        <!--# reload repository-->
        <!--# log out-->
        <script language="JavaScript" type="text/javascript">
		var myMenu =
		[
			[null,'Status',null,null,'Status',
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/checkin.png" />','Test Status','<%=request.getContextPath()%>/status/',null,''],
<%
                if (isLoggedIn) {
%>
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/config.png" />','Change Configuration','<%=request.getContextPath()%>/admin/changeConfiguration.jsp',null,''],
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/config.png" />','Reload repository','<%=request.getContextPath()%>/admin/reload.jsp',null,''],
                ['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/index.png" />','Recreate Index','<%=request.getContextPath()%>/admin/index-lucene.jsp',null,'Recreate Index'],
                ['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/index.png" />','Optimize Index','<%=request.getContextPath()%>/admin/optimize-lucene.jsp',null,'Optimize Index'],
<%
                }
%>
            ],
			_cmSplit,
			[null,'Insert',null,null,'Insert',
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/document.png" />','Insert File','<%=request.getContextPath()%>/insert/file.jsp',null,''],
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/content.png" />','Insert Metadata','<%=request.getContextPath()%>/insert/metadata.jsp',null,''],
			],
			_cmSplit,
			[null,'Obtain',null,null,'Obtain',
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/document.png" />','Obtain File','<%=request.getContextPath()%>/obtain/file.jsp',null,'Obtain File'],
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/content.png" />','Obtain Metadata','<%=request.getContextPath()%>/obtain/metadata.jsp',null,'Obtain Metadata'],
			],
			_cmSplit,
			[null,'Modify',null,null,'Modify',
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/content.png" />','Modify Metadata','<%=request.getContextPath()%>/admin/updateMetadata.jsp',null,'Modify Metadata'],
			],
			_cmSplit,
			[null,'Search',null,null,'Search',
				['<img src="<%=request.getContextPath()%>/includes/js/ThemeOffice/query.png" />','Search repository','<%=request.getContextPath()%>/search/',null,'Search repository'],
			],
		];
		cmDraw ('myMenuID', myMenu, 'hbr', cmThemeOffice, 'ThemeOffice');
		</script>

    </td>
    <td class="menubackgr" align="right">
    </td>
    <td class="menubackgr" align="right">
<%
        if (!isLoggedIn) {
%>
            <a href="<%=request.getContextPath()%>/login/login.jsp" style="color: #333333; font-weight: bold; padding-right:5px;">LOG IN</a>
<%
        } else {
%>
            <a href="<%=request.getContextPath()%>/login/logout.jsp" style="color: #333333; font-weight: bold; padding-right:5px;">LOG OUT</a>
<%
        }
%>
    </td>
    </tr>
</table>







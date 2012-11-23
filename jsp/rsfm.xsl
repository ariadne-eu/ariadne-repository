<?xml version="1.0" encoding="utf-8"?>

	<!--

		XSL Transform to convert OAI 2.0 responses into XHTML By Christopher
		Gutteridge, University of Southampton
	-->

	<!--

		Copyright (c) 2000-2004 University of Southampton, UK. SO17 1BJ.

		EPrints 2 is free software; you can redistribute it and/or modify it
		under the terms of the GNU General Public License as published by the
		Free Software Foundation; either version 2 of the License, or (at your
		option) any later version. EPrints 2 is distributed in the hope that
		it will be useful, but WITHOUT ANY WARRANTY; without even the implied
		warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
		the GNU General Public License for more details. You should have
		received a copy of the GNU General Public License along with EPrints
		2; if not, write to the Free Software Foundation, Inc., 59 Temple
		Place, Suite 330, Boston, MA 02111-1307 USA
	-->


	<!--

		All the elements really needed for EPrints are done but if you want to
		use this XSL for other OAI archive you may want to make some minor
		changes or additions. Not Done The 'about' section of 'record' The
		'compession' part of 'identify' The optional attributes of
		'resumptionToken' The optional 'setDescription' container of 'set' All
		the links just link to oai_dc versions of records.
	-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:oai="http://www.openarchives.org/OAI/2.0/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:swrc="http://swrc.ontoware.org/ontology#" xmlns:fn="http://www.w3.org/2005/xpath-functions">

	<xsl:output method="html"/>



	<xsl:template name="style"> td.value { vertical-align: top; padding-left: 1em; padding: 3px; }
		td.key { background-color: #e0e0ff; padding: 3px; text-align: right; border: 1px solid
		#c0c0c0; white-space: nowrap; font-weight: bold; vertical-align: top; } .dcdata td.key {
		background-color: #ffffe0; } body { margin: 1em 2em 1em 2em; } h1, h2, h3 { font-family:
		sans-serif; clear: left; } h1 { padding-bottom: 4px; margin-bottom: 0px; } h2 {
		margin-bottom: 0.5em; } h3 { margin-bottom: 0.3em; font-size: medium; } .link { border: 1px
		outset #88f; background-color: #c0c0ff; padding: 1px 4px 1px 4px; font-size: 80%;
		text-decoration: none; font-weight: bold; font-family: sans-serif; color: black; }
		.link:hover { color: red; } .link:active { color: red; border: 1px inset #88f;
		background-color: #a0a0df; } .oaiRecord, .oaiRecordTitle { background-color: #f0f0ff;
		border-style: solid; border-color: #d0d0d0; } h2.oaiRecordTitle { background-color: #e0e0ff;
		font-size: medium; font-weight: bold; padding: 10px; border-width: 2px 2px 0px 2px; margin:
		0px; } .oaiRecord { margin-bottom: 3em; border-width: 2px; padding: 10px; } .results {
		margin-bottom: 1.5em; } ul.quicklinks { margin-top: 2px; padding: 4px; text-align: left;
		border-bottom: 2px solid #ccc; border-top: 2px solid #ccc; clear: left; } ul.quicklinks li {
		font-size: 80%; display: inline; list-stlye: none; font-family: sans-serif; } p.intro {
		font-size: 80%; } <xsl:call-template name="xmlstyle"/>
	</xsl:template>

	<xsl:variable name="identifier"
		select="substring-before(concat(substring-after(/oai:OAI-PMH/oai:request,'identifier='),'&amp;'),'&amp;')"/>

	<xsl:variable name="defaultMetadataPrefix"
		select="document('services/oai?verb=ListMetadataFormats')//oai:metadataPrefix"/>
	<xsl:variable name="variableMetadataPrefix" select="/oai:OAI-PMH/oai:request/@metadataPrefix"/>
	<xsl:variable name="metadataPrefix">
		<xsl:choose>
			<xsl:when test="not($variableMetadataPrefix)">
				<xsl:value-of select="$defaultMetadataPrefix"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$variableMetadataPrefix"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template name="replace-api">
		<xsl:param name="text" select="."/>
		<xsl:choose>
			<xsl:when test="contains($text, '/api/')">
				<xsl:value-of select="substring-before($text, '/api/')"/>/api/xml/<xsl:value-of select="substring-after($text, '/api/')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="get-id">
		<xsl:param name="text" select="."/>
		<xsl:variable name="vCountSeperators" select="string-length($text) - string-length(translate($text,'/',''))"/>
		<xsl:choose>
			<xsl:when test="$vCountSeperators &gt;= 2">
				<!-- recursive call -->
				<xsl:call-template name="get-id">
					<xsl:with-param name="text" select="substring-after(substring-after($text,'/'),'/')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$vCountSeperators = 1">
				<!-- one seperator - text|link -->
					<xsl:value-of select="substring-after($text,'/')"/>
			</xsl:when>
		</xsl:choose>
		
	</xsl:template>
	

	<xsl:template match="/results">
		<html>
			<head>
				<title>Research.fm Results</title>
				<style>
					<xsl:call-template name="style"/>
				</style>
			</head>
			<body>

				<xsl:apply-templates select="rdf:RDF"/>

			</body>
		</html>
	</xsl:template>

	<xsl:template match="rdf:RDF">

		<xsl:choose>
			<xsl:when test="oai:error">
				<h2>OAI Error(s)</h2>
				<p>The request could not be completed due to the following error or errors.</p>
				<div class="results">
					<xsl:apply-templates select="oai:error"/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="results">

					<xsl:apply-templates select="swrc:Person"/>
					<xsl:apply-templates select="swrc:Publication"/>

				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="swrc:Person">
		<xsl:variable name="link">
			<xsl:call-template name="replace-api"><xsl:with-param name="text"><xsl:value-of select="@rdf:about"/></xsl:with-param></xsl:call-template>
		</xsl:variable>
		<xsl:variable name="id"><xsl:call-template name="get-id"><xsl:with-param name="text" select="$link"></xsl:with-param></xsl:call-template></xsl:variable>
		
		<h2 class="oaiRecordTitle"> <a href="/repository/api/xml/authors/{$id}"><xsl:value-of select="$id"/></a> <xsl:text>	</xsl:text> <a href="/repository/api/xml/publications/author/{$id}">publications</a>
		</h2>
		<div class="oaiRecord">
			<xsl:call-template name="metadata"/>
		</div>
	</xsl:template>

	<!-- record object -->

	<xsl:template match="swrc:Pedrson">
		<xsl:variable name="link">
			<xsl:call-template name="replace-api"><xsl:with-param name="text"><xsl:value-of select="@rdf:about"/></xsl:with-param></xsl:call-template>
		</xsl:variable>
		<h2 class="oaiRecordTitle"> <a href="{$link}"><xsl:call-template name="get-id"><xsl:with-param name="text" select="$link"></xsl:with-param></xsl:call-template></a>
		</h2>
		<div class="oaiRecord">
			<xsl:call-template name="metadata"/>
		</div>
	</xsl:template>

	<xsl:template match="swrc:Publication">
		<xsl:variable name="link">
			<xsl:call-template name="replace-api"><xsl:with-param name="text"><xsl:value-of select="@rdf:about"/></xsl:with-param></xsl:call-template>
		</xsl:variable>
		<xsl:variable name="id"><xsl:call-template name="get-id"><xsl:with-param name="text" select="$link"></xsl:with-param></xsl:call-template></xsl:variable>
		
		<h2 class="oaiRecordTitle"> <a href="/repository/api/xml/publications/{$id}"><xsl:value-of select="$id"/></a>
		</h2>
		<div class="oaiRecord">
			<xsl:call-template name="metadata"/>
		</div>
	</xsl:template>

	<xsl:template name="metadata"> &#160; <div class="metadata">
			<xsl:apply-templates select="*"/>
		</div>
	</xsl:template>


	<xsl:template match="*[not(*)]">
		<tr>
			<table>
				<td class="key">
					<xsl:value-of select="local-name()"/>
					<xsl:apply-templates select="@*"/>
				</td>
				<td class="value">
					<xsl:value-of select="."/>
				</td>
			</table>
		</tr>
	</xsl:template>

	<xsl:template match="@*"> (<xsl:value-of select="local-name()"/>:<xsl:value-of select="."/>)<br/>
	</xsl:template>

	<xsl:template match="*">
		<div class="dcdata">

			<tr>
				<td class="key">
					<xsl:value-of select="local-name()"/>
				</td>
				<td class="value">
					<table>
						<xsl:apply-templates select="*"/>
					</table>
				</td>


			</tr>
		</div>
	</xsl:template>


	<!-- XML Pretty Maker -->

	<xsl:template match="node()" mode="xmlMarkup">
		<div class="xmlBlock"> &lt; <span class="xmlTagName">
				<xsl:value-of select="name(.)"/>
			</span>
			<xsl:apply-templates select="@*" mode="xmlMarkup"/> &gt; <xsl:apply-templates
				select="node()" mode="xmlMarkup"/> &lt;/ <span class="xmlTagName">
				<xsl:value-of select="name(.)"/>
			</span> &gt; </div>
	</xsl:template>

	<xsl:template match="text()" mode="xmlMarkup">
		<span class="xmlText">
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<xsl:template match="@*" mode="xmlMarkup">
		<xsl:text> </xsl:text>
		<span class="xmlAttrName">
			<xsl:value-of select="name()"/>
		</span> =" <span class="xmlAttrValue">
			<xsl:value-of select="."/>
		</span> " </xsl:template>

	<xsl:template name="xmlstyle"> .xmlSource { font-size: 70%; border: solid #c0c0a0 1px;
		background-color: #ffffe0; padding: 2em 2em 2em 0em; } .xmlBlock { padding-left: 2em; }
		.xmlTagName { color: #800000; font-weight: bold; } .xmlAttrName { font-weight: bold; }
		.xmlAttrValue { color: #0000c0; } </xsl:template>

</xsl:stylesheet>

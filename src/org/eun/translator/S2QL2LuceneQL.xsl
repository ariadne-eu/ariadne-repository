<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fires2ql="http://fire.eun.org/xsd/s2ql-2.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" >
    
    <xsl:output omit-xml-declaration="yes"/>
    
    <xsl:template match="fires2ql:s2ql">
        xmlns:"http://ltsc.ieee.org/xsd/LOM"
        <xsl:for-each select="./fires2ql:keyword">
           AND (lom.general.title.string:"<xsl:value-of select="."/>"
             OR lom.general.description.string:"<xsl:value-of select="."/>" 
             OR lom.general.keyword.string:"<xsl:value-of select="."/>" 
             OR lom.lifecycle.contribute.entity:"<xsl:value-of select="."/>" 
             OR lom.metadata.contribute.entity:"<xsl:value-of select="."/>"  )
        </xsl:for-each>
        
        <xsl:for-each select="./fires2ql:language">
        	<xsl:choose>
                <xsl:when test="(position()=1) and (position()=last())" > AND lom.general.language:"<xsl:value-of select="."/>" </xsl:when>
                <xsl:when test="position()=1" > AND lom.general.language:"<xsl:value-of select="."/>" OR </xsl:when>
                <xsl:when test="position()=last()" > lom.general.language:"<xsl:value-of select="."/>" </xsl:when>
                <xsl:otherwise>lom.general.language:"<xsl:value-of select="."/>" OR </xsl:otherwise>
            </xsl:choose> 
             
        </xsl:for-each>
        
        <xsl:for-each select="./fires2ql:ageRange/fires2ql:minAge">
        	<xsl:choose>
				<xsl:when test="../../fires2ql:ageRange/fires2ql:maxAge">
					<xsl:if test="(. = 'u')">
					   AND lom.educational.typicalagerange.min:"u" 
					</xsl:if>
					<xsl:if test="not (. = 'u') ">
						<xsl:if test="(not (. >= 100)) and (. >= 10)">
							AND lom.educational.typicalagerange.min:[000 TO 0<xsl:value-of select="."/>]
						</xsl:if>
						<xsl:if test="not (. >= 10)">
							AND lom.educational.typicalagerange.min:[000 TO 00<xsl:value-of select="."/>]
						</xsl:if>
						<xsl:if test="(. >= 100)">
							AND lom.educational.typicalagerange.min:[000 TO <xsl:value-of select="."/>]
						</xsl:if>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="(. = 'u')">
						AND lom.educational.typicalagerange.min:"u"
						AND lom.educational.typicalagerange.max:"u"
					</xsl:if>
					<xsl:if test="not (. = 'u') ">
						<xsl:if test="(not (. >= 100)) and (. >= 10)">
							AND lom.educational.typicalagerange.min:[000 TO 0<xsl:value-of select="."/>]
							AND lom.educational.typicalagerange.max:[0<xsl:value-of select="."/> TO 199]
						</xsl:if>
						<xsl:if test="not (. >= 10)">
							AND lom.educational.typicalagerange.min:[000 TO 00<xsl:value-of select="."/>]
							AND lom.educational.typicalagerange.max:[00<xsl:value-of select="."/> TO 199]
						</xsl:if>
						<xsl:if test="(. >= 100)">
							AND lom.educational.typicalagerange.min:[000 TO <xsl:value-of select="."/>]
							AND lom.educational.typicalagerange.max:[<xsl:value-of select="."/> TO 199]
						</xsl:if>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
        </xsl:for-each> 
        
        <xsl:for-each select="./fires2ql:ageRange/fires2ql:maxAge">
	    	<xsl:choose>
				<xsl:when test="../../fires2ql:ageRange/fires2ql:minAge">
			        <xsl:if test="(. = 'u')">
					   AND  lom.educational.typicalagerange.max:"u"
					</xsl:if>
					<xsl:if test="not (. = 'u') ">
						<xsl:if test="(not (. >= 100)) and (. >= 10)">
							AND lom.educational.typicalagerange.max:[0<xsl:value-of select="."/> TO 199]
						</xsl:if>
						<xsl:if test="not (. >= 10)">
							AND lom.educational.typicalagerange.max:[00<xsl:value-of select="."/> TO 199]
						</xsl:if>
						<xsl:if test="(. >= 100)">
							AND lom.educational.typicalagerange.max:[<xsl:value-of select="."/> TO 199]
						</xsl:if>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="(. = 'u')">
						AND lom.educational.typicalagerange.max:"u"
						AND lom.educational.typicalagerange.min:"u"
					</xsl:if>
					<xsl:if test="not (. = 'u') ">
						<xsl:if test="(not (. >= 100)) and (. >= 10)">
							AND lom.educational.typicalagerange.max:[0<xsl:value-of select="."/> TO 199]
							AND lom.educational.typicalagerange.min:[000 TO 0<xsl:value-of select="."/>]
						</xsl:if>
						<xsl:if test="not (. >= 10)">
							AND lom.educational.typicalagerange.max:[00<xsl:value-of select="."/> TO 199]
							AND lom.educational.typicalagerange.min:[000 TO 00<xsl:value-of select="."/>]
						</xsl:if>
						<xsl:if test="(. >= 100)">
							AND lom.educational.typicalagerange.max:[<xsl:value-of select="."/> TO 199]
							AND lom.educational.typicalagerange.min:[000 TO <xsl:value-of select="."/>]
						</xsl:if>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
        </xsl:for-each>
        
    </xsl:template>
    
    <xsl:template match="s2ql">  
        xmlns:"http://ltsc.ieee.org/xsd/LOM"
        <xsl:for-each select="keyword">
           AND (lom.general.title.string:"<xsl:value-of select="."/>"
             OR lom.general.description.string:"<xsl:value-of select="."/>" 
             OR lom.general.keyword.string:"<xsl:value-of select="."/>" 
             OR lom.lifecycle.contribute.entity:"<xsl:value-of select="."/>" 
             OR lom.metadata.contribute.entity:"<xsl:value-of select="."/>"  )
        </xsl:for-each>
        
        <xsl:for-each select="language">
        	<xsl:choose>
                <xsl:when test="(position()=1) and (position()=last())" > AND lom.general.language:"<xsl:value-of select="."/>" </xsl:when>
                <xsl:when test="position()=1" > AND lom.general.language:"<xsl:value-of select="."/>" OR </xsl:when>
                <xsl:when test="position()=last()" > lom.general.language:"<xsl:value-of select="."/>" </xsl:when>
                <xsl:otherwise>lom.general.language:"<xsl:value-of select="."/>" OR </xsl:otherwise>
            </xsl:choose> 
             
        </xsl:for-each>
        
        <xsl:for-each select="minAge">
	        <xsl:variable name="min">
			   <xsl:value-of select="."/>
			</xsl:variable>
	        
	        <xsl:if test="minAge = u ">
			   lom.educational.typicalagerange.min:"u" 
			</xsl:if>
			<xsl:if test="minAge != u ">
			   lom.educational.typicalagerange.min:[0 TO <xsl:value-of select="."/>]
			</xsl:if>
            
        </xsl:for-each> 
        
        <xsl:for-each select="maxAge">   
	        <xsl:if test=". = u ">
			   AND  lom.educational.typicalagerange.max:"u"
			</xsl:if>
			<xsl:if test=". != u ">
			   AND lom.educational.typicalagerange.max:[<xsl:value-of select="."/> TO 199]
			</xsl:if>
            
        </xsl:for-each>
        
    </xsl:template>    
    
</xsl:stylesheet>

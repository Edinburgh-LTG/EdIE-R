<!-- Convert inline markup to standoff. -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="reln">
  <xsl:apply-templates select="node()"/>
</xsl:template>

<xsl:template match="standoff">
  <xsl:copy>
    <xsl:apply-templates select="relations"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="relations">
<!--  <xsl:text>&#10;</xsl:text>-->
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
    <xsl:if test="../../text//ng[ent[@type='mod' and @stime] and ent[@type~'stroke$']]">
      <xsl:apply-templates select="../../text//ng[ent[@type='mod' and @stime] and ent[@type~'stroke$']]" mode="standoff"/>
    </xsl:if>
<!--    <xsl:text>&#10;</xsl:text>-->
  </xsl:copy>
<!--  <xsl:text>&#10;</xsl:text>-->
</xsl:template>

<xsl:template mode="standoff" match="*">
  <xsl:for-each select="ent[@type='mod' and @stime]">
    <xsl:text>&#10;  </xsl:text>
    <relation type='time'>
      <xsl:text>&#10;    </xsl:text>
      <argument arg1="true">
        <xsl:attribute name="ref">
          <xsl:value-of select="../ent[@type~'stroke$']/@id"/>
        </xsl:attribute>
        <xsl:attribute name="text">
          <xsl:value-of select="normalize-space(../ent[@type~'stroke$'])"/>
        </xsl:attribute>
      </argument>
      <xsl:text>&#10;    </xsl:text>
      <argument arg2="true">
        <xsl:attribute name="ref">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="text">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:attribute>
      </argument>
    <xsl:text>&#10;  </xsl:text>
    </relation>
  </xsl:for-each>
</xsl:template>

</xsl:stylesheet>

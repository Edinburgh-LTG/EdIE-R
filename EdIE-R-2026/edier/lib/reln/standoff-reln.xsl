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
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="relations"/>
    <xsl:text>&#10;</xsl:text>
   </xsl:copy>
</xsl:template>

<xsl:template match="relations">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
    <xsl:if test="../../text//reln">
      <xsl:text>&#10;</xsl:text>
      <xsl:apply-templates select="../../text//reln" mode="standoff"/>
      <xsl:text>&#10;</xsl:text>
    </xsl:if>
  </xsl:copy>
</xsl:template>

<xsl:template mode="standoff" match="reln[@type='loc']">
  <xsl:text>&#10;  </xsl:text>
  <relation>
    <xsl:apply-templates select="@*"/>
    <xsl:text>&#10;    </xsl:text>
    <argument arg1="true">
      <xsl:attribute name="ref">
        <xsl:value-of select="ng[@type]//ent[not(@type='mod')]/@id"/>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space(ng[@type]//ent[not(@type='mod')])"/>
      </xsl:attribute>
    </argument>
    <xsl:text>&#10;    </xsl:text>
    <argument arg2="true">
      <xsl:attribute name="ref">
        <xsl:value-of select="ent[@type='mod' and @sloc]/@id"/>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space(ent[@type='mod' and @sloc])"/>
      </xsl:attribute>
    </argument>
  <xsl:text>&#10;  </xsl:text>
  </relation>
</xsl:template>

<xsl:template mode="standoff" match="reln[@type='time']">
  <xsl:text>&#10;  </xsl:text>
  <relation>
    <xsl:apply-templates select="@*"/>
    <xsl:text>&#10;    </xsl:text>
    <argument arg1="true">
      <xsl:attribute name="ref">
        <xsl:value-of select="ng[@type]//ent[not(@type='mod')]/@id"/>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space(ng[@type]//ent[not(@type='mod')])"/>
      </xsl:attribute>
    </argument>
    <xsl:text>&#10;    </xsl:text>
    <argument arg2="true">
      <xsl:attribute name="ref">
        <xsl:value-of select="ent[@type='mod' and @stime]/@id"/>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space(ent[@type='mod' and @stime])"/>
      </xsl:attribute>
    </argument>
  <xsl:text>&#10;  </xsl:text>
  </relation>
</xsl:template>

</xsl:stylesheet>

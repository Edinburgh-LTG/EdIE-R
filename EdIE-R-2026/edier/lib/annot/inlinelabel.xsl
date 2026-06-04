<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="p[.//w[@id~'^w00']]">
  <xsl:variable name="id">
    <xsl:value-of select=".//w[@id~'^w00']/@id"/>
  </xsl:variable>
  <xsl:copy>
    <xsl:if test="//ent[parts[part[@sw=$id]]]">
      <xsl:attribute name="label">yes</xsl:attribute>
    </xsl:if>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="ent[@type='label_yes']">
  <xsl:variable name="sw">
    <xsl:value-of select="parts/part/@sw"/>
  </xsl:variable>
  <xsl:variable name="label">
    <xsl:value-of select="//p/s[w[@id=$sw]]/w[1]"/>
  </xsl:variable>
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="type">
      <xsl:text>label:</xsl:text>
      <xsl:value-of select="$label"/>
    </xsl:attribute>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>

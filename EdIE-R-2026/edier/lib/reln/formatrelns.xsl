<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="standoff">
  <xsl:copy>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="node()|@*"/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="relations">
  <xsl:copy>
    <xsl:apply-templates select="relation"/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="relation">
  <xsl:text>&#10;  </xsl:text>
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="section[@type='clinical']//s">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="proc">no</xsl:attribute>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="section[@type='report']//s">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="proc">yes</xsl:attribute>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>


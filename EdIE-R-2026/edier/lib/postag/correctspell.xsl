<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="w[.~'^hae(mmor|mor)hage']">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="sp"><xsl:value-of select='.'/></xsl:attribute>
    <xsl:text>haemorrhage</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="w[.~'^(aranchoid|archnoid|aracnoid)$']">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="sp"><xsl:value-of select='.'/></xsl:attribute>
    <xsl:text>arachnoid</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="w[.~'^sub.*(aranchoid|archnoid|aracnoid)$']">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="sp"><xsl:value-of select='.'/></xsl:attribute>
    <xsl:text>subarachnoid</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>

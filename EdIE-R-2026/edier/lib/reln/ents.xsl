<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="numex">
  <xsl:apply-templates select="node()"/>
</xsl:template>

<xsl:template match="timex">
  <xsl:apply-templates select="node()"/>
</xsl:template>

<xsl:template match="ng">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="ent[@sloc or @stime]">
  <ent type="mod">
    <xsl:apply-templates select="@*|node()"/>
  </ent>
</xsl:template>

<!-- this moved from standoff-end.xsl because it's better for reindexing -->
<xsl:template match="ent[@type='haemorrhagic_stroke']/ent[.~'^[Ii]ntra.*cerebral$']">
  <xsl:apply-templates select="node()"/>
</xsl:template>

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>

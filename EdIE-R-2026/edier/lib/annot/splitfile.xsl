<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common">

<xsl:template match="records">
  <xsl:apply-templates select="document"/>
</xsl:template>

<xsl:template match="document">
  <xsl:variable name="fname">
    <xsl:value-of select="@id"/>
    <xsl:text>.xml</xsl:text>
  </xsl:variable>
  <exsl:document href="{$fname}">
    <xsl:copy-of select="."/>
  </exsl:document>
</xsl:template>

</xsl:stylesheet>

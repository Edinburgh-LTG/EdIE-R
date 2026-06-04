<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="IAA-file" select="IAA-file"/>
<xsl:param name="pipeline-file" select="pipeline-file"/>

<xsl:variable name="IAA" select="document($IAA-file)/performance/run/document"/>
<xsl:variable name="pipeline" select="document($pipeline-file)/performance/run/document"/>

<xsl:output method="html"/>

<xsl:template match="/">
  <xsl:apply-templates select="//component"/>
</xsl:template>

<xsl:template match="component">

  <xsl:variable name="component" select="."/>
  <xsl:variable name="iaa-results" select="$IAA/*[name()=$component/@name]"/>
  <xsl:variable name="pipeline-results" select="$pipeline/*[name()=$component/@name]"/>

  <h1><xsl:value-of select="@name"/></h1>
  <table>
    <tr><th>Name</th><th>IAA</th><th>%-required</th><th>Target</th><th>Pipeline</th><th>Deviation</th></tr>

    <xsl:for-each select="subcomponent">
    <xsl:call-template name="row">
      <xsl:with-param name="label" select="@name"/>
      <xsl:with-param name="target">
	<xsl:choose>
          <xsl:when test="@target">
            <xsl:value-of select="@target"/>
	  </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="../@target"/>
          </xsl:otherwise>
	</xsl:choose>
      </xsl:with-param>
      <xsl:with-param name="iaa-results" select="$iaa-results/breakdown[(@type|@name|@IsProven|@IsDirect|@IsPositive)=current()/@name]"/>
      <xsl:with-param name="pipeline-results" select="$pipeline-results/breakdown[(@type|@name|@IsProven|@IsDirect|@IsPositive)=current()/@name]"/>
    </xsl:call-template>
    </xsl:for-each>

    <xsl:call-template name="row">
      <xsl:with-param name="label" select="'TOTAL'"/>
      <xsl:with-param name="target" select="$component/@target"/>
      <xsl:with-param name="iaa-results" select="$iaa-results"/>
      <xsl:with-param name="pipeline-results" select="$pipeline-results"/>
    </xsl:call-template>

  </table>

</xsl:template>

<xsl:template name="row">
  <xsl:param name="label"/>     
  <xsl:param name="target"/>     
  <xsl:param name="iaa-results"/>     
  <xsl:param name="pipeline-results"/>     

  <xsl:variable name="iaa-tp" select="sum($iaa-results/@tp)"/>
  <xsl:variable name="iaa-fp" select="sum($iaa-results/@fp)"/>
  <xsl:variable name="iaa-fn" select="sum($iaa-results/@fn)"/>
  <xsl:variable name="iaa-f" select="(200*$iaa-tp) div (2*$iaa-tp+$iaa-fp+$iaa-fn)"/>
	
  <xsl:variable name="pipeline-tp" select="sum($pipeline-results/@tp)"/>
  <xsl:variable name="pipeline-fp" select="sum($pipeline-results/@fp)"/>
  <xsl:variable name="pipeline-fn" select="sum($pipeline-results/@fn)"/>
  <xsl:variable name="pipeline-f" select="(200*$pipeline-tp) div (2*$pipeline-tp+$pipeline-fp+$pipeline-fn)"/>

  <tr>
    <th><xsl:value-of select="$label"/></th>
    <td><xsl:value-of select="format-number($iaa-f, '0.00')"/></td>
    <td><xsl:value-of select="format-number($target, '0.00')"/></td>
    <td><xsl:value-of select="format-number($iaa-f * $target div 100, '0.00')"/></td>
    <td><xsl:value-of select="format-number($pipeline-f, '0.00')"/></td>
    <td><xsl:value-of select="format-number($pipeline-f - $iaa-f * $target div 100, '0.00')"/></td>
  </tr>


</xsl:template>

</xsl:stylesheet>

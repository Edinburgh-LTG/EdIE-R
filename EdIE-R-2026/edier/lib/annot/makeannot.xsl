<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="document">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="version">5</xsl:attribute>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="meta"/>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="text"/>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="standoff"/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="meta">
  <xsl:copy>
  </xsl:copy>
</xsl:template>

<xsl:template match="standoff[ents and relations]">
  <xsl:copy>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="ents"/>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="relations"/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="standoff[not(ents) and not(relations)]">
  <xsl:copy>
    <xsl:text>&#10;</xsl:text>
    <ents/>
    <xsl:text>&#10;</xsl:text>
    <relations/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="standoff[ents and not(relations)]">
  <xsl:copy>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="ents"/>
    <xsl:text>&#10;</xsl:text>
    <relations/>
    <xsl:text>&#10;</xsl:text>
  </xsl:copy>
</xsl:template>

<xsl:template match="text">
  <xsl:copy>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="../meta/header"/>
    <xsl:text>&#10;</xsl:text>
    <xsl:apply-templates select="../meta/labels"/>
    <xsl:text>&#10;</xsl:text>
    <p><s><w><xsl:text>CLINICAL HISTORY</xsl:text></w></s></p>
    <xsl:text>&#10;&#10;</xsl:text>
    <xsl:apply-templates select="section[@type='clinical']/p"/>
    <xsl:text>&#10;</xsl:text>
    <p><s><w><xsl:text>REPORT</xsl:text></w></s></p>
    <xsl:text>&#10;&#10;</xsl:text>
    <xsl:apply-templates select="section[@type='report']/p"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="header">
  <p><s><w><xsl:value-of select="."/><xsl:text> : </xsl:text><xsl:value-of select="../attr[@name='healthboard']"/></w></s></p>
</xsl:template>

<xsl:template match="labels">
  <xsl:apply-templates select="node()"/>
</xsl:template>

<xsl:template match="s[not(last())]">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
  <xsl:text>&#x20;</xsl:text>
</xsl:template>

<xsl:template match="s[last()]">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="p">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
<!--  <xsl:choose>
  <xsl:when test="s[@sec='rep'] and following-sibling::p[s[@sec='rep']]">
    <xsl:text>&#10;&#10;</xsl:text>
  </xsl:when>
  <xsl:when test="s[@sec='ch'] and following-sibling::p[s[@sec='ch']]">
    <xsl:text>&#10;&#10;</xsl:text>
  </xsl:when>
  <xsl:otherwise>-->
    <xsl:text>&#10;</xsl:text>
<!--  </xsl:otherwise>
  </xsl:choose>-->
</xsl:template>

<xsl:template match="l">
  <xsl:variable name="id">
    <xsl:text>w00</xsl:text>
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <p><s>
    <w>
    <xsl:apply-templates select="node()"/>
    </w><xsl:text> </xsl:text>
    <w>[</w><xsl:text> </xsl:text><w id="{$id}">select</w><xsl:text> </xsl:text><w>]</w>
  </s></p>
</xsl:template>

<xsl:template match="ent">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:if test="@neg='yes'">
      <attribute name="negated"/>
    </xsl:if>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="ent[@type='mod' and @sloc]">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="type">
      <xsl:text>loc_</xsl:text>
      <xsl:value-of select="@sloc"/>
    </xsl:attribute>
    <xsl:if test="@neg='yes'">
      <attribute name="negated"/>
    </xsl:if>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="ent[@type='mod' and @stime]">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="type">
      <xsl:text>time_</xsl:text>
      <xsl:value-of select="@stime"/>
    </xsl:attribute>
    <xsl:if test="@neg='yes'">
      <attribute name="negated"/>
    </xsl:if>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="relation[@type='loc']">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="type">mod-loc</xsl:attribute>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="relation[@type='time']">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:attribute name="type">mod-time</xsl:attribute>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="argument">
  <xsl:copy>
    <xsl:apply-templates select="@text|@ref"/>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="ents">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
    <xsl:for-each select="ancestor::document//l[@choice='pos']">
      <xsl:variable name="wid">
        <xsl:text>w00</xsl:text>
        <xsl:value-of select="@id"/>
      </xsl:variable>
      <xsl:variable name="lid">
        <xsl:text>l</xsl:text>
        <xsl:value-of select="@id"/>
      </xsl:variable>
      <ent type="label_yes" id="{$lid}">
        <parts>
	<part sw="{$wid}" ew="{$wid}">select</part>
        </parts>
      </ent>
    </xsl:for-each>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>


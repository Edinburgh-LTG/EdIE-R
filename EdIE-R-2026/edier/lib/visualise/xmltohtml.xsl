<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <style>
       strike {
        text-decoration: none;
        background-image: linear-gradient(transparent 7px,#000000 7px,#000000 9px,transparent 9px);
       }
      </style>
      <style>div.mrirecord {border:2px solid #000000}</style>
      <style>div.ctrecord {border:2px solid #000000}</style>
      <style>div.usrecord {border:2px solid #000000}</style>
      <style>div.report {border:2px solid #6699FF}</style>
      <style>div.concl {border:2px solid #99FF99}</style>
      <style>span.svd {background:#FFFF99}</style>
      <style>span.atrophy {background:#99FF99}</style>
      <style>span.ischstroke {background:#FF8899}</style>
      <style>span.haemstroke {background:#009999}</style>
      <style>span.stroke {background:#FF8533}</style>
      <style>span.tumour {background:#6699FF}</style>
      <style>span.haematoma {background:#BB88FF}</style>
      <style>span.microh {background:#CCFFFF}</style>
      <style>span.subarach {background:#FFCCFF}</style>
      <style>span.haemtrans {background:#CCFF00}</style>
      <style>span.deep {color:red}</style>
      <style>span.cortical {color:blue}</style>
      <style>span.old {color:#00CC00}</style>
      <style>span.recent {color:#FFCC00}</style>
    </head>
    <body>
<table align="center" border="0" style="border:2px solid #000000">
<tr><td colspan="4" align="center"><b>Mark-up Key</b></td></tr>
<tr/>
<tr><td colspan="2" style="background-color:LightBlue">Positive label for entire record</td><td colspan="2"><strike>negated mark-up</strike></td></tr>
<tr/>
<tr>
 <td><span class="ischstroke">Ischaemic stroke</span></td>
 <td><span class="haemstroke">Haemorrhagic stroke</span></td>
 <td><span class="stroke">Stroke (type unknown)</span></td>
</tr>
<tr>
 <td><span class="haematoma">Subdural haematoma</span></td>
 <td><span class="microh">Microhaemorrhage</span></td>
 <td><span class="subarach">Subarachnoid haemorrhage</span></td>
 <td><span class="haemtrans">Haemorrhagic transformation</span></td>
</tr>
<tr>
 <td><span class="tumour">Tumour</span></td>
 <td><span class="svd">Small vessel disease</span></td>
 <td><span class="atrophy">Atrophy</span></td>
</tr>
<tr>
 <td><span class="deep">Deep</span></td>
 <td><span class="cortical">Cortical</span></td>
 <td><span class="old">Old</span></td>
 <td><span class="recent">Recent</span></td>
</tr>
</table>
<br/>
<br/>
<br/>
      <xsl:apply-templates select="//document" />
    </body>
  </html>
</xsl:template>

<xsl:template match="document">
  <xsl:choose>
    <xsl:when test="meta/attr[@name='type' and .='MRI']">
      <div class="mrirecord" style="padding:2px">
        <xsl:apply-templates select="node()"/>
      </div>
    </xsl:when>
    <xsl:when test="meta/attr[@name='type' and .='CT']">
      <div class="ctrecord" style="padding:2px">
        <xsl:apply-templates select="node()"/>
      </div>
    </xsl:when>
    <xsl:when test="meta/attr[@name='type' and .='US_Doppler']">
      <div class="usrecord" style="padding:2px">
        <xsl:apply-templates select="node()"/>
      </div>
    </xsl:when>
    <xsl:otherwise>
      <div class="ctrecord" style="padding:2px">
        <xsl:apply-templates select="node()"/>
      </div>
    </xsl:otherwise>
  </xsl:choose>
<br/>
<br/>
</xsl:template>

<xsl:template match="meta">
  <xsl:apply-templates select="header|labels"/>
</xsl:template>

<xsl:template match="header">
  <p><b>
    <xsl:text>Document : </xsl:text><xsl:value-of select="../../@id"/><br/>
    <xsl:apply-templates/>
  </b></p>
</xsl:template>

<xsl:template match="labels">
  <table align="center" border="1" style="border:2px solid #000000">
    <tr>
      <xsl:apply-templates select="l[@id='1']"/>
      <xsl:apply-templates select="l[@id='2']"/>
      <xsl:apply-templates select="l[@id='3']"/>
      <xsl:apply-templates select="l[@id='4']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='6']"/>
      <xsl:apply-templates select="l[@id='7']"/>
      <xsl:apply-templates select="l[@id='8']"/>
      <xsl:apply-templates select="l[@id='9']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='5']"/>
      <xsl:apply-templates select="l[@id='10']"/>
      <xsl:apply-templates select="l[@id='11']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='12']"/>
      <xsl:apply-templates select="l[@id='13']"/>
      <xsl:apply-templates select="l[@id='14']"/>
      <xsl:apply-templates select="l[@id='15']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='16']"/>
      <xsl:apply-templates select="l[@id='17']"/>
      <xsl:apply-templates select="l[@id='18']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='19']"/>
      <xsl:apply-templates select="l[@id='20']"/>
      <xsl:apply-templates select="l[@id='24']"/>
    </tr>
    <tr>
      <xsl:apply-templates select="l[@id='21']"/>
      <xsl:apply-templates select="l[@id='22']"/>
      <xsl:apply-templates select="l[@id='23']"/>
    </tr>
  </table>
<br/>
<br/>
</xsl:template>

<xsl:template match="l">
  <xsl:choose>
    <xsl:when test="@choice='pos'">
      <td style="background-color:LightBlue">
        <xsl:apply-templates/>
      </td>
    </xsl:when>
    <xsl:otherwise>
      <td>
        <xsl:apply-templates/>
      </td>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="p">
  <p>
    <xsl:apply-templates/>
  </p>
</xsl:template>

<xsl:template match="text">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="report">
  <div class="report" style="padding:2px">
    <xsl:apply-templates/>
  </div>
<br/>
</xsl:template>

<xsl:template match="conclusion">
  <div class="concl" style="padding:2px">
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="ent[@type='small_vessel_disease']">
  <span class="svd">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type='stroke']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="stroke">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="stroke">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='ischaemic_stroke']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="ischstroke">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="ischstroke">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='haemorrhagic_stroke']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="haemstroke">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="haemstroke">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='atrophy']">
  <span class="atrophy">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type~'tumour$']">
  <span class="tumour">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type='subdural_haematoma']">
  <span class="haematoma">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type='microhaemorrhage']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="microh">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="microh">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='subarachnoid_haemorrhage']">
  <span class="subarach">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type='haemorrhagic_transformation']">
  <span class="haemtrans">
    <xsl:apply-templates/>
  </span>
</xsl:template>

<xsl:template match="ent[@type='mod' and @sloc='deep']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="deep">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="deep">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='mod' and @sloc='cortical']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="cortical">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="cortical">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='mod' and @stime='old']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="old">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="old">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="ent[@type='mod' and @stime='recent']">
  <xsl:variable name="id">
    <xsl:value-of select="@id"/>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="ancestor::document//relations/relation/argument/@ref=$id">
      <xsl:text>[</xsl:text>
      <span class="recent">
        <xsl:apply-templates/>
      </span>
      <xsl:text>]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <span class="recent">
        <xsl:apply-templates/>
      </span>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="w[ancestor::*[1][@neg]]">
  <b><strike>
    <xsl:apply-templates/>
  </strike></b>
</xsl:template>

<xsl:template match="standoff">
</xsl:template>

</xsl:stylesheet>

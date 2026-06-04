<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="meta">
  <xsl:copy>
    <xsl:if test="attr[@name='modality_Desc' and not(.~'^[CM][TR]')]">
      <xsl:text>&#10;</xsl:text>
      <attr name="exclude">notctmr</attr>
    </xsl:if>
    <xsl:if test="../text[section[@type='clinical'] and not(section[@type='report'])]">
      <xsl:text>&#10;</xsl:text>
      <attr name="exclude">justclinhist</attr>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="../text/section[@type='report' and .//s[.~'(CT|MR).* ([Ss]pine|SPINE|Thorax|THORAX|thorax|Neck|NECK|Lumbar|LUMBAR|Chest|CHEST|Abdomen|ABDOMEN|abdomen|Cervical|CERVICAL|Aortic|AORTIC|Thoracic|THORACIC|Adrenal|ADRENAL|Sinuses|Pelvis|PELVIS|pelvis)']]">
          <xsl:text>&#10;</xsl:text> 
          <attr name="exclude">otherbodypart</attr>
      </xsl:when>
      <xsl:when test="../text/section[@type='report' and .//s[.~'(Spine|SPINE|Thorax|THORAX|Neck|NECK|Lumbar|LUMBAR|Chest|CHEST|Abdomen|ABDOMEN|Cervical|CERVICAL|Aortic|AORTIC|Thoracic|THORACIC|Adrenal|ADRENAL|Sinuses).*(CT|MR)']]">
          <xsl:text>&#10;</xsl:text> 
          <attr name="exclude">otherbodypart</attr>
      </xsl:when>
      <xsl:when test="../text[.//s[1][.~'^PET FDG Whole body' or .~'^CHEST']]">
          <xsl:text>&#10;</xsl:text> 
          <attr name="exclude">otherbodypart</attr>
      </xsl:when>
    </xsl:choose>
    <xsl:if test="../text[.//s[1][.='Unprocessable html']]">
        <xsl:text>&#10;</xsl:text> 
        <attr name="exclude">html</attr>
    </xsl:if>
    <xsl:choose>
<!-- new -->
      <xsl:when test="../text[.~'(NEMEX|XYLO|XILO|STRADL|BAIRD|ENGAGE|AMARYLIS|NAVIGATE|STIGA|TRIMETHS)']">
        <xsl:text>&#10;</xsl:text> 
        <attr name="exclude">study</attr>
      </xsl:when>
      <xsl:when test="../text[.~'(R&amp;D|Magnetom|tempo2|Clarify|ACUMEN|Lantern|CIP|Xilo-Fist|XILO-FIST|Xilofist|Future MS|FUTURE MS|Wyeth|CENTAUR|Cell Study|LIFT|PISCES|Pisces|Epad|EPAD|E-PAD|E-Pad)' or .//w[.~'^([Pp]articipant|PARTICIPANT|[Rr]esearch|RESEARCH|Study|STUDY|Trial|TRIAL|[Vv]olunteer|VOLUNTEER)$']]">
        <xsl:text>&#10;</xsl:text> 
        <attr name="exclude">study</attr>
      </xsl:when>
      <xsl:when test="../text[.~'(TICH|Neuropolitical|Smart|participant|Participant|PARTICIPANT|research|Research|RESEARCH|Study|STUDY|Trial|TRIAL|volunteer|Volunteer|VOLUNTEER|study recruit)']">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">study</attr>
      </xsl:when>
      <xsl:when test="../text[.//s[.~'[Rr]ecruited .*[Tt](rial|RIAL)']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">study</attr>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="../text[not(.//s) or count(.//w) = 1 ]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">nocontent</attr>
        <attr name="noc">1word</attr>
      </xsl:when>
      <xsl:when test="../text[count(.//s) &lt; 5 and .//s[w[.~'^[Nn]o$' and following-sibling::w[1][.='report']]]]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">nocontent</attr>
        <attr name="noc">no_report</attr>
      </xsl:when>
      <xsl:when test="../text[count(.//s) &lt; 7 and .//s[.~'no images.*(acquired|obtained)']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">nocontent</attr>
        <attr name="noc">no_images</attr>
      </xsl:when>
      <xsl:when test="../text[.//s[w[2]] and count(.//w) &lt; 20 and not(.//w[.~'([Nn]ormal|NORMAL)' or .~'^([Ee]vidence|[Nn][Oo]|[Pp]revious|headache|Negative|html)$'])]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">nocontent</attr>
        <attr name="noc">under20w</attr>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="../text/section[@type='report' and .//s[.~'claustrophob']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">move-art</attr>
        <attr name="move">claust</attr>
      </xsl:when>
      <xsl:when test="../text/section[@type='report' and .//s[.~'(exam|[Pp]rocedure|[Ss]can).*(abandoned|terminated)' or .~'able.*magnet' or .~'Optical Disc']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">move-art</attr>
        <attr name="move">abandoned</attr>
      </xsl:when>
      <xsl:when test="../text/section[@type='report' and .//s[.~'(not|able) .*tolerate']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">move-art</attr>
        <attr name="move">tolerate</attr>
      </xsl:when>
      <xsl:when test="../text/section[@type='report' and .//s[.~'movement art[ie]fact']]">
        <xsl:text>&#10;</xsl:text>
        <attr name="exclude">move-art</attr>
        <attr name="move">move-art</attr>
      </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="node()|@*">
  <xsl:copy>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>

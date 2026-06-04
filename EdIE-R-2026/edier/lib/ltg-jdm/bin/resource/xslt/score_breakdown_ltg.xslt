<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exslt="http://exslt.org/common">

	<xsl:output method="html" version="1.0" encoding="UTF-8"
		indent="no" />

	<xsl:template match="/">
		<HTML>
			<HEAD />
			<BODY>
				<H1>
					<xsl:choose>
						<xsl:when test="run/@type='iaa'">
							<xsl:text>IAA Score</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Pipeline Score</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</H1>
				<p>
					<H3>
						<em>Date: </em>
						<xsl:value-of select="run/@creationDate" />
						<br />
						<xsl:choose>
							<xsl:when test="run/@type='iaa'">
								<em>Input Files: </em>
								<xsl:value-of select="run/@inputFilesDir" />
							</xsl:when>
							<xsl:otherwise>
								<em>Gold Files: </em>
								<xsl:value-of select="run/@goldFilesDir" />
								<br />
								<em>System Output Files: </em>
								<xsl:value-of select="run/@systemFilesDir" />
							</xsl:otherwise>
						</xsl:choose>
					</H3>
				</p>

				<xsl:variable name="alphaValue" select="0.5" />

				<H2>NER Score Summary</H2>

				<TABLE>
					<TR>
						<TH>Type</TH>
						<TH>Number</TH>
						<TH>TP</TH>
						<TH>FP</TH>
						<TH>FN</TH>
						<TH>Precison</TH>
						<TH>Recall</TH>
						<TH>F1</TH>
					</TR>

					<xsl:variable name="breakdowns">
						<xsl:for-each
							select="run/document/NER/breakdown[not(@isScored)]">
							<xsl:sort select="@type" />
							<xsl:copy-of select="." />
						</xsl:for-each>
					</xsl:variable>

					<xsl:for-each select="exslt:node-set($breakdowns)/breakdown">
						<xsl:variable name="pos" select="position()" />
						<xsl:if
							test="$pos = 1 or @type != current()/preceding-sibling::breakdown[1]/@type">
							<xsl:variable name="subset"
								select="exslt:node-set($breakdowns)/breakdown[@type = current()/@type]" />
							<xsl:call-template name="output-scores">
								<xsl:with-param name="title" select="@type" />
								<xsl:with-param name="tp"
									select="sum(exslt:node-set($subset)/@tp)" />
								<xsl:with-param name="fp"
									select="sum(exslt:node-set($subset)/@fp)" />
								<xsl:with-param name="fn"
									select="sum(exslt:node-set($subset)/@fn)" />
								<xsl:with-param name="alpha" select="$alphaValue" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<xsl:for-each select="run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title" select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/NER/breakdown[not(@isScored) and not(@type='personal') and not(@type='summary') and not(@type='skills') and not(@type='education') and not(@type='employment') and not(@type='references') and not(@type='other')]/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/NER/breakdown[not(@isScored) and not(@type='personal') and not(@type='summary') and not(@type='skills') and not(@type='education') and not(@type='employment') and not(@type='references') and not(@type='other')]/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/NER/breakdown[not(@isScored) and not(@type='personal') and not(@type='summary') and not(@type='skills') and not(@type='education') and not(@type='employment') and not(@type='references') and not(@type='other')]/@fn)" />
							<xsl:with-param name="alpha" select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</TABLE>

				<H2>Relation Extraction Score Summary</H2>

				<TABLE>
					<TR>
						<TH>Type</TH>
						<TH>Number</TH>
						<TH>TP</TH>
						<TH>FP</TH>
						<TH>FN</TH>
						<TH>Precison</TH>
						<TH>Recall</TH>
						<TH>F1</TH>
					</TR>

					<xsl:variable name="breakdowns">
						<xsl:for-each select="run/document/RE/breakdown[not(@isScored)]">
							<xsl:sort select="@type" />
							<xsl:copy-of select="." />
						</xsl:for-each>
					</xsl:variable>

					<xsl:for-each select="exslt:node-set($breakdowns)/breakdown">
						<xsl:variable name="pos" select="position()" />
						<xsl:if
							test="$pos = 1 or @type != current()/preceding-sibling::breakdown[1]/@type">
							<xsl:variable name="subset"
								select="exslt:node-set($breakdowns)/breakdown[@type = current()/@type]" />
							<xsl:call-template name="output-scores">
								<xsl:with-param name="source" select="@source" />
								<xsl:with-param name="title" select="@type" />
								<xsl:with-param name="tp"
									select="sum(exslt:node-set($subset)/@tp)" />
								<xsl:with-param name="fp"
									select="sum(exslt:node-set($subset)/@fp)" />
								<xsl:with-param name="fn"
									select="sum(exslt:node-set($subset)/@fn)" />
								<xsl:with-param name="alpha" select="$alphaValue" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<xsl:for-each select="run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title" select="'TOTAL'" />
							<xsl:with-param name="tp" select="sum(document/RE/@tp)" />
							<xsl:with-param name="fp" select="sum(document/RE/@fp)" />
							<xsl:with-param name="fn" select="sum(document/RE/@fn)" />
							<xsl:with-param name="alpha" select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</TABLE>

			</BODY>
		</HTML>
	</xsl:template>


	<xsl:template name="precision">
		<xsl:param name="tp" />
		<xsl:param name="fp" />
		<xsl:param name="fn" />

		<xsl:value-of select="$tp div ($tp + $fp) * 100" />
	</xsl:template>

	<xsl:template name="recall">
		<xsl:param name="tp" />
		<xsl:param name="fp" />
		<xsl:param name="fn" />

		<xsl:value-of select="$tp div ($tp + $fn) * 100" />
	</xsl:template>



	<xsl:template name="fmeasure">
		<xsl:param name="precision" />
		<xsl:param name="recall" />
		<xsl:param name="alpha" />

		<xsl:value-of
			select="1 div (($alpha div $precision) + ((1 - $alpha) div $recall))" />
	</xsl:template>


	<xsl:template name="output-scores">
		<xsl:param name="source" />
		<xsl:param name="title" />
		<xsl:param name="tp" />
		<xsl:param name="fp" />
		<xsl:param name="fn" />
		<xsl:param name="alpha" />

		<xsl:variable name="precision">
			<xsl:call-template name="precision">
				<xsl:with-param name="tp" select="$tp" />
				<xsl:with-param name="fp" select="$fp" />
				<xsl:with-param name="fn" select="$fn" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="recall">
			<xsl:call-template name="recall">
				<xsl:with-param name="tp" select="$tp" />
				<xsl:with-param name="fp" select="$fp" />
				<xsl:with-param name="fn" select="$fn" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="fmeasure">
			<xsl:call-template name="fmeasure">
				<xsl:with-param name="precision" select="$precision" />
				<xsl:with-param name="recall" select="$recall" />
				<xsl:with-param name="alpha" select="$alpha" />
			</xsl:call-template>
		</xsl:variable>

		<TR>
			<TH>
				<xsl:value-of select="$title" />
			</TH>
			<TD>
				<xsl:value-of select="format-number($tp + $fp + $fn, '#,##0')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($tp, '#,##0')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($fp, '#,##0')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($fn, '#,##0')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($precision, '0.00')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($recall, '0.00')" />
			</TD>
			<TD>
				<xsl:value-of select="format-number($fmeasure, '0.00')" />
			</TD>
		</TR>

	</xsl:template>



</xsl:stylesheet>

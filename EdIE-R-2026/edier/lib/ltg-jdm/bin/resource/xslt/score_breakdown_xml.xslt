<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:exslt="http://exslt.org/common">

	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:template match="/">
		<results>
				<title>
					<xsl:choose>
						<xsl:when test="performance/run/@type='iaa'">
							<xsl:text>IAA Score</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>NLPP Score</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</title>
				<creationDate>
					<xsl:value-of
						select="performance/run/@creationDate" />
				</creationDate>

				<inputFiles>
					<xsl:value-of
						select="performance/run/@inputFilesDir" />
				</inputFiles>
				
				<xsl:variable name="alphaValue" select="0.5" />

				<result type="NER">

					<xsl:variable name="breakdowns">
						<xsl:for-each
							select="performance/run/document/NER/breakdown[not(@isScored)]">
							<xsl:sort select="@type" />
							<xsl:copy-of select="." />
						</xsl:for-each>
					</xsl:variable>

					<xsl:for-each
						select="exslt:node-set($breakdowns)/breakdown">
						<xsl:variable name="pos" select="position()" />
						<xsl:if
							test="$pos = 1 or @type != current()/preceding-sibling::breakdown[1]/@type">
							<xsl:variable name="subset"
								select="exslt:node-set($breakdowns)/breakdown[@type = current()/@type]" />
							<xsl:call-template name="output-scores">
								<xsl:with-param name="title"
									select="@type" />
								<xsl:with-param name="tp"
									select="sum(exslt:node-set($subset)/@tp)" />
								<xsl:with-param name="fp"
									select="sum(exslt:node-set($subset)/@fp)" />
								<xsl:with-param name="fn"
									select="sum(exslt:node-set($subset)/@fn)" />
								<xsl:with-param name="alpha"
									select="$alphaValue" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<xsl:for-each select="performance/run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title"
								select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/NER/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/NER/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/NER/@fn)" />
							<xsl:with-param name="alpha"
								select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</result>


				<result type="Species">

					<xsl:for-each select="performance/run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title"
								select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/Species/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/Species/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/Species/@fn)" />
							<xsl:with-param name="alpha"
								select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</result>

				<xsl:variable name="alphaValueTI">
						<xsl:choose>
					   	  <xsl:when test="performance/run/@type='iaa'">
							   <xsl:value-of select="0.5" />
						  </xsl:when>
						  <xsl:otherwise>
							  <xsl:value-of select="0.0" />
						  </xsl:otherwise>
					    </xsl:choose>
				</xsl:variable>

				<result type="TN">

					<xsl:for-each select="performance/run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title"
								select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/TI/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/TI/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/TI/@fn)" />
							<xsl:with-param name="alpha"
								 select="$alphaValueTI" />
						</xsl:call-template>
					</xsl:for-each>

				</result>


				<result type="RE">

					<xsl:variable name="breakdowns">
						<xsl:for-each
							select="performance/run/document/RE/breakdown[not(@isScored)]">
							<xsl:sort select="@type" />
							<xsl:copy-of select="." />
						</xsl:for-each>
					</xsl:variable>

					<xsl:for-each
						select="exslt:node-set($breakdowns)/breakdown">
						<xsl:variable name="pos" select="position()" />
						<xsl:if
							test="$pos = 1 or @type != current()/preceding-sibling::breakdown[1]/@type">
							<xsl:variable name="subset"
								select="exslt:node-set($breakdowns)/breakdown[@type = current()/@type]" />
							<xsl:call-template name="output-scores">
								<xsl:with-param name="title"
									select="@type" />
								<xsl:with-param name="tp"
									select="sum(exslt:node-set($subset)/@tp)" />
								<xsl:with-param name="fp"
									select="sum(exslt:node-set($subset)/@fp)" />
								<xsl:with-param name="fn"
									select="sum(exslt:node-set($subset)/@fn)" />
								<xsl:with-param name="alpha"
									select="$alphaValue" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<xsl:for-each select="performance/run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title"
								select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/RE/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/RE/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/RE/@fn)" />
							<xsl:with-param name="alpha"
								select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</result>

				<!-- Relation Attributes -->


				<result type="Relation Attributes">

					<xsl:variable name="breakdowns">
						<xsl:for-each
							select="performance/run/document/RelAttribute/breakdown[not(@isScored)]">
							<xsl:sort select="@name" />
							<xsl:copy-of select="." />
						</xsl:for-each>
					</xsl:variable>

					<xsl:for-each
						select="exslt:node-set($breakdowns)/breakdown">
						<xsl:variable name="pos" select="position()" />
						<xsl:if
							test="$pos = 1 or @name != current()/preceding-sibling::breakdown[1]/@name">
							<xsl:variable name="subset"
								select="exslt:node-set($breakdowns)/breakdown[@name = current()/@name]" />
							<xsl:call-template name="output-scores">
								<xsl:with-param name="title"
									select="@name" />
								<xsl:with-param name="tp"
									select="sum(exslt:node-set($subset)/@tp)" />
								<xsl:with-param name="fp"
									select="sum(exslt:node-set($subset)/@fp)" />
								<xsl:with-param name="fn"
									select="sum(exslt:node-set($subset)/@fn)" />
								<xsl:with-param name="alpha"
									select="$alphaValue" />
							</xsl:call-template>
						</xsl:if>
					</xsl:for-each>

					<xsl:for-each select="performance/run">
						<xsl:call-template name="output-scores">
							<xsl:with-param name="title"
								select="'TOTAL'" />
							<xsl:with-param name="tp"
								select="sum(document/RelAttribute/@tp)" />
							<xsl:with-param name="fp"
								select="sum(document/RelAttribute/@fp)" />
							<xsl:with-param name="fn"
								select="sum(document/RelAttribute/@fn)" />
							<xsl:with-param name="alpha"
								select="$alphaValue" />
						</xsl:call-template>
					</xsl:for-each>

				</result>

				<result type="Relation Properties">

					<!-- Property IsDirect=Direct -->
					<xsl:variable name="direct"
						select="performance/run/document/RelProperty/breakdown[@IsDirect='Direct']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title" select="'Direct'" />
						<xsl:with-param name="tp"
							select="sum($direct/@tp)" />
						<xsl:with-param name="fp"
							select="sum($direct/@fp)" />
						<xsl:with-param name="fn"
							select="sum($direct/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>
					<!-- Property IsDirect=NotDirect -->
					<xsl:variable name="notdirect"
						select="performance/run/document/RelProperty/breakdown[@IsDirect='NotDirect']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title"
							select="'NotDirect'" />
						<xsl:with-param name="tp"
							select="sum($notdirect/@tp)" />
						<xsl:with-param name="fp"
							select="sum($notdirect/@fp)" />
						<xsl:with-param name="fn"
							select="sum($notdirect/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>

					<!-- Property IsProven=Proven -->
					<xsl:variable name="proven"
						select="performance/run/document/RelProperty/breakdown[@IsProven='Proven']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title" select="'Proven'" />
						<xsl:with-param name="tp"
							select="sum($proven/@tp)" />
						<xsl:with-param name="fp"
							select="sum($proven/@fp)" />
						<xsl:with-param name="fn"
							select="sum($proven/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>
					<!-- Property IsProven=Referenced -->
					<xsl:variable name="referenced"
						select="performance/run/document/RelProperty/breakdown[@IsProven='Referenced']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title"
							select="'Referenced'" />
						<xsl:with-param name="tp"
							select="sum($referenced/@tp)" />
						<xsl:with-param name="fp"
							select="sum($referenced/@fp)" />
						<xsl:with-param name="fn"
							select="sum($referenced/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>
					<!-- Property IsProven=Unspecified -->
					<xsl:variable name="unspec"
						select="performance/run/document/RelProperty/breakdown[@IsProven='Unspecified']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title"
							select="'Unspecified'" />
						<xsl:with-param name="tp"
							select="sum($unspec/@tp)" />
						<xsl:with-param name="fp"
							select="sum($unspec/@fp)" />
						<xsl:with-param name="fn"
							select="sum($unspec/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>
					<!-- Property IsPositive=Positive -->
					<xsl:variable name="positive"
						select="performance/run/document/RelProperty/breakdown[@IsPositive='Positive']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title"
							select="'Positive'" />
						<xsl:with-param name="tp"
							select="sum($positive/@tp)" />
						<xsl:with-param name="fp"
							select="sum($positive/@fp)" />
						<xsl:with-param name="fn"
							select="sum($positive/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>
					<!-- Property IsPositive=Negative -->
					<xsl:variable name="negative"
						select="performance/run/document/RelProperty/breakdown[@IsPositive='Negative']" />
					<xsl:call-template name="output-scores">
						<xsl:with-param name="title"
							select="'Negative'" />
						<xsl:with-param name="tp"
							select="sum($negative/@tp)" />
						<xsl:with-param name="fp"
							select="sum($negative/@fp)" />
						<xsl:with-param name="fn"
							select="sum($negative/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>

					<xsl:call-template name="output-scores">
						<xsl:with-param name="title" select="'TOTAL'" />
						<xsl:with-param name="tp"
							select="sum($direct/@tp) + sum($notdirect/@tp) + sum($proven/@tp) + sum($referenced/@tp) + sum($unspec/@tp) + sum($positive/@tp) + sum($negative/@tp)" />
						<xsl:with-param name="fp"
							select="sum($direct/@fp) + sum($notdirect/@fp) + sum($proven/@fp) + sum($referenced/@fp) + sum($unspec/@fp) + sum($positive/@fp) + sum($negative/@fp)" />
						<xsl:with-param name="fn"
							select="sum($direct/@fn) + sum($notdirect/@fn) + sum($proven/@fn) + sum($referenced/@fn) + sum($unspec/@fn) + sum($positive/@fn) + sum($negative/@fn)" />
						<xsl:with-param name="alpha"
							select="$alphaValue" />
					</xsl:call-template>

				</result>

			</results>
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
		<xsl:param name="title" />
		<xsl:param name="tp" />
		<xsl:param name="fp" />
		<xsl:param name="fn" />
		<xsl:param name="alpha" />
		
		<xsl:variable name="number">
		    <xsl:value-of select="$tp + $fp + $fn" />
		</xsl:variable>
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

        <breakdown type="{$title}" number="{$number}" tp="{$tp}" fp="{$fp}" fn="{$fn}" p="{$precision}" r="{$recall}" f1="{$fmeasure}" />
	</xsl:template>


</xsl:stylesheet>

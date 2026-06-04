/**
 * 
 */
package uk.ac.ed.ltg.jdm.io.states;

import static uk.ac.ed.ltg.jdm.io.XmlAttributeType.REFERENCED_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import uk.ac.ed.ltg.jdm.io.DocumentHandler;
import uk.ac.ed.ltg.jdm.io.XmlAttributeType;
import uk.ac.ed.ltg.jdm.io.XmlElementType;
import uk.ac.ed.ltg.jdm.textdata.AbstractElement;
import uk.ac.ed.ltg.jdm.textdata.CharSpan;
import uk.ac.ed.ltg.jdm.textdata.Document;
import uk.ac.ed.ltg.jdm.textdata.Enamex;
import uk.ac.ed.ltg.jdm.textdata.Entity;
import uk.ac.ed.ltg.jdm.textdata.Normalisation;
import uk.ac.ed.ltg.jdm.textdata.PalSnippet;
import uk.ac.ed.ltg.jdm.textdata.Reference;
import uk.ac.ed.ltg.jdm.textdata.Relation;
import uk.ac.ed.ltg.jdm.textdata.Sentence;
import uk.ac.ed.ltg.jdm.textdata.Snippet;
import uk.ac.ed.ltg.jdm.textdata.SurfaceForm;
import uk.ac.ed.ltg.jdm.textdata.TextSection;
import uk.ac.ed.ltg.jdm.textdata.Token;
import uk.ac.ed.ltg.jdm.textdata.Zone;

/**
 * @author rshen
 * 
 */
public class StandoffState extends HandlerState {
	protected class Tuple<S, T> {
		private final S s;

		private final T t;

		public Tuple(final S s, final T t) {
			this.s = s;
			this.t = t;
		}

		public S getS() {
			return s;
		}

		public T getT() {
			return t;
		}
	}

	private static Logger logger = Logger.getLogger(StandoffState.class);

	private static StandoffState instance = null;

	public static StandoffState getInstance(final int version) {
		switch (version) {
		case Document.VERSION_TWO:
			instance = new V2StandoffState();
			break;
		case Document.VERSION_THREE:
			instance = new V3StandoffState();
			break;
		case Document.VERSION_FOUR:
			instance = new V4StandoffState();
			break;
		case Document.VERSION_FIVE:
			instance = new V5StandoffState();
			break;
		}
		return instance;
	}

	protected String entitySource = null;

	protected String relationSource = null;

	protected Stack<Tuple<String, String>> parent = new Stack<Tuple<String, String>>();

	protected Entity currentEntity = null;

	protected Snippet currentSnippet = null;

	protected PalSnippet currentPalSnippet = null;

	protected Relation currentRelation = null;

	protected SurfaceForm currentSurfaceForm = null;

	protected Zone currentZone = null;

	protected int partIndex = 0;

	protected String endOffset = null;

	@Override
	public void characters(final DocumentHandler handler,
			final Document document, final char[] ch, final int start,
			final int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(final DocumentHandler handler,
			final Document document, final String elemName) {
		// TODO Auto-generated method stub
		if (XmlElementType.ENTITY.name.equals(elemName)) {
			handleEndEntity(handler, document, elemName);
		} else if (XmlElementType.PART.name.equals(elemName)) {
			handleEndPart(handler, document, elemName);
		} else if (XmlElementType.SNIPPET.name.equals(elemName)) {
			handleEndSnippet(handler, document, elemName);
		} else if (XmlElementType.PAL_SNIPPET.name.equals(elemName)) {
			handleEndPalSnippet(handler, document, elemName);
		} else if (XmlElementType.RELATION.name.equals(elemName)) {
			handleEndRelation(handler, document, elemName);
		} else if (XmlElementType.ARGUMENT.name.equals(elemName)) {
			handleEndArgument(handler, document, elemName);
		} else if (XmlElementType.SURFACEFORM.name.equals(elemName)) {
			handleEndSurfaceForm(handler, document, elemName);
		} else if (XmlElementType.ZONE.name.equals(elemName)) {
			handleEndZone(handler, document, elemName);
		} else if (XmlElementType.STANDOFF_SECTION.name.equals(elemName)) {
			handler.setState(DocumentState.getInstance());
		}

		if (!parent.isEmpty()) {
			parent.pop();
		}
	}

	protected void handleEndArgument(final DocumentHandler handler,
			final Document document, final String elemName) {

	}

	protected void handleEndEntity(final DocumentHandler handler,
			final Document document, final String elemName) {

	}

	protected void handleEndPart(final DocumentHandler handler,
			final Document document, final String elemName) {
		partIndex++;
	}

	protected void handleEndSnippet(final DocumentHandler handler,
			final Document document, final String elemName) {
	}

	protected void handleEndPalSnippet(final DocumentHandler handler,
			final Document document, final String elemName) {
	}

	protected void handleEndRelation(final DocumentHandler handler,
			final Document document, final String elemName) {
		document.getStandoffSection().addRelation(relationSource,
				currentRelation);
	}

	protected void handleEndSurfaceForm(final DocumentHandler handler,
			final Document document, final String elemName) {
		document.getStandoffSection().addSurfaceForm(currentSurfaceForm);
	}

	protected void handleEndZone(final DocumentHandler handler,
			final Document document, final String elemName) {
		if (endOffset != null && endOffset.length() > 0) {
			int intEo = Integer.parseInt(endOffset);
			if (intEo > 0) {
				intEo = -1 * intEo;
			}
			currentZone.addAttribute(XmlAttributeType.END_OFFSET.name, Integer
					.toString(intEo));
		}
		document.getStandoffSection().addZone(currentZone);
	}

	protected void handleStartArgument(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final String refId = attrs
				.getValue(XmlAttributeType.REFERENCED_ID.name);
		if (currentRelation.getArgument1() == null) {
			final AbstractElement arg1 = handler.getIdMap().get(refId);
			currentRelation.setArgument1(new Reference(document, arg1, refId,
					"", getAttributesMap(attrs)));
		} else if (currentRelation.getArgument2() == null) {
			final AbstractElement arg2 = handler.getIdMap().get(refId);
			currentRelation.setArgument2(new Reference(document, arg2, refId,
					"", getAttributesMap(attrs)));
			final Reference arg1 = currentRelation.getArgument1();

			if (arg1.getRef() != null && arg2 != null) {
				if (arg1.getRef().compareTo(arg2) <= 0) {
					currentRelation
							.setStartIndex(arg1.getRef().getStartIndex());
					currentRelation.setEndIndex(arg2.getEndIndex());
				} else {
					currentRelation.setStartIndex(arg2.getStartIndex());
					currentRelation.setEndIndex(arg1.getRef().getEndIndex());
				}
			}

			if ((arg1.getRef() instanceof Entity) && (arg2 instanceof Entity)) {
				((Entity) arg1.getRef()).addRelatedEntity((Entity) arg2);
				((Entity) arg2).addRelatedEntity((Entity) arg1.getRef());
			}
		}
	}

	protected void handleStartAttribute(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final Tuple<String, String> parentElem = parent.peek();
		final String type = attrs.getValue(XmlAttributeType.NAME.name);
		AbstractElement arg1 = null;
		if (XmlElementType.RELATION.name.equals(parentElem.getS())) {
			arg1 = currentRelation;
		} else if (XmlElementType.ARGUMENT.name.equals(parentElem.getS())) {
			arg1 = handler.getIdMap().get(parentElem.getT());
		}

		AbstractElement arg2 = handler.getIdMap().get(
				attrs.getValue(XmlAttributeType.REFERENCED_ID.name));

		if (arg2 == null) {
			logger.error("Argument 2 is null: "
					+ attrs.getValue(REFERENCED_ID.name));
			arg2 = new Reference(document, attrs
					.getValue(XmlAttributeType.REFERENCED_ID.name), "",
					getAttributesMap(attrs));
		}
		final Relation secRelation = new Relation(document, type,
				new Reference(document, arg1, parentElem.getT(), "", arg1
						.getAttributes()), new Reference(document, arg2, attrs
						.getValue(XmlAttributeType.REFERENCED_ID.name), "",
						getAttributesMap(attrs)));
		// Finally point primary relation to secondary relation
		currentRelation.addPointedby(secRelation);
	}

	protected void handleStartEntity(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {

	}

	protected void handleStartNormalisation(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final String entityType = attrs.getValue("type");
		final String norm = attrs.getValue("norm");
		final float conf = Float.parseFloat(attrs.getValue("conf"));
		final String strRank = attrs.getValue("rank");
		final int rank = (strRank == null) || (strRank.length() == 0) ? -1
				: Integer.parseInt(strRank);
		final String normSource = attrs.getValue("source");
		final Normalisation normalisation = new Normalisation(document,
				entityType, norm, conf, rank, normSource);

		final Tuple<String, String> parentElem = parent.peek();
		if (XmlElementType.ENTITY.name.equals(parentElem.getS())) {
			currentEntity.addNormalisation(normalisation);
		} else if (XmlElementType.SURFACEFORM.name.equals(parentElem.getS())) {
			currentSurfaceForm.addNormalisation(normalisation);
		}
	}

	protected void handleStartPart(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final Tuple<String, String> parentElem = parent.peek();
		if (partIndex == 0) {
			final String so = attrs.getValue(XmlAttributeType.START_WORD.name);
			if (so != null && so.length() > 0) {
				if (XmlElementType.ENTITY.name.equals(parentElem.getS())) {
					currentEntity.addAttribute(
							XmlAttributeType.START_WORD.name, so);
				} else if (XmlElementType.ZONE.name.equals(parentElem.getS())) {
					currentZone.addAttribute(XmlAttributeType.START_WORD.name,
							so);
				}
			}
		}

		final String sw = attrs.getValue("sw");
		final String ew = attrs.getValue("ew");
		final Token swToken = (Token) handler.getIdMap().get(sw);
		final Token ewToken = (Token) handler.getIdMap().get(ew);
		final int startIndex = (swToken == null) ? -1 : swToken.getStartIndex();
		final int endIndex = (ewToken == null) ? -1 : ewToken.getEndIndex();
		if ((startIndex == -1) || (endIndex == -1)) {
			logger.warn("Entity has non existent start or end word "
					+ "token reference - it will be rejected!");
		} else {
			if (XmlElementType.ENTITY.name.equals(parentElem.getS())) {
				currentEntity.addPart(new CharSpan(startIndex, endIndex));
			} else if (XmlElementType.ZONE.name.equals(parentElem.getS())) {
				currentZone.addPart(new CharSpan(startIndex, endIndex));
			}
		}

		endOffset = attrs.getValue(XmlAttributeType.END_OFFSET.name);
	}

	protected void handleStartSnippet(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final Map<String, String> attributes = getAttributesMap(attrs);
		String sw = currentEntity.getAttribute("sw");
		final Token swToken = (Token) handler.getIdMap().get(sw);

		TextSection ts = document.getTextSection();
		List<Sentence> sentences = ts.getSentences();
		Sentence snippetSentence = swToken.getParentFromList(sentences);
		currentSnippet = new Snippet(document, null, null, attributes,
				snippetSentence);
		currentEntity.setSnippet(currentSnippet);
	}

	protected void handleStartPalSnippet(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {

		String sIdStart = attrs.getValue("start");
		String sIdEnd = attrs.getValue("end");

		List<Sentence> palSnippetSentences = new ArrayList<Sentence>();
		Sentence snippetSentence = (Sentence) handler.getIdMap().get(sIdStart);
		palSnippetSentences.add(snippetSentence);

		addRemaingingSnippetSentences(handler, palSnippetSentences, sIdStart,
				sIdEnd);
		int firstSentenceSO = palSnippetSentences.get(0).getStartIndex();
		int lastSentenceEO = palSnippetSentences.get(
				palSnippetSentences.size() - 1).getEndIndex();

		final Map<String, String> attributes = getAttributesMap(attrs);
		attributes.put("so", Integer.toString(firstSentenceSO));
		attributes.put("eo", Integer.toString(lastSentenceEO));

		currentPalSnippet = new PalSnippet(document, null, null, attributes);
		currentPalSnippet.setSnippetSentences(palSnippetSentences);

		List<Enamex> palSnippetEnts = new ArrayList<Enamex>();
		TextSection ts = document.getTextSection();
		List<Enamex> enamexes = ts.getExamexes();

		for (Enamex enamex : enamexes) {
			int pseSO = enamex.getStartIndex();
			int pseEO = enamex.getEndIndex();
			if (pseSO >= firstSentenceSO && pseEO <= lastSentenceEO) {
				palSnippetEnts.add(enamex);
			}
		}
		currentPalSnippet.setSnippetEntities(palSnippetEnts);

		List<Token> toks = document.getTextSection().getTokens();
		List<Token> palSnippetTokens = new ArrayList<Token>();
		for (Token t : toks) {
			for (Sentence s : palSnippetSentences) {
				if (t.getStartIndex() >= s.getStartIndex()
						&& t.getEndIndex() <= s.getEndIndex()) {
					palSnippetTokens.add(t);
					break;
				}
			}

		}
		currentPalSnippet.setSnippetTokens(palSnippetTokens);

		currentEntity.setPalSnippet(currentPalSnippet);

		// String gazID = currentEntity.getAttribute("gazref");
		// if (gazID.startsWith("pg")) {
		// String sw = currentEntity.getAttribute("sw");
		// final Token swToken = (Token) handler.getIdMap().get(sw);
		// TextSection ts = document.getTextSection();
		// List<Sentence> sentences = ts.getSentences();
		// List<Paragraph> paragraphs = ts.getParagraphs();
		// Sentence snippetSentence = swToken.getParentFromList(sentences);
		// Paragraph paragraph = snippetSentence.getParentFromList(paragraphs);
		// String sIdentifier = snippetSentence.getId();
		// int sIdNumber = Integer.parseInt((String) sIdentifier.subSequence(
		// 1, sIdentifier.length()));
		// List<Sentence> palSnippetSentences = new ArrayList<Sentence>();
		// if (sIdNumber > 1) {
		// String sIdBefore;
		// sIdBefore = "s" + Integer.toString(sIdNumber - 1);
		// Sentence previousSentence = (Sentence) handler.getIdMap().get(
		// sIdBefore);
		// Paragraph paraOfPreviousSentence = previousSentence
		// .getParentFromList(paragraphs);
		// if (paragraph.equals(paraOfPreviousSentence)) {
		// palSnippetSentences.add(previousSentence);
		// // System.err.println(previousSentence);
		// }
		// }
		// palSnippetSentences.add(snippetSentence);
		// // System.err.println(snippetSentence);
		//
		// int numberOfSentences = sentences.size();
		// Sentence lastSentence = sentences.get(numberOfSentences - 1);
		// String lastSentenceId = lastSentence.getId();
		// // System.err.println(lastSentenceId);
		// int lastSentencIdNumber = Integer.parseInt((String) lastSentenceId
		// .subSequence(1, lastSentenceId.length()));
		// if (sIdNumber < lastSentencIdNumber) {
		// String sIdAfter;
		// sIdAfter = "s" + Integer.toString(sIdNumber + 1);
		// Sentence nextSentence = (Sentence) handler.getIdMap().get(
		// sIdAfter);
		// Paragraph paraOfNextSentence = nextSentence
		// .getParentFromList(paragraphs);
		// if (paragraph.equals(paraOfNextSentence)) {
		// palSnippetSentences.add(nextSentence);
		// // System.err.println(nextSentence);
		// }
		// }
		//
		// final Map<String, String> attributes = getAttributesMap(attrs);
		// currentPalSnippet = new PalSnippet(document, null, null,
		// attributes, palSnippetSentences);
		// currentEntity.setPalSnippet(currentPalSnippet);
		// }
	}

	private void addRemaingingSnippetSentences(final DocumentHandler handler,
			List<Sentence> palSnippetSentences, String sIdFirst, String sIdLast) {
		if (!sIdFirst.equals(sIdLast)) {
			int sIdNumber = Integer.parseInt((String) sIdFirst.subSequence(1,
					sIdFirst.length()));
			String sIdNext = "s" + Integer.toString(sIdNumber + 1);
			Sentence nextSnippetSentence = (Sentence) handler.getIdMap().get(
					sIdNext);
			palSnippetSentences.add(nextSnippetSentence);
			addRemaingingSnippetSentences(handler, palSnippetSentences,
					sIdNext, sIdLast);
		}
	}

	protected void handleStartRelation(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);
		currentRelation = new Relation(document, id, type, otherAttrs, null,
				null);
	}

	protected void handleStartSurfaceForm(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);
		final String text = otherAttrs.remove("text");
		currentSurfaceForm = new SurfaceForm(document, id, type, otherAttrs,
				text);
	}

	protected void handleStartZone(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		partIndex = 0;

		final String id = attrs.getValue("id");
		final String type = attrs.getValue("type");
		final Map<String, String> otherAttrs = getAttributesMap(attrs);
		currentZone = new Zone(document, id, type, otherAttrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.ed.ltg.jdm.textdata.biomed.states.HandlerState#handleElement
	 * (uk.ac.ed.ltg.jdm.textdata.biomed.DocumentHandler,
	 * uk.ac.ed.ltg.jdm.textdata.Document, java.lang.String,
	 * org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(final DocumentHandler handler,
			final Document document, final String elemName,
			final Attributes attrs) {
		if (XmlElementType.ENTITY_SECTION.name.equals(elemName)) {
			entitySource = attrs.getValue("source");
			if (entitySource == null || entitySource.equals("")) {
				entitySource = Entity.GOLD;
			}
		} else if (XmlElementType.ENTITY.name.equals(elemName)) {
			handleStartEntity(handler, document, elemName, attrs);
		} else if (XmlElementType.PART.name.equals(elemName)) {
			handleStartPart(handler, document, elemName, attrs);
		} else if (XmlElementType.SNIPPET.name.equals(elemName)) {
			handleStartSnippet(handler, document, elemName, attrs);
		} else if (XmlElementType.PAL_SNIPPET.name.equals(elemName)) {
			handleStartPalSnippet(handler, document, elemName, attrs);
		} else if (XmlElementType.NORMALISATION.name.equals(elemName)) {
			handleStartNormalisation(handler, document, elemName, attrs);
		} else if (XmlElementType.RELATIONS_SECTION.name.equals(elemName)) {
			relationSource = attrs.getValue("source");
			if (relationSource == null || relationSource.equals("")) {
				relationSource = Relation.DEFAULT;
			}
		} else if (XmlElementType.RELATION.name.equals(elemName)) {
			handleStartRelation(handler, document, elemName, attrs);
		} else if (XmlElementType.ARGUMENT.name.equals(elemName)) {
			handleStartArgument(handler, document, elemName, attrs);
		} else if (XmlElementType.ATTRIBUTE.name.equals(elemName)) {
			handleStartAttribute(handler, document, elemName, attrs);
		} else if (XmlElementType.SURFACEFORM.name.equals(elemName)) {
			handleStartSurfaceForm(handler, document, elemName, attrs);
		} else if (XmlElementType.ZONE.name.equals(elemName)) {
			handleStartZone(handler, document, elemName, attrs);
		}

		if (!XmlElementType.PARTS_SECTION.name.equals(elemName)) {
			parent.push(new Tuple<String, String>(elemName, attrs
					.getValue(XmlAttributeType.REFERENCED_ID.name)));
		}
	}
}

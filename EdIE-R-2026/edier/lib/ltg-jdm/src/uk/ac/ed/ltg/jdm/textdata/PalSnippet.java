package uk.ac.ed.ltg.jdm.textdata;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author balex
 * 
 */
public class PalSnippet extends AbstractElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5578625746696636557L;

	private List<Sentence> snippetSentences;

	private List<Token> snippetTokens = new ArrayList<Token>();

	private List<Enamex> snippetEntities = new ArrayList<Enamex>();

	private String snippetText;

	private final ArrayList<String> features = new ArrayList<String>();

	private double iScore;

	public PalSnippet(Document document, String id, String type,
			Map<String, String> attributes) {
		super(document, id, type, attributes);
		this.iScore = 0.0;
	}

	public PalSnippet(Document document, String id, String type,
			Map<String, String> attributes, List<Sentence> snippetSentences) {
		super(document, id, type, attributes);
		this.snippetSentences = snippetSentences;
		this.snippetText = deriveSnippetText(snippetSentences);
	}

	private String deriveSnippetText(List<Sentence> snippetSentences) {
		String palSnippetString = "";
		boolean isFirst = true;
		for (Sentence palSnippetSentence : snippetSentences) {
			if (isFirst) {
				palSnippetString = palSnippetString.concat(palSnippetSentence
						.getText());
				isFirst = false;
			} else {
				if (!palSnippetString.endsWith(" ")) {
					palSnippetString = palSnippetString.concat(" "
							+ palSnippetSentence.getText());
				}
			}

		}
		String normalisedPalsSnippetString = palSnippetString.replaceAll(" \n",
				" ").replaceAll("\n", "");
		return normalisedPalsSnippetString;
	}

	public String getNormalisedText() {
		String normalisedSnippetText = this.snippetText.replaceAll("\n", "");
		return normalisedSnippetText;
	}

	@Override
	public String getText() {
		return this.snippetText;
	}

	public double getScore() {
		return this.iScore;
	}

	public String getName() {
		return null;
	}

	public List<Sentence> getSnippetSentences() {
		return this.snippetSentences;
	}

	public List<Enamex> getSnippetEntities() {
		return this.snippetEntities;
	}

	public ArrayList<String> getFeatures() {
		return this.features;
	}

	public void setSnippetSentences(final List<Sentence> snippetSentences) {
		this.snippetSentences = snippetSentences;
		this.snippetText = deriveSnippetText(snippetSentences);
	}

	public void setSnippetSentences(final long iScore) {
		this.iScore = iScore;
	}

	public void setText(String text) {
		this.snippetText = text;
	}

	public void setSnippetEntities(List<Enamex> snippetEntities) {
		this.snippetEntities = snippetEntities;
	}

	public void setSnippetTokens(List<Token> snippetTokens) {
		this.snippetTokens = snippetTokens;
	}

	public void setScore(final Double iScore) {
		DecimalFormat df = new DecimalFormat("#.##");
		double roundedScore = Double.valueOf(df.format(iScore));
		this.iScore = roundedScore;
	}

	public List<Token> getSnippetTokens() {
		return this.snippetTokens;
	}

	public void addFeature(final String feature) {
		this.features.add(feature);
	}

}

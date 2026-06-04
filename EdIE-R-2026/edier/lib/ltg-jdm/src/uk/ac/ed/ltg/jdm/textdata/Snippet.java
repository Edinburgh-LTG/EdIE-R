package uk.ac.ed.ltg.jdm.textdata;

import java.util.Map;

/**
 * @author balex
 * 
 */
public class Snippet extends AbstractElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5578625746696636557L;

	private Sentence snippetSentence;

	private String snippetText;

	public Snippet(Document document, String id, String type,
			Map<String, String> attributes) {
		super(document, id, type, attributes);
	}

	public Snippet(Document document, String id, String type,
			Map<String, String> attributes, Sentence snippetSentence) {
		super(document, id, type, attributes);
		this.snippetSentence = snippetSentence;
		this.snippetText = snippetSentence.getText();
	}

	@Override
	public String getText() {
		return this.snippetText;
	}

	public String getName() {
		return null;
	}

	public void setSnippetSentence(final Sentence snippetSentence) {
		this.snippetSentence = snippetSentence;
	}
}

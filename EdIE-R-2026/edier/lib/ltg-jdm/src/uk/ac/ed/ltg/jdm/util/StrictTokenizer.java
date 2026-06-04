package uk.ac.ed.ltg.jdm.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * An abstract base class for strict tokeninisation. Derived from Lucene
 * CharTokeniser
 */
public class StrictTokenizer extends Tokenizer {

	/** The Constant UPPER. */
	static final int UPPER = 1;

	/** The Constant INITUPPER. */
	static final int INITUPPER = 2;

	/** The Constant LOWER. */
	static final int LOWER = 3;

	/** The Constant DIGIT. */
	static final int DIGIT = 4;

	/** The Constant GREEK. */
	static final int GREEK = 5;

	/** The Constant UNKNOWN. */
	static final int UNKNOWN = 6;

	/** The data len. */
	private int offset = 0, bufferIndex = 0, dataLen = 0;

	/** The Constant MAX_WORD_LEN. */
	private static final int MAX_WORD_LEN = 255;

	/** The Constant IO_BUFFER_SIZE. */
	private static final int IO_BUFFER_SIZE = 1024;

	/** The buffer. */
	private final char[] buffer = new char[MAX_WORD_LEN];

	/** The io buffer. */
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

	/** The state. */
	private int state = UNKNOWN;

	/** The init. */
	private boolean init = true;

	/**
	 * Instantiates a new strict tokenizer.
	 * 
	 * @param input
	 *            the input
	 */
	public StrictTokenizer(final Reader input) {
		super(input);
	}

	/**
	 * Break on state.
	 * 
	 * @param state1
	 *            the state1
	 * @param state2
	 *            the state2
	 * 
	 * @return true, if successful
	 */
	protected boolean breakOnState(final int state1, final int state2) {
		if (state1 == INITUPPER)
			return !(state2 == UPPER || state2 == LOWER);
		else
			return state1 != state2;
	}

	/**
	 * Gets the state.
	 * 
	 * @param c
	 *            the c
	 * 
	 * @return the state
	 */
	protected int getState(final char c) {
		if (Character.isDigit(c))
			return DIGIT;
		else if (GreekCharacterUtilities.isGreekCharacter(c))
			return GREEK;
		else if (Character.isUpperCase(c)) {
			if (this.init)
				return INITUPPER;
			else
				return UPPER;
		} else if (Character.isLowerCase(c))
			return LOWER;
		else
			return UNKNOWN;
	}

	/**
	 * Returns true iff a character should be included in a token. This
	 * tokenizer generates as tokens adjacent sequences of characters which
	 * satisfy this predicate. Characters for which this is false are used to
	 * define token boundaries and are not included in tokens.
	 * 
	 * @param c
	 *            the c
	 * 
	 * @return true, if checks if is token char
	 */
	protected boolean isTokenChar(final char c) {
		return Character.isLetterOrDigit(c);
	}

	/**
	 * Returns the next token in the stream, or null at EOS.
	 * 
	 * @return the token
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public final Token next() throws IOException {
		int length = 0;
		int start = this.offset;
		while (true) {
			final char c;

			this.offset++;
			if (this.bufferIndex >= this.dataLen) {
				this.dataLen = this.input.read(this.ioBuffer);
				this.bufferIndex = 0;
			}
			if (this.dataLen == -1) {
				if (length > 0) {
					break;
				} else
					return null;
			} else {
				c = this.ioBuffer[this.bufferIndex];
			}

			if (isTokenChar(c)) { // if it's a token char

				if (length == 0) { // start of token
					start = this.offset - 1;
					this.state = getState(c);
					this.init = false;
				} else {
					final int newState = getState(c);
					if (breakOnState(this.state, newState)) {
						this.offset--;
						break;
					}
					this.state = newState;
				}
				this.bufferIndex++;
				this.buffer[length++] = normalize(c); // buffer it, normalized

				if (length == MAX_WORD_LEN) {
					break;
				}

			} else {
				this.init = true;
				this.bufferIndex++;// at non-Letter w/ chars
				if (length > 0) {
					break;
				}// return 'em
			}
		}

		return new Token(new String(this.buffer, 0, length), start, start
				+ length);
	}

	/**
	 * Called on each token character to normalize it before it is added to the
	 * token. The default implementation does nothing. Subclasses may use this
	 * to, e.g., lowercase tokens.
	 * 
	 * @param c
	 *            the c
	 * 
	 * @return the char
	 */
	protected char normalize(final char c) {
		return c;
	}
}

package uk.ac.ed.ltg.jdm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Stemmer, implementing the Porter Stemming Algorithm
 * 
 * <p>
 * The Stemmer class transforms a word into its root form. The input word can be
 * provided a character at time (by calling add()), or at once by calling one of
 * the various stem(something) methods.
 * 
 * <p>
 * Porter stemmer in Java. The original paper is in Porter, 1980, An algorithm
 * for suffix stripping, Program, Vol. 14, no. 3, pp 130-137,
 * 
 * <p>
 * See also http://www.tartarus.org/~martin/PorterStemmer/index.html
 * 
 * <p>
 * Bug 1 (reported by Gonzalo Parra 16/10/99) fixed as marked below. Tthe words
 * 'aed', 'eed', 'oed' leave k at 'a' for step 3, and b[k-1] is then out outside
 * the bounds of b.
 * 
 * <p>
 * Similarly,
 * 
 * <p>
 * Bug 2 (reported by Steve Dyrdahl 22/2/00) fixed as marked below. 'ion' by
 * itself leaves j = -1 in the test for 'ion' in step 5, and b[j] is then
 * outside the bounds of b.
 * 
 * <p>
 * Release 3.
 * 
 * <p>
 * [ This version is derived from Release 3, modified by Brian Goetz to optimize
 * for fewer object creations. ]
 * 
 * @author mmatsews
 */

public class PorterStemmer {

	/**
	 * Test program for demonstrating the Stemmer. It reads a file and stems
	 * each word, writing the result to standard out. Usage: Stemmer file-name
	 */
	public static void main(final String[] args) {
		final PorterStemmer s = new PorterStemmer();

		System.out.println(s.stem("Conclusion"));
		System.out.println(s.stem("CONCLUSIONS"));
		System.out.println(s.stem("Conclusions"));

		for (final String arg : args) {
			try {
				final InputStream in = new FileInputStream(arg);
				final byte[] buffer = new byte[1024];
				int bufferLen, offset, ch;

				bufferLen = in.read(buffer);
				offset = 0;
				s.reset();

				while (true) {
					if (offset < bufferLen) {
						ch = buffer[offset++];
					} else {
						bufferLen = in.read(buffer);
						offset = 0;
						if (bufferLen < 0) {
							ch = -1;
						} else {
							ch = buffer[offset++];
						}
					}

					if (Character.isLetter((char) ch)) {
						s.add(Character.toLowerCase((char) ch));
					} else {
						s.stem();
						System.out.print(s.toString());
						s.reset();
						if (ch < 0) {
							break;
						} else {
							System.out.print((char) ch);
						}
					}
				}

				in.close();
			} catch (final IOException e) {
				System.out.println("error reading " + arg);
			}
		}
	}

	private char[] b;

	private int i, /* offset into b */
	j, k, k0;

	private boolean dirty = false;

	private static final int INC = 50; /* unit of size whereby b is increased */

	private static final int EXTRA = 1;

	public PorterStemmer() {
		this.b = new char[INC];
		this.i = 0;
	}

	/**
	 * Add a character to the word being stemmed. When you are finished adding
	 * characters, you can call stem(void) to process the word.
	 */
	public void add(final char ch) {
		if (this.b.length <= this.i + EXTRA) {
			final char[] new_b = new char[this.b.length + INC];
			for (int c = 0; c < this.b.length; c++) {
				new_b[c] = this.b[c];
			}
			this.b = new_b;
		}
		this.b[this.i++] = ch;
	}

	private final boolean cons(final int i) {
		switch (this.b[i]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i == this.k0) ? true : !cons(i - 1);
		default:
			return true;
		}
	}

	private final boolean cvc(final int i) {
		if (i < this.k0 + 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
			return false;
		else {
			final int ch = this.b[i];
			if (ch == 'w' || ch == 'x' || ch == 'y')
				return false;
		}
		return true;
	}

	private final boolean doublec(final int j) {
		if (j < this.k0 + 1)
			return false;
		if (this.b[j] != this.b[j - 1])
			return false;
		return cons(j);
	}

	/* cons(i) is true <=> b[i] is a consonant. */

	private final boolean ends(final String s) {
		final int l = s.length();
		final int o = this.k - l + 1;
		if (o < this.k0)
			return false;
		for (int i = 0; i < l; i++) {
			if (this.b[o + i] != s.charAt(i))
				return false;
		}
		this.j = this.k - l;
		return true;
	}

	/*
	 * m() measures the number of consonant sequences between k0 and j. if c is
	 * a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
	 * presence, <c><v> gives 0 <c>vc<v> gives 1 <c>vcvc<v> gives 2 <c>vcvcvc<v>
	 * gives 3 ....
	 */

	/**
	 * Returns a reference to a character buffer containing the results of the
	 * stemming process. You also need to consult getResultLength() to determine
	 * the length of the result.
	 */
	public char[] getResultBuffer() {
		return this.b;
	}

	/* vowelinstem() is true <=> k0,...j contains a vowel */

	/**
	 * Returns the length of the word resulting from the stemming process.
	 */
	public int getResultLength() {
		return this.i;
	}

	/* doublec(j) is true <=> j,(j-1) contain a double consonant. */

	private final int m() {
		int n = 0;
		int i = this.k0;
		while (true) {
			if (i > this.j)
				return n;
			if (!cons(i)) {
				break;
			}
			i++;
		}
		i++;
		while (true) {
			while (true) {
				if (i > this.j)
					return n;
				if (cons(i)) {
					break;
				}
				i++;
			}
			i++;
			n++;
			while (true) {
				if (i > this.j)
					return n;
				if (!cons(i)) {
					break;
				}
				i++;
			}
			i++;
		}
	}

	/*
	 * cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant
	 * and also if the second c is not w,x or y. this is used when trying to
	 * restore an e at the end of a short word. e.g. cav(e), lov(e), hop(e),
	 * crim(e), but snow, box, tray.
	 */
	public String normalize(final String value) {

		return this.stem(value);
	}

	void r(final String s) {
		if (m() > 0) {
			setto(s);
		}
	}

	/*
	 * setto(s) sets (j+1),...k to the characters in the string s, readjusting
	 * k.
	 */

	/**
	 * reset() resets the stemmer so it can stem another word. If you invoke the
	 * stemmer by calling add(char) and then stem(), you must call reset()
	 * before starting another word.
	 */
	public void reset() {
		this.i = 0;
		this.dirty = false;
	}

	/* r(s) is used further down. */

	void setto(final String s) {
		final int l = s.length();
		final int o = this.j + 1;
		for (int i = 0; i < l; i++) {
			this.b[o + i] = s.charAt(i);
		}
		this.k = this.j + l;
		this.dirty = true;
	}

	/*
	 * step1() gets rid of plurals and -ed or -ing. e.g. caresses -> caress
	 * ponies -> poni ties -> ti caress -> caress cats -> cat feed -> feed
	 * agreed -> agree disabled -> disable matting -> mat mating -> mate meeting
	 * -> meet milling -> mill messing -> mess meetings -> meet
	 */

	/**
	 * Stem the word placed into the Stemmer buffer through calls to add().
	 * Returns true if the stemming process resulted in a word different from
	 * the input. You can retrieve the result with
	 * getResultLength()/getResultBuffer() or toString().
	 */
	public boolean stem() {
		return stem(0);
	}

	/* step2() turns terminal y to i when there is another vowel in the stem. */

	/**
	 * Stem a word contained in a char[]. Returns true if the stemming process
	 * resulted in a word different from the input. You can retrieve the result
	 * with getResultLength()/getResultBuffer() or toString().
	 */
	public boolean stem(final char[] word) {
		return stem(word, word.length);
	}

	/*
	 * step3() maps double suffices to single ones. so -ization ( = -ize plus
	 * -ation) maps to -ize etc. note that the string before the suffix must
	 * give m() > 0.
	 */

	/**
	 * Stem a word contained in a leading portion of a char[] array. Returns
	 * true if the stemming process resulted in a word different from the input.
	 * You can retrieve the result with getResultLength()/getResultBuffer() or
	 * toString().
	 */
	public boolean stem(final char[] word, final int wordLen) {
		return stem(word, 0, wordLen);
	}

	/* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */

	/**
	 * Stem a word contained in a portion of a char[] array. Returns true if the
	 * stemming process resulted in a word different from the input. You can
	 * retrieve the result with getResultLength()/getResultBuffer() or
	 * toString().
	 */
	public boolean stem(final char[] wordBuffer, final int offset,
			final int wordLen) {
		reset();
		if (this.b.length < wordLen) {
			final char[] new_b = new char[wordLen + EXTRA];
			this.b = new_b;
		}
		for (int j = 0; j < wordLen; j++) {
			this.b[j] = wordBuffer[offset + j];
		}
		this.i = wordLen;
		return stem(0);
	}

	/* step5() takes off -ant, -ence etc., in context <c>vcvc<v>. */

	public boolean stem(final int i0) {
		this.k = this.i - 1;
		this.k0 = i0;
		if (this.k > this.k0 + 1) {
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		// Also, a word is considered dirty if we lopped off letters
		// Thanks to Ifigenia Vairelles for pointing this out.
		if (this.i != this.k + 1) {
			this.dirty = true;
		}
		this.i = this.k + 1;
		return this.dirty;
	}

	/* step6() removes a final -e if m() > 1. */

	/**
	 * Stem a word provided as a String. Returns the result as a String.
	 */
	public String stem(final String s) {
		if (stem(s.toCharArray(), s.length()))
			return toString();
		else
			return s;
	}

	private final void step1() {
		if (this.b[this.k] == 's') {
			if (ends("sses")) {
				this.k -= 2;
			} else if (ends("ies")) {
				setto("i");
			} else if (this.b[this.k - 1] != 's') {
				this.k--;
			}
		}
		if (ends("eed")) {
			if (m() > 0) {
				this.k--;
			}
		} else if ((ends("ed") || ends("ing")) && vowelinstem()) {
			this.k = this.j;
			if (ends("at")) {
				setto("ate");
			} else if (ends("bl")) {
				setto("ble");
			} else if (ends("iz")) {
				setto("ize");
			} else if (doublec(this.k)) {
				final int ch = this.b[this.k--];
				if (ch == 'l' || ch == 's' || ch == 'z') {
					this.k++;
				}
			} else if (m() == 1 && cvc(this.k)) {
				setto("e");
			}
		}
	}

	private final void step2() {
		if (ends("y") && vowelinstem()) {
			this.b[this.k] = 'i';
			this.dirty = true;
		}
	}

	private final void step3() {
		if (this.k == this.k0)
			return; /* For Bug 1 */
		switch (this.b[this.k - 1]) {
		case 'a':
			if (ends("ational")) {
				r("ate");
				break;
			}
			if (ends("tional")) {
				r("tion");
				break;
			}
			break;
		case 'c':
			if (ends("enci")) {
				r("ence");
				break;
			}
			if (ends("anci")) {
				r("ance");
				break;
			}
			break;
		case 'e':
			if (ends("izer")) {
				r("ize");
				break;
			}
			break;
		case 'l':
			if (ends("bli")) {
				r("ble");
				break;
			}
			if (ends("alli")) {
				r("al");
				break;
			}
			if (ends("entli")) {
				r("ent");
				break;
			}
			if (ends("eli")) {
				r("e");
				break;
			}
			if (ends("ousli")) {
				r("ous");
				break;
			}
			break;
		case 'o':
			if (ends("ization")) {
				r("ize");
				break;
			}
			if (ends("ation")) {
				r("ate");
				break;
			}
			if (ends("ator")) {
				r("ate");
				break;
			}
			break;
		case 's':
			if (ends("alism")) {
				r("al");
				break;
			}
			if (ends("iveness")) {
				r("ive");
				break;
			}
			if (ends("fulness")) {
				r("ful");
				break;
			}
			if (ends("ousness")) {
				r("ous");
				break;
			}
			break;
		case 't':
			if (ends("aliti")) {
				r("al");
				break;
			}
			if (ends("iviti")) {
				r("ive");
				break;
			}
			if (ends("biliti")) {
				r("ble");
				break;
			}
			break;
		case 'g':
			if (ends("logi")) {
				r("log");
				break;
			}
		default:
			// Do nothing.
			break;
		}
	}

	private final void step4() {
		switch (this.b[this.k]) {
		case 'e':
			if (ends("icate")) {
				r("ic");
				break;
			}
			if (ends("ative")) {
				r("");
				break;
			}
			if (ends("alize")) {
				r("al");
				break;
			}
			break;
		case 'i':
			if (ends("iciti")) {
				r("ic");
				break;
			}
			break;
		case 'l':
			if (ends("ical")) {
				r("ic");
				break;
			}
			if (ends("ful")) {
				r("");
				break;
			}
			break;
		case 's':
			if (ends("ness")) {
				r("");
				break;
			}
			break;
		default:
			// Do nothing.
			break;
		}
	}

	private final void step5() {
		if (this.k == this.k0)
			return; /* for Bug 1 */
		switch (this.b[this.k - 1]) {
		case 'a':
			if (ends("al")) {
				break;
			}
			return;
		case 'c':
			if (ends("ance")) {
				break;
			}
			if (ends("ence")) {
				break;
			}
			return;
		case 'e':
			if (ends("er")) {
				break;
			}
			return;
		case 'i':
			if (ends("ic")) {
				break;
			}
			return;
		case 'l':
			if (ends("able")) {
				break;
			}
			if (ends("ible")) {
				break;
			}
			return;
		case 'n':
			if (ends("ant")) {
				break;
			}
			if (ends("ement")) {
				break;
			}
			if (ends("ment")) {
				break;
			}
			/* element etc. not stripped before the m */
			if (ends("ent")) {
				break;
			}
			return;
		case 'o':
			if (ends("ion") && this.j >= 0
					&& (this.b[this.j] == 's' || this.b[this.j] == 't')) {
				break;
			}
			/* j >= 0 fixes Bug 2 */
			if (ends("ou")) {
				break;
			}
			return;
			/* takes care of -ous */
		case 's':
			if (ends("ism")) {
				break;
			}
			return;
		case 't':
			if (ends("ate")) {
				break;
			}
			if (ends("iti")) {
				break;
			}
			return;
		case 'u':
			if (ends("ous")) {
				break;
			}
			return;
		case 'v':
			if (ends("ive")) {
				break;
			}
			return;
		case 'z':
			if (ends("ize")) {
				break;
			}
			return;
		default:
			return;
		}
		if (m() > 1) {
			this.k = this.j;
		}
	}

	private final void step6() {
		this.j = this.k;
		if (this.b[this.k] == 'e') {
			final int a = m();
			if (a > 1 || a == 1 && !cvc(this.k - 1)) {
				this.k--;
			}
		}
		if (this.b[this.k] == 'l' && doublec(this.k) && m() > 1) {
			this.k--;
		}
	}

	/**
	 * After a word has been stemmed, it can be retrieved by toString(), or a
	 * reference to the internal buffer can be retrieved by getResultBuffer and
	 * getResultLength (which is generally more efficient.)
	 */
	@Override
	public String toString() {
		return new String(this.b, 0, this.i);
	}

	private final boolean vowelinstem() {
		int i;
		for (i = this.k0; i <= this.j; i++) {
			if (!cons(i))
				return true;
		}
		return false;
	}
}

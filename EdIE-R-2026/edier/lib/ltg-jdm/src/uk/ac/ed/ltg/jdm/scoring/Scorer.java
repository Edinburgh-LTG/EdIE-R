/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.List;

/**
 * @author rshen
 * 
 */
public interface Scorer {
	public List<Score> getScores();

	public void score() throws Exception;
}

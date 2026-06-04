/**
 * 
 */
package uk.ac.ed.ltg.jdm.scoring;

import java.util.List;
import java.util.Map;

import uk.ac.ed.ltg.jdm.textdata.Document;

/**
 * @author rshen
 * 
 */
public interface Result {
	public void collectResults(Document gold, Document prediction,
			List<Result> results, Map<String, Object> context);

	public int getEndIndex();

	public Object getGold();

	public Document getGoldDocument();

	public String getGroup();

	public Object getPrediction();

	public Document getPredictionDocument();

	public int getStartIndex();

	public boolean isIncludable();

	public void setGold(Object gold);

	public void setGoldDocument(Document document);

	public void setPrediction(Object prediction);

	public void setPredictionDocument(Document document);
}

package uk.ac.ed.ltg.jdm.textdata;

import java.util.List;

import junit.framework.TestCase;

public class EntityTest extends TestCase {

	Entity entity1;

	Entity entity2;

	Entity entity3;

	Entity entity4;

	@Override
	public void setUp() throws Exception {
		final Document document = new Document();
		document
				.setText(new StringBuilder(
						"This is Joe Bloggs. He lives in Edinburgh. He is awesome at Java and SQL."));
		this.entity1 = new Entity(document, "ent1", "location", null);
		this.entity1.setStartIndex(32);
		this.entity1.setEndIndex(41);
		this.entity2 = new Entity(document, "ent2", "person", null);
		this.entity2.setStartIndex(8);
		this.entity2.setEndIndex(18);
		this.entity3 = new Entity(document, "ent3", "skill", null);
		this.entity3.setStartIndex(60);
		this.entity3.setEndIndex(64);
		this.entity4 = new Entity(document, "ent4", "skill", null);
		this.entity4.setStartIndex(69);
		this.entity4.setEndIndex(72);
	}

	public void testRelatedEntities() {
		this.entity2.addRelatedEntity(this.entity1);
		this.entity2.addRelatedEntity(this.entity3);
		this.entity2.addRelatedEntity(this.entity4);
		assertNotNull(this.entity2.getRelatedEntities());
		assertNotNull(this.entity2.getRelatedEntitiesByType("skill"));
		assertNotNull(this.entity2.getRelatedEntitiesByType("location"));
		assertNull(this.entity2.getRelatedEntitiesByType("date"));
		assertEquals(this.entity2.getRelatedEntitiesByType("location").size(),
				1);
		final List<Entity> skills = this.entity2
				.getRelatedEntitiesByType("skill");
		assertEquals(skills.size(), 2);
		assertEquals(skills.get(0).getType(), "skill");
		assertEquals(skills.get(0).getText(), "Java");
		assertEquals(skills.get(1).getText(), "SQL");
	}

	public void testSimpleNorm() {
		this.entity3.setSimpleNorm("Java");

		assertSame("Java", this.entity3.getSimpleNorm());
	}

	public void testSimpleNormOrText() {
		this.entity3.setSimpleNorm("java");

		assertEquals("java", this.entity3.getSimpleNormOrText());

		assertEquals("SQL", this.entity4.getSimpleNormOrText());
	}

}

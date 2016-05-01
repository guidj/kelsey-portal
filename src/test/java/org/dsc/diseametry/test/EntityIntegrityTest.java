package org.dsc.diseametry.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dsc.diseametry.domain.Indicator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EntityIntegrityTest extends Neo4jTest {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(EntityIntegrityTest.class);

	@Test
	@Transactional
	public void testShouldFindAllIndicatorNames() {
		
		super.seedData("sample/data.json");
		
		@SuppressWarnings("serial")
		Map<String, Set<String>> expectedNames = new HashMap<String, Set<String>>(){{
			put("T400", new HashSet<String>(Arrays.asList(new String[]{"fever"})));
			put("T401", new HashSet<String>(Arrays.asList(new String[]{"concussion", "swollen"})));
			put("T402", new HashSet<String>(Arrays.asList(new String[]{"chills", "calafrios"})));
			put("T403", new HashSet<String>(Arrays.asList(new String[]{"weakness"})));
		}};
		
		for(Map.Entry<String, Set<String>> entry: expectedNames.entrySet()) {
			
			Logger.info("{} expected to have {} names", entry.getKey(), entry.getValue().size());
			
			Indicator indicator = this.dbContext.getIndicatorRepo().findByCui(entry.getKey());
			assertNotNull(indicator);
		
			assertEquals(entry.getValue().size(), indicator.getNames().size());
			
			for(String name: entry.getValue()) {
				assertTrue(indicator.getNames().contains(name));
			}
		}
	}	
}

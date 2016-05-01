package org.dsc.diseametry.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.dsc.diseametry.data.DiseaseWithScoreDTO;
import org.dsc.diseametry.data.IndicatorWithScoreDTO;
import org.dsc.diseametry.domain.Disease;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DiseaseRelationsTest extends Neo4jTest {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(DiseaseRelationsTest.class);

	@Test
	@Transactional
	public void testShouldFindSimilarDiseases() {
		
		super.seedData("sample/data.json");
		
		@SuppressWarnings("serial")
		Map<String, Map<String, Integer>> expectedScores = new HashMap<String, Map<String, Integer>>(){{
			put("C100", new HashMap<String, Integer>(){{put("C101", 2);}});
			put("C101", new HashMap<String, Integer>(){{put("C100", 2); put("C102", 1); put("C103", 1);}});
			put("C102", new HashMap<String, Integer>(){{put("C101", 1); put("C103", 1);}});
			put("C103", new HashMap<String, Integer>(){{put("C101", 1); put("C102", 1);}});
		}};
		
		String cui;
		Integer score;
		
		for(Map.Entry<String, Map<String, Integer>> entry: expectedScores.entrySet()) {
			
			Disease disease = this.dbContext.getDiseaseRepo().findByCui(entry.getKey());
			assertNotNull(disease);
			
			Collection<DiseaseWithScoreDTO> similarDiseases = this.dbContext.getDiseaseRepo().findSimilarDiseases(disease.getCui(), 0, 10);
			
			assertNotNull(similarDiseases);
			
			for(DiseaseWithScoreDTO foundDisease: similarDiseases) {
				cui = foundDisease.getDisease().getCui();
				score = entry.getValue().get(cui);
				assertNotNull(score);
				assertEquals(entry.getValue().get(cui), foundDisease.getScore());
				
				Logger.info("{} similar to {} [score: {}]", entry.getKey(), cui, score);
			}
		}
	}
	
	@Test
	@Transactional
	public void testShouldFindMostConnectingIndicators() {
		
		super.seedData("sample/data.json");
		
		@SuppressWarnings("serial")
		Map<String, Map<String, Integer>> expectedScores = new HashMap<String, Map<String, Integer>>(){{
			put("C100", new HashMap<String, Integer>(){{put("T400", 1); put("T401", 1);}});
			put("C101", new HashMap<String, Integer>(){{put("T400", 1); put("T401", 1); put("T402", 2);}});
			put("C102", new HashMap<String, Integer>(){{put("T402", 2); put("T403", 0);}});
			put("C103", new HashMap<String, Integer>(){{put("T402", 2);}});
		}};
		
		String cui;
		Integer score;
		
		for(Map.Entry<String, Map<String, Integer>> entry: expectedScores.entrySet()) {
			
			Disease disease = this.dbContext.getDiseaseRepo().findByCui(entry.getKey());
			assertNotNull(disease);
			
			Collection<IndicatorWithScoreDTO> connectingIndicators = this.dbContext.getDiseaseRepo().findConnectingIndicators(disease.getCui(), 0, 10);
			
			assertNotNull(connectingIndicators);
			
			for(IndicatorWithScoreDTO foundIndicator: connectingIndicators) {
				cui = foundIndicator.getIndicator().getCui();
				score = entry.getValue().get(cui);
				assertNotNull(score);
				assertEquals(entry.getValue().get(cui), foundIndicator.getScore());
				
				Logger.info("{} connected by {} [score: {}]", entry.getKey(), cui, score);
			}
		}
	}	
}

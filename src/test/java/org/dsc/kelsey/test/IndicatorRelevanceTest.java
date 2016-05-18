package org.dsc.kelsey.test;

import org.dsc.kelsey.data.IndicatorWithScoreDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class IndicatorRelevanceTest extends Neo4jTest {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(IndicatorRelevanceTest.class);

    @Test
    @Transactional
    public void testShouldFindSimilarDiseases() {

        super.seedData("sample/data.json");

        @SuppressWarnings("serial")
        Map<String, Integer> expectedScores = new HashMap<String, Integer>() {{
            put("T400", 2);
            put("T401", 2);
            put("T402", 3);
            put("T403", 1);
        }};

        Collection<IndicatorWithScoreDTO> foundIndicators = this.dbContext.getIndicatorRepo().findMostRelevantIndicators(0, 10);

        assertEquals(4, foundIndicators.size());

        List<IndicatorWithScoreDTO> foundIndicatorsList = new ArrayList<IndicatorWithScoreDTO>();
        Map<String, IndicatorWithScoreDTO> mostRelevantIndicators = new HashMap<String, IndicatorWithScoreDTO>();
        IndicatorWithScoreDTO refIndicatorWithScoreDTO;

        for (Iterator<IndicatorWithScoreDTO> i = foundIndicators.iterator(); i.hasNext(); ) {
            refIndicatorWithScoreDTO = i.next();
            foundIndicatorsList.add(refIndicatorWithScoreDTO);
            mostRelevantIndicators.put(refIndicatorWithScoreDTO.getIndicator().getCui(), refIndicatorWithScoreDTO);
        }

        assertEquals("T402", foundIndicatorsList.get(0).getIndicator().getCui());
        assertEquals("T403", foundIndicatorsList.get(expectedScores.size() - 1).getIndicator().getCui());

        assertNotNull(mostRelevantIndicators);

        IndicatorWithScoreDTO relevantIndicator;

        for (Map.Entry<String, Integer> entry : expectedScores.entrySet()) {

            relevantIndicator = mostRelevantIndicators.get(entry.getKey());
            assertNotNull(relevantIndicator);
            assertEquals(entry.getValue(), relevantIndicator.getScore());

            Logger.info("{} has relevance score of [{}]", entry.getKey(), entry.getValue());
        }
    }
}

package org.dsc.diseametry.test;

import java.util.Map;

import org.dsc.diseametry.data.Document;
import org.dsc.diseametry.data.JsonStreamFileReader;
import org.dsc.diseametry.domain.Disease;
import org.dsc.diseametry.domain.Indicator;
import org.dsc.diseametry.domain.IndicatorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsonParserTest extends Neo4jTest {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(JsonParserTest.class);

	@Test
	@Transactional
	@Rollback(false)
	public void shouldInsertData() {

		Logger.info("Testing data insertion");

		String filePath = this.getClass().getClassLoader().getResource("sample/data.json").getFile();
		JsonStreamFileReader reader = new JsonStreamFileReader(filePath);

		Document doc;
		Disease disease;
		Indicator indicator;

		while ((doc = reader.next()) != null) {

			disease = this.dbContext.getDiseaseRepo().findByCui(doc.getDiseaseCui());

			if (disease == null) {
				disease = new Disease(doc.getDiseaseCui());
			}

			for (String name : doc.getDiseaseNames()) {
				disease.addName(name);
			}

//			this.dbContext.getDiseaseRepo().save(disease);

			for (Map.Entry<String, Document.FoundIndicator> entry : doc.getFoundIndicators().entrySet()) {

				indicator = this.dbContext.getIndicatorRepo().findByCui(entry.getKey());

				if (indicator == null) {
					indicator = new Indicator(entry.getKey());
				}

				for (String name : entry.getValue().getNames()) {
					indicator.addName(name);
				}

//				indicator = this.dbContext.getIndicatorRepo().save(indicator);

				if (entry.getValue().getIndicatorType() == IndicatorType.SIGN_OR_SYMPTOM) {
					// if (!disease.getSymptoms().contains(indicator)) {
					disease.addSymptomOrSign(indicator);
					// }
				} else if (entry.getValue().getIndicatorType() == IndicatorType.FINDING) {
					// if (!disease.getFindings().contains(indicator)) {
					disease.addFinding(indicator);
					// }
				}
			}
			
			this.dbContext.getDiseaseRepo().save(disease);
		}
	}
}

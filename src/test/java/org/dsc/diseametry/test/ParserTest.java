package org.dsc.diseametry.test;

import java.io.File;

import org.dsc.diseametry.DbContext;
import org.dsc.diseametry.domain.Disease;
import org.dsc.diseametry.domain.Indication;
import org.dsc.diseametry.domain.IndicationType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParserTest extends Neo4jTest {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(ParserTest.class);

	@Test
	@Transactional
	public void shouldInsertData() {

		JSONArray objects;
		JSONObject object;
		JSONObject tmpObj;

		Disease disease;
		Indication indication;
		int n;

		String filename = "sample/data.json";
		String filepath = (new File(filename).toString());

		Logger.info("Parsing {}", filename);

		objects = new JSONArray(new String(TestUtils.readResourceAsBytes(filepath)));

		for (int i = 0; i < objects.length(); i++) {
			object = objects.getJSONObject(i);

			disease = new Disease(object.getJSONObject("disease").getString("cui"),
					(object.getJSONObject("disease").getString("name")));

			dbContext.getDiseaseRepo().save(disease);
			
			n = object.getJSONArray("indications").length();

			for (int j = 0; j < n; j++) {
				tmpObj = object.getJSONArray("indications").getJSONObject(j);

				indication = new Indication(tmpObj.getString("cui"), tmpObj.getString("name"),
						IndicationType.fromAbbreviation(tmpObj.getString("type")));
				dbContext.getIndicationRepo().save(indication);
				disease.addIndication(indication);
			}
			
			dbContext.getDiseaseRepo().save(disease);
		}
	}
}

package org.dsc.diseametry.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dsc.diseametry.DbContext;
import org.dsc.diseametry.data.Document;
import org.dsc.diseametry.data.JsonStreamFileReader;
import org.dsc.diseametry.domain.Disease;
import org.dsc.diseametry.domain.Indicator;
import org.dsc.diseametry.domain.IndicatorType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("file:src/main/resources/spring/testContext.xml") })

public abstract class  Neo4jTest extends TestCase {

	@Autowired
	DbContext dbContext;

	private static void recursiveDelete(File file) {
		// to end the recursive loop
		if (!file.exists())
			return;

		// if directory, go inside and call recursively
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				// call recursively
				recursiveDelete(f);
			}
		}
		// call delete to delete files and empty directory
		file.delete();
	}

	protected static void deleteDatabase() {
		File file = new File("target/neo4j-db-test");

		recursiveDelete(file);
	}
	
	protected void seedData(String filename) {

		String filePath = this.getClass().getClassLoader().getResource(filename).getFile();
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

			for (Map.Entry<String, Document.FoundIndicator> entry : doc.getFoundIndicators().entrySet()) {

				indicator = this.dbContext.getIndicatorRepo().findByCui(entry.getKey());

				if (indicator == null) {
					indicator = new Indicator(entry.getKey());
				}

				for (String name : entry.getValue().getNames()) {
					indicator.addName(name);
				}

				if (entry.getValue().getIndicatorType() == IndicatorType.SIGN_OR_SYMPTOM) {
					disease.addSymptomOrSign(indicator);
				} else if (entry.getValue().getIndicatorType() == IndicatorType.FINDING) {
					disease.addFinding(indicator);
				}
				
				this.dbContext.getIndicatorRepo().save(indicator);
			}
			
			this.dbContext.getDiseaseRepo().save(disease);
		}
	}	
	
	protected List<Document> readDocuments(String filename){
		
		String filePath = this.getClass().getClassLoader().getResource(filename).getFile();
		JsonStreamFileReader reader = new JsonStreamFileReader(filePath);

		Document doc;
		List<Document> docs = new ArrayList<Document>();
		
		while ((doc = reader.next()) != null) {
			docs.add(doc);
		}
		
		return docs;
	}

	@Override
	public void setUp() throws Exception {
		deleteDatabase();
	}

	@Override
	public void tearDown() throws Exception {
		deleteDatabase();
	}
}

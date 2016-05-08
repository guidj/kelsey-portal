package org.dsc.diseametry.data;

import java.util.List;
import java.util.Map;

import org.dsc.diseametry.DbContext;
import org.dsc.diseametry.domain.Disease;
import org.dsc.diseametry.domain.Indicator;
import org.dsc.diseametry.domain.IndicatorType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataService {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(DataService.class);

	@Autowired
	private DbContext dbContext;

	@Transactional
	public void indexDocuments(List<Document> documents) {

		Logger.info(String.format("Inserting %s new document(s) into Neo4j", documents.size()));

		Disease disease;
		Indicator indicator;

		for (Document doc : documents) {

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
}

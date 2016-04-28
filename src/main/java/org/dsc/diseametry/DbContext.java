package org.dsc.diseametry;

import org.dsc.diseametry.repositories.DiseaseRepository;
import org.dsc.diseametry.repositories.IndicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

@Service
@ContextConfiguration("file:src/main/resources/spring/context.xml")
public class DbContext {

	@Autowired
	private DiseaseRepository diseaseRepository;

	@Autowired
	private IndicationRepository indicationRepository;

	public DiseaseRepository getDiseaseRepo() {
		return diseaseRepository;
	}

	public IndicationRepository getIndicationRepo() {
		return indicationRepository;
	}
}

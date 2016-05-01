package org.dsc.diseametry;

import org.dsc.diseametry.repositories.DiseaseRepository;
import org.dsc.diseametry.repositories.IndicatiorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
//@ContextConfiguration("file:src/main/resources/spring/context.xml")
@Configuration("file:src/main/resources/spring/context.xml")
public class DbContext {

	@Autowired
	private DiseaseRepository diseaseRepository;

	@Autowired
	private IndicatiorRepository indicatorRepository;

	public DiseaseRepository getDiseaseRepo() {
		return diseaseRepository;
	}

	public IndicatiorRepository getIndicatorRepo() {
		return indicatorRepository;
	}
}

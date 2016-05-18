package org.dsc.kelsey;

import org.dsc.kelsey.repositories.DiseaseRepository;
import org.dsc.kelsey.repositories.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration("file:src/main/resources/spring/context.xml")
public class DbContext {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    public DiseaseRepository getDiseaseRepo() {
        return diseaseRepository;
    }

    public IndicatorRepository getIndicatorRepo() {
        return indicatorRepository;
    }
}

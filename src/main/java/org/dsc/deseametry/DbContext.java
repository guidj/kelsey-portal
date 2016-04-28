//package org.dsc.disymnet;
//
//import org.dsc.disynmnet.repositories.ImageRepository;
//import org.dsc.disynmnet.repositories.WebPageRepository;
//
//public class DBContext {
//
//	private WebPageRepository webPageRepository;
//	private ImageRepository imageRepository;
//
//	public DBContext(WebPageRepository webPageRepository,
//			ImageRepository imageRepository) {
//		super();
//		this.webPageRepository = webPageRepository;
//		this.imageRepository = imageRepository;
//	}
//
//	public WebPageRepository getWebPageRepository() {
//		return webPageRepository;
//	}
//
//	public void setWebPageRepository(WebPageRepository webPageRepository) {
//		this.webPageRepository = webPageRepository;
//	}
//
//	public ImageRepository getImageRepository() {
//		return imageRepository;
//	}
//
//	public void setImageRepository(ImageRepository imageRepository) {
//		this.imageRepository = imageRepository;
//	}
//
//}
package org.dsc.deseametry;

import org.dsc.deseametry.repositories.DiseaseRepository;
import org.dsc.deseametry.repositories.IndicationRepository;
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

package org.dsc.diseametry;

import org.dsc.diseametry.metamap.MetaMapNLP;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {		
        ApplicationContext context = 
                new ClassPathXmlApplicationContext("spring/context.xml");

        MetaMapNLP metaMapNLP = context.getBean(MetaMapNLP.class);
        
        metaMapNLP.run();
	}
}

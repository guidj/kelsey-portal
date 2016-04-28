//package org.dsc.disymnet;
//
//import java.io.File;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.log4j.Logger;
//import org.dsc.disymnet.domain.WebPage;
//import org.dsc.disynmnet.repositories.ImageRepository;
//import org.dsc.disynmnet.repositories.WebPageRepository;
//import org.gp.spyder.crawl.Crawler;
//import org.gp.spyder.crawl.Harvester;
//import org.gp.spyder.crawl.Parser;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//public class App {
//
//	private final static Logger LOGGER = Logger.getLogger(App.class.getName());
//	
//	private final static String DOWNLOAD_DIR = "data";
//    
//    private static void recursiveDelete(File file) {
//        //to end the recursive loop
//        if (!file.exists())
//            return;
//         
//        //if directory, go inside and call recursively
//        if (file.isDirectory()) {
//            for (File f : file.listFiles()) {
//                //call recursively
//                recursiveDelete(f);
//            }
//        }
//        //call delete to delete files and empty directory
//        file.delete();
//    }
//    
//    private static void deleteDatabase(){
//    	File file = new File("target/neo4j-db-plain");
//    	
//    	recursiveDelete(file);
//    }
//    
//    public static void main(String[] args) throws InterruptedException {
//    	
////    	deleteDatabase();
//    	
//        @SuppressWarnings("resource")
//		ApplicationContext context = new ClassPathXmlApplicationContext("spring/context.xml");
//
//        final String baseUrl = "http://www.mangareader.net";
//        final BlockingQueue<String> dataQueue = new LinkedBlockingQueue<String>();
//        final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
//        final BlockingQueue<String> imgQueue = new LinkedBlockingQueue<String>();
//
//        urlQueue.add(baseUrl);
//        
//        DBContext dbContext = new DBContext(
//        		(WebPageRepository)context.getBean("webPageRepository"),
//        		(ImageRepository)context.getBean("imageRepository")
//        		);
//        
//        for (WebPage webPage: dbContext.getWebPageRepository().getWebPages(false, 100)) {
//        	urlQueue.add(webPage.getUrl());
//        }
//        
//        LOGGER.info(String.format("Starting with URL Queue of with %d entries", urlQueue.size()));
//        LOGGER.info("Starting Crawler, Parser and Harvester workers...");        
//        
//        int  corePoolSize  =    6;
//        int  maxPoolSize   =   12;
//        long keepAliveTime = 5000;
//
//        ExecutorService threadPoolExecutor =
//                new ThreadPoolExecutor(
//                        corePoolSize,
//                        maxPoolSize,
//                        keepAliveTime,
//                        TimeUnit.MILLISECONDS,
//                        new LinkedBlockingQueue<Runnable>()
//                        );
//        
//        threadPoolExecutor.submit(new Crawler(dbContext, urlQueue, dataQueue));
//        threadPoolExecutor.submit(new Crawler(dbContext, urlQueue, dataQueue));
//        threadPoolExecutor.submit(new Parser(dbContext, baseUrl, urlQueue, dataQueue, imgQueue));
//        threadPoolExecutor.submit(new Parser(dbContext, baseUrl, urlQueue, dataQueue, imgQueue));
//        threadPoolExecutor.submit(new Harvester(dbContext, DOWNLOAD_DIR, imgQueue));
//        threadPoolExecutor.submit(new Harvester(dbContext, DOWNLOAD_DIR, imgQueue));
//        
//        while(threadPoolExecutor.isTerminated() == false) {
//        	Thread.sleep(1000);
//        }
//        
////        Thread crawler = new Crawler(dbContext, urlQueue, dataQueue);
////        Thread parser = new Parser(dbContext, baseUrl, urlQueue, dataQueue, imgQueue);
////        Thread harvester = new Harvester(dbContext, DOWNLOAD_DIR, imgQueue);      
////        
////        
////        crawler.start();
////        parser.start();
////        harvester.start();
//        
//        //TODO: when to stop?
//    }
//}
package org.dsc.deseametry;

public class App {
	public static void main(String[] args) {
		
	}
}

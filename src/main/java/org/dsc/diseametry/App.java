package org.dsc.diseametry;

import org.apache.commons.cli.*;
import org.dsc.diseametry.data.DiseasePairWithScoreDTO;
import org.dsc.diseametry.data.DiseaseWithScoreDTO;
import org.dsc.diseametry.data.IndicatorWithScoreDTO;
import org.dsc.diseametry.domain.Disease;
import org.dsc.diseametry.domain.Indicator;
import org.dsc.diseametry.metamap.MetaMapNLP;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public class App {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(App.class);

    private static final String CMD_FEED = "feed";
    private static final String CMD_QUERY = "query";
    private static final String CMD_CUI = "cui";
    private static final String CMD_SKIP = "skip";
    private static final String CMD_LIMIT = "limit";

    private static void usage(Options options) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("Diseametry", options);
    }

    @SuppressWarnings("resource")
    private static void runQuery(int q, String cui, int skip, int limit) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/context.xml");

        DbContext dbContext = context.getBean(DbContext.class);


        switch (q) {
            case 1:

                Collection<DiseaseWithScoreDTO> similarDiseases = dbContext.getDiseaseRepo().findSimilarDiseases(cui,
                        skip, limit);

                Logger.info("Found results: ", similarDiseases.size());

                if (!similarDiseases.isEmpty()) {
                    Logger.info("Results: (Disease, Score)");
                    return;
                }

                for (DiseaseWithScoreDTO diseaseWithScore : similarDiseases) {
                    Logger.info(String.format("%s, %s", diseaseWithScore.getDisease().toString(), diseaseWithScore.getScore()));
                }

                break;

            case 2:

                Collection<IndicatorWithScoreDTO> connectingIndicators = dbContext.getDiseaseRepo().findConnectingIndicators(cui,
                        skip, limit);

                Logger.info("Found results: ", connectingIndicators.size());

                if (!connectingIndicators.isEmpty()) {
                    Logger.info("Results: (Indicator, Score)");
                    return;
                }

                for (IndicatorWithScoreDTO connectingIndicator : connectingIndicators) {
                    Logger.info(String.format("%s, %s", connectingIndicator.getIndicator().toString(), connectingIndicator.getScore()));
                }

                break;

            case 3:

                Collection<IndicatorWithScoreDTO> similarIndicators = dbContext.getIndicatorRepo().findSimilarIndicators(cui,
                        skip, limit);

                Logger.info("Found results: ", similarIndicators.size());

                if (!similarIndicators.isEmpty()) {
                    Logger.info("Results: (Indicator, Score)");
                    return;
                }

                for (IndicatorWithScoreDTO similarIndicator : similarIndicators) {
                    Logger.info(String.format("%s, %s", similarIndicator.getIndicator().toString(), similarIndicator.getScore()));
                }

                break;
        }
    }

    @SuppressWarnings("resource")
    private static void runQuery(int q, int skip, int limit) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/context.xml");

        DbContext dbContext = context.getBean(DbContext.class);

        switch (q) {
            case 4:

                Collection<DiseasePairWithScoreDTO> similarDiseases = dbContext.getDiseaseRepo().findSimilarDiseases(skip, limit);

                Logger.info("Found results: ", similarDiseases.size());

                if (!similarDiseases.isEmpty()) {
                    Logger.info("Results: (Disease, OtherDisease, Score)");
                }

                for (DiseasePairWithScoreDTO diseasePairWithScore : similarDiseases) {
                    Logger.info(String.format("%s and %s, %s", diseasePairWithScore.getDisease().toString(),
                            diseasePairWithScore.getOther().toString(),
                            diseasePairWithScore.getScore()));
                }

                break;

            case 5:

                Set<Disease> diseases = dbContext.getDiseaseRepo().get(skip, limit);

                Logger.info("Found results: ", diseases.size());

                if (!diseases.isEmpty()) {
                    Logger.info("Results: Disease(cui, names)");
                }

                for (Disease d : diseases) {
                    Logger.info(d.toString());
                }

                break;

            case 6:

                Set<Indicator> indicators = dbContext.getIndicatorRepo().get(skip, limit);

                Logger.info("Found results: ", indicators.size());

                if (!indicators.isEmpty()) {
                    Logger.info("Results: Indicator(cui, names)");
                }

                for (Indicator i : indicators) {
                    Logger.info(i.toString());
                }

                break;
        }
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("f", CMD_FEED, true,
                "Feed [file] annotated data into neo4j. After seeding, program exits, without executing any queries.");
        options.addOption("q", CMD_QUERY, true, "Run query:\n"
                + "\n\t1: For a given disease (CUI), what other diseases have the highest number of common indicators?"
                + "\n\t2: For a given disease (CUI), what indicators connect it to other diseases"
                + "\n\t3: For an indicator (CUI), what other indicators commonly present in diseases where said indicator is present?"
                + "\n\t4: Which diseases are most similar to each other (share indicators) (No CUI)?"
                + "\n\t5: List diseases (No CUI)"
                + "\n\t6: List indicators (No CUI)");
        options.addOption("c", CMD_CUI, true, "CUI of disease or indicator (symptom or sign or finding)");
        options.addOption("s", CMD_SKIP, true, "Results offset. Default 0");
        options.addOption("l", CMD_LIMIT, true, "Maximum number of results. Default 10");

        CommandLineParser parser = new DefaultParser();

        String cui;
        int q = 0;
        int skip = 0;
        int limit = 0;

        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(CMD_FEED)) {

                Logger.info("Seeding Neo4j");

                ApplicationContext context = new ClassPathXmlApplicationContext("spring/context.xml");

                MetaMapNLP metaMapNLP = context.getBean(MetaMapNLP.class);

                String docPath = cmd.getOptionValue(CMD_FEED);

                if (!(new File(docPath)).exists()) {
                    usage(options);
                    Logger.error(String.format("No file found at path %s", docPath));
                }

                metaMapNLP.run(docPath);
            } else {
                if (cmd.hasOption(CMD_QUERY)) {
                    q = Integer.parseInt(cmd.getOptionValue(CMD_QUERY));
                    cui = cmd.getOptionValue(CMD_CUI, null);
                    skip = Integer.parseInt(cmd.getOptionValue(CMD_SKIP, "0"));
                    limit = Integer.parseInt(cmd.getOptionValue(CMD_LIMIT, "10"));

                    if (q < 1 || q > 6) {
                        usage(options);
                        Logger.error("[q] should be an integers between 1 and 3");
                        System.exit(1);
                    }

                    if (q >= 1 && q <= 3 && cui == null) {
                        usage(options);
                        Logger.error("For queries 1 to 3, [cui] is required");
                        System.exit(1);
                    }

                    if (cui == null) {
                        runQuery(q, skip, limit);
                    } else {
                        runQuery(q, cui, skip, limit);
                    }

                } else {
                    usage(options);
                    System.exit(1);
                }
            }

        } catch (ParseException e) {
            Logger.error(e.toString());
            System.exit(1);
        } catch (NumberFormatException e) {
            usage(options);
            Logger.error("[q, skip, limit] should be an integers.");
            Logger.error(String.format("%s, %s, %s", q, skip, limit));
            System.exit(1);
        }
    }
}

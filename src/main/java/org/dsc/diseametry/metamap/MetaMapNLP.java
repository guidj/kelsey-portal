package org.dsc.diseametry.metamap;

import gov.nih.nlm.nls.metamap.*;
import org.dsc.diseametry.data.DataService;
import org.dsc.diseametry.data.Document;
import org.dsc.diseametry.data.Document.FoundIndicator;
import org.dsc.diseametry.domain.IndicatorType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetaMapNLP {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(MetaMapNLP.class);

    private static final String SEMANTIC_TYPES[] = {"sosy", "diap", "dsyn", "fndg", "lbpr", "lbtr"};
    private static final String UMLS_DISEASE = "dsyn";
    private MetaMapApi mmapi;

    @Autowired
    private DataService ds;

    public MetaMapNLP() {
        this.init();
    }

    private void init() {
        ServerConfig serverConfig = new ServerConfig();
        this.mmapi = new MetaMapApiImpl(serverConfig.getProperty(CONFIG.HOST));
        this.mmapi.setOptions("-R SNOMEDCT_US");
    }

    public void run(String docPath) {

        if (this.mmapi == null) {
            this.init();
        }

        ArrayList<MetaSourceDoc> metaSourceDocs = readJSON(docPath);
        List<Document> tmpDocs;
        FoundIndicator tmpFoundIndicator;
        Document tmpDoc;
        long index = 1;
        Logger.info("Found {} source documents in {}", metaSourceDocs.size(), docPath);

        for (MetaSourceDoc metaSourceDoc : metaSourceDocs) {

            Logger.info("Reading source doc {}/{}", index, metaSourceDocs.size());
            index++;

            tmpDocs = new ArrayList<Document>();
            tmpDoc = null;

            Logger.info("Processing disease: " + metaSourceDoc.getName());
            Logger.info("URL: " + metaSourceDoc.getUrl() + "\n");

            try {
                Logger.debug("Symptoms:");
                Logger.debug("-------------");
                Logger.debug(metaSourceDoc.getSymptoms());
                Logger.debug("-------------");
                Logger.debug("Concepts retrieved for disease: " + metaSourceDoc.getName() + "\n");

                ArrayList<Ev> disease_concepts = getUMLSConceptsIn(metaSourceDoc.getName());
                for (Ev concept : disease_concepts) {
                    Logger.debug("\t\t" + concept.getConceptName() + " (CUI: " + concept.getConceptId() + ") - "
                            + concept.getSemanticTypes());
                    if (concept.getSemanticTypes().contains(UMLS_DISEASE)) {
                        tmpDoc = new Document();
                        tmpDoc.setDisease(concept.getConceptId());
                        tmpDoc.addDiseaseName(concept.getConceptName());
                        tmpDocs.add(tmpDoc);
                    }
                }

                Logger.debug("-------------");

                Logger.debug("Concepts retrieved for symptoms: \n");

                ArrayList<Ev> symptoms_concepts = getUMLSConceptsIn(metaSourceDoc.getSymptoms());

                for (Ev concept : symptoms_concepts) {
                    Logger.debug("\t\t" + concept.getConceptName() + " (CUI: " + concept.getConceptId() + ") - "
                            + concept.getSemanticTypes());

                    if (concept.getSemanticTypes().contains(IndicatorType.SIGN_OR_SYMPTOM.toString())) {
                        for (Document doc : tmpDocs) {
                            tmpFoundIndicator = doc.new FoundIndicator();
                            tmpFoundIndicator.setCui(concept.getConceptId());
                            tmpFoundIndicator.addName(concept.getConceptName());
                            tmpFoundIndicator.setIndicatorType(IndicatorType.SIGN_OR_SYMPTOM);
                            doc.addFoundIndicator(tmpFoundIndicator);
                        }
                    }

                    if (concept.getSemanticTypes().contains(IndicatorType.FINDING.toString())) {
                        for (Document doc : tmpDocs) {
                            tmpFoundIndicator = doc.new FoundIndicator();
                            tmpFoundIndicator.setCui(concept.getConceptId());
                            tmpFoundIndicator.addName(concept.getConceptName());
                            tmpFoundIndicator.setIndicatorType(IndicatorType.FINDING);
                            doc.addFoundIndicator(tmpFoundIndicator);
                        }
                    }
                }

                ds.indexDocuments(tmpDocs);

                Logger.debug("-------------");

            } catch (Exception e) {
                Logger.error("Error performing NLP: " + e.getMessage());
            }
        }
    }

    private ArrayList<MetaSourceDoc> readJSON(String file) {
        JSONParser parser = new JSONParser();
        ArrayList<MetaSourceDoc> diseases = new ArrayList<MetaSourceDoc>();

        try {

            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

            for (Object o : jsonArray) {
                JSONObject article = (JSONObject) o;

                if (!article.containsKey("Symptoms")) {
                    continue;
                }

                String url = (String) article.get("URL");
                String name = (String) article.get("Title");
                String symptoms = (String) article.get("Symptoms");

                diseases.add(new MetaSourceDoc(name, url, symptoms));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return diseases;
    }

    /**
     * Method to process the UMLS terms loaded in a disease.
     *
     * @param input Receives the disease.
     * @throws Exception It can throws an exception.
     */
    public ArrayList<Ev> getUMLSConceptsIn(String input) throws Exception {
        String preprocessedInput = preprocessString(input);
        ArrayList<Ev> concepts = new ArrayList<Ev>();
        List<Result> results = this.mmapi.processCitationsFromString(preprocessedInput);
        for (Result result : results) {
            for (Utterance utterance : result.getUtteranceList()) {
                for (PCM pcm : utterance.getPCMList()) {
                    for (Mapping map : pcm.getMappingList()) {
                        for (Ev mapEv : map.getEvList()) {
                            if (isAValidSemanticType(mapEv.getSemanticTypes())) {
                                // Logger.info("utterance:" +
                                // utterance.toString() +"\n"+ "PCM:" +
                                // pcm.getPhrase().toString() + "\n" + "map" +
                                // map.getEvList().toString() + "\n" + "mapEV:"
                                // + mapEv.getConceptName());
                                concepts.add(mapEv);
                            }
                        }
                    }
                }
            }
        }
        return concepts;
    }

    public String preprocessString(String input) {
        String filtered = input.replaceAll("[^\\x00-\\x7F]", "");
        return filtered.toLowerCase();
    }

    /**
     * Method to check if contains a valid semantic type.
     *
     * @param semanticTypes Receive the list of semantic types of the term.
     * @return Return true or false.
     */
    private boolean isAValidSemanticType(List<String> semanticTypes) {
        for (int i = 0; i < SEMANTIC_TYPES.length; i++) {
            String validSemanticType = SEMANTIC_TYPES[i];
            if (semanticTypes.contains(validSemanticType)) {
                return true;
            }
        }
        return false;
    }
}

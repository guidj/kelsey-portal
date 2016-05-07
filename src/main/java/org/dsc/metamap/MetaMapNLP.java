package org.dsc.metamap;

import gov.nih.nlm.nls.metamap.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MetaMapNLP {

    private final String SEMANTIC_TYPES[] = {"sosy", "diap", "dsyn", "fndg",
            "lbpr", "lbtr"};
    private MetaMapApi mmapi;

    public MetaMapNLP() {
        ServerConfig serverConfig = new ServerConfig();
        this.mmapi = new MetaMapApiImpl(serverConfig.getProperty(CONFIG.HOST));
        this.mmapi.setOptions("-R SNOMEDCT_US");
    }

    private void init() {
        ArrayList<Disease> diseases = readJSON("diseasesData/adam_articles.json");
        for (Disease disease : diseases) {
            System.out.println("Processing disease: " + disease.getName());
            System.out.println("URL: " + disease.getUrl() + "\n");

            try {
                System.out.println("Symptoms:");
                System.out.println("-------------");
                System.out.println(disease.getSymptoms());
                System.out.println("-------------");
                System.out.println("Concepts retrieved for disease: " + disease.getName() + "\n");

                ArrayList<Ev> disease_concepts = getUMLSConceptsIn(disease.getName());
                for (Ev concept : disease_concepts) {
                    System.out.println("\t\t" + concept.getConceptName()
                            + " (CUI: " + concept.getConceptId()
                            + ") - " + concept.getSemanticTypes());
                }

                System.out.println("-------------");

                System.out.println("Concepts retrieved for symptoms: \n");

                ArrayList<Ev> symptoms_concepts = getUMLSConceptsIn(disease.getSymptoms());

                for (Ev concept : symptoms_concepts) {
                    System.out.println("\t\t" + concept.getConceptName()
                            + " (CUI: " + concept.getConceptId()
                            + ") - " + concept.getSemanticTypes());
                }

                System.out.println("-------------");


            } catch (Exception e) {
                System.err.println("Error performing NLP: "
                        + e.getMessage());
            }

        }

    }

//    private Disease readFile(File file) {
//        try {
//            Properties prop = new Properties();
//            prop.load(new FileInputStream(file));
//            String name = prop.getProperty("NAME");
//            String url = prop.getProperty("URL_ENGLISH");
//            String nConcepts = prop.getProperty("NUMBER_TEXTS");
//            int nConc = Integer.parseInt(nConcepts);
//            Disease dis = new Disease(name, url);
//            for (int i = 1; i <= nConc; i++) {
//                String txt = prop.getProperty("TEXT_" + i);
//                if (txt != null) {
//                    txt = txt.replaceAll("[^\\x00-\\x7F]", "") + "\n";
//                    dis.addText(txt);
//                }
//            }
//            return dis;
//
//        } catch (Exception e) {
//            System.err.println("Error reading file: " + file.toString()
//                    + ". Error: " + e.getMessage());
//        }
//        return null;
//    }

    private ArrayList<Disease> readJSON(String file) {
        JSONParser parser = new JSONParser();
        ArrayList<Disease> diseases = new ArrayList<Disease>();

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

                diseases.add(new Disease(name, url, symptoms));

            }
            return diseases;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Method to process the UMLS terms loaded in a disease.
     *
     * @param input Receives the disease.
     * @throws Exception It can throws an exception.
     */
    public ArrayList<Ev> getUMLSConceptsIn(String input)
            throws Exception {
        String preprocessedInput = preprocessString(input);
        ArrayList<Ev> concepts = new ArrayList<Ev>();
        List<Result> results = this.mmapi
                .processCitationsFromString(preprocessedInput);
        for (Result result : results) {
            for (Utterance utterance : result.getUtteranceList()) {
                for (PCM pcm : utterance.getPCMList()) {
                    for (Mapping map : pcm.getMappingList()) {
                        for (Ev mapEv : map.getEvList()) {
                            if (isAValidSemanticType(mapEv.getSemanticTypes())) {
                                //System.out.println("utterance:" + utterance.toString() +"\n"+ "PCM:" + pcm.getPhrase().toString() + "\n" + "map" + map.getEvList().toString() + "\n" + "mapEV:" + mapEv.getConceptName());
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

    public static void main(String args[]) {
        new MetaMapNLP().init();
    }
}

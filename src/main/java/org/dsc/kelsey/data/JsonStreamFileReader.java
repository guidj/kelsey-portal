package org.dsc.kelsey.data;

import org.apache.commons.io.IOUtils;
import org.dsc.kelsey.domain.IndicatorType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class JsonStreamFileReader implements IStreamReader {

    private static final String CUI = "cui";
    private static final String NAMES = "names";
    private static final String INDICATORS = "indicators";
    private static final String DISEASE = "disease";
    private static final String TYPE = "type";

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(JsonStreamFileReader.class);

    private String filePath;
    private JSONArray objects;
    private int nextIndex;
    private boolean closed;

    public JsonStreamFileReader(String filePath) {
        this.filePath = filePath;
        this.initialize();
    }

    private void initialize() {

        Reader reader;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            Logger.error(e.toString());
            throw new RuntimeException(String.format("Error reading file %s", filePath));
        }

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {

            Logger.info("Parsing {}", filePath);

            this.objects = new JSONArray(new String(IOUtils.toByteArray(bufferedReader)));
            this.nextIndex = 0;
        } catch (IOException e) {
            Logger.error(e.toString());
            throw new RuntimeException(String.format("Error reading file %s", filePath));
        }

        this.nextIndex = 0;
        this.closed = false;

    }

    public Document next() {
        JSONObject object;
        JSONObject tmpObj;
        Document doc = new Document();
        Document.FoundIndicator indicator;
        Set<String> names = new HashSet<String>();

        if (this.isClosed()) {
            return null;
        }

        object = objects.getJSONObject(nextIndex);

        doc.setDisease(object.getJSONObject(DISEASE).getString(CUI));

        for (int i = 0; i < object.getJSONObject(DISEASE).getJSONArray(NAMES).length(); i++) {
            names.add(object.getJSONObject(DISEASE).getJSONArray(NAMES).getString(i));
        }

        for (int i = 0; i < object.getJSONArray(INDICATORS).length(); i++) {
            tmpObj = object.getJSONArray(INDICATORS).getJSONObject(i);

            indicator = Document.createIndicator();
            indicator.setCui(tmpObj.getString(CUI));
            indicator.setIndicatorType(IndicatorType.fromAbbreviation(tmpObj.getString(TYPE)));

            for (int j = 0; j < tmpObj.getJSONArray(NAMES).length(); j++) {
                indicator.addName(tmpObj.getJSONArray(NAMES).getString(j));
            }

            doc.addFoundIndicator(indicator);
        }

        nextIndex++;

        if (nextIndex >= this.objects.length()) {
            this.closed = true;
        }

        return doc;
    }

    public boolean isClosed() {
        return this.closed;
    }
}

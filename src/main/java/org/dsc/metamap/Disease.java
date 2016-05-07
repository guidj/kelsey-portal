package org.dsc.metamap;

public class Disease {

    private String name;
    private String symptoms;
    private String url;

    public Disease(String n, String u, String text) {
        this.name = n;
        this.url = u;
        this.symptoms = text;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSymptoms() {
        return symptoms;
    }


}

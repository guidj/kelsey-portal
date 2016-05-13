package org.dsc.diseametry.domain;

public enum IndicatorType {
    SIGN_OR_SYMPTOM("T033", "fndg"),
    FINDING("T184", "sosy");

    String tui;
    String abbreaviation;

    private IndicatorType(String tui, String abbreviation) {
        this.tui = tui;
        this.abbreaviation = abbreviation;
    }

    public String toString() {
        return this.abbreaviation;
    }

    public String getTui() {
        return tui;
    }

    public String getAbbreaviation() {
        return abbreaviation;
    }

    public static IndicatorType fromAbbreviation(String abbr) {

        for (IndicatorType type : values()) {
            if (type.getAbbreaviation().equals(abbr.toLowerCase())) {
                return type;
            }
        }

        return null;
    }
}

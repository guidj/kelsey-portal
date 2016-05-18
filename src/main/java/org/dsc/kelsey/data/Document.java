package org.dsc.kelsey.data;

import org.dsc.kelsey.domain.IndicatorType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Document {

    private String diseaseCui;
    private Set<String> diseaseNames;
    private Map<String, Document.FoundIndicator> foundIndicators;

    public Document() {
        this.diseaseNames = new HashSet<String>();
        this.foundIndicators = new HashMap<String, Document.FoundIndicator>();
    }

    public void setDisease(String diseaseCui) {
        this.diseaseCui = diseaseCui;
    }

    public void addDiseaseName(String name) {
        diseaseNames.add(name);
    }

    public void addFoundIndicator(FoundIndicator indicator) {
        if (indicator == null) {
            throw new RuntimeException("Tried to add a null Indicator");
        }

        this.foundIndicators.put(indicator.getCui(), indicator);
    }

    public void addFoundIndicator(String indicatorCui) {
        if (!this.foundIndicators.containsKey(indicatorCui)) {
            Document.FoundIndicator i = new Document.FoundIndicator();
            i.setCui(indicatorCui);
            this.foundIndicators.put(indicatorCui, i);
        }
    }

    public String getDiseaseCui() {
        return diseaseCui;
    }

    public Set<String> getDiseaseNames() {
        return diseaseNames;
    }

    public Map<String, Document.FoundIndicator> getFoundIndicators() {
        return foundIndicators;
    }

    public static FoundIndicator createIndicator() {
        return (new Document()).new FoundIndicator();
    }


    public class FoundIndicator {
        private String cui;
        private Set<String> names;
        private IndicatorType indicatorType;

        public FoundIndicator() {
        }

        public String getCui() {
            return cui;
        }

        public void setCui(String cui) {
            this.cui = cui;
        }

        public Set<String> getNames() {
            return names;
        }

        public void setNames(Set<String> names) {
            this.names = names;
        }

        public IndicatorType getIndicatorType() {
            return indicatorType;
        }

        public void setIndicatorType(IndicatorType indicatorType) {
            this.indicatorType = indicatorType;
        }

        public void addName(String name) {
            if (this.names == null) {
                this.names = new HashSet<String>();
            }
            this.names.add(name);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((cui == null) ? 0 : cui.hashCode());
            result = prime * result + ((indicatorType == null) ? 0 : indicatorType.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FoundIndicator other = (FoundIndicator) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (cui == null) {
                if (other.cui != null)
                    return false;
            } else if (!cui.equals(other.cui))
                return false;
            if (indicatorType != other.indicatorType)
                return false;
            return true;
        }

        private Document getOuterType() {
            return Document.this;
        }
    }
}

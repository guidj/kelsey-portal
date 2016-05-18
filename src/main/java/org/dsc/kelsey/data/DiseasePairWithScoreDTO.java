package org.dsc.kelsey.data;

import org.dsc.kelsey.domain.Disease;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

@QueryResult
public class DiseasePairWithScoreDTO {
    @ResultColumn("disease")
    private Disease disease;
    @ResultColumn("other")
    private Disease other;
    @ResultColumn("score")
    private Integer score;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Disease getOther() {
        return other;
    }

    public void setOther(Disease other) {
        this.other = other;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}

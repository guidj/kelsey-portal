package org.dsc.kelsey.data;

import org.dsc.kelsey.domain.Indicator;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;


@QueryResult
public class IndicatorWithScoreDTO {
    @ResultColumn("indicator")
    private Indicator indicator;
    @ResultColumn("score")
    private Integer score;

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}

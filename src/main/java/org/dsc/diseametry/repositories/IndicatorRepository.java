package org.dsc.diseametry.repositories;

import org.dsc.diseametry.data.IndicatorWithScoreDTO;
import org.dsc.diseametry.domain.Indicator;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface IndicatorRepository extends GraphRepository<Indicator> {

    @Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
    Set<Indicator> get(@Param("skip") int skip, @Param("limit") int limit);

    Indicator findByCui(String cui);

    @Query(
            "MATCH (i:Indicator)<-[r :HAS_FINDING|:HAS_SYM_OR_SIGN]-(:Disease)" +
                    "\nRETURN i AS indicator, COUNT(r) AS score" +
                    "\nORDER BY score DESC" +
                    "\nSKIP {skip}" +
                    "\nLIMIT {limit}")
    Collection<IndicatorWithScoreDTO> findMostRelevantIndicators(@Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(d:Disease)-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(o:Indicator)" +
                    "\n WHERE i.cui = {cui}" +
                    "\nRETURN o AS indicator, COUNT(d) AS score" +
                    "\nORDER BY score DESC" +
                    "\nSKIP {skip}" +
                    "\nLIMIT {limit}")
    Collection<IndicatorWithScoreDTO> findSimilarIndicators(@Param("cui") String cui, @Param("skip") int skip, @Param("limit") int limit);
}

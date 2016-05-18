package org.dsc.kelsey.repositories;

import org.dsc.kelsey.data.DiseasePairWithScoreDTO;
import org.dsc.kelsey.data.DiseaseWithScoreDTO;
import org.dsc.kelsey.data.IndicatorWithScoreDTO;
import org.dsc.kelsey.domain.Disease;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface DiseaseRepository extends GraphRepository<Disease> {

    @Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
    Set<Disease> get(@Param("skip") int skip, @Param("limit") int limit);

    Disease findByCui(String cui);

    @Query(
            "MATCH (d:Disease {cui:{cui}})-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(o:Disease)" +
                    "\nWHERE d <> o" +
                    "\nRETURN DISTINCT o AS disease, COUNT(i) AS score" +
                    "\nORDER BY score DESC" +
                    "\nSKIP {skip}" +
                    "\nLIMIT {limit}")
    Collection<DiseaseWithScoreDTO> findSimilarDiseases(@Param("cui") String cui, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (d:Disease)-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(o:Disease)" +
                    "\nWHERE d <> o AND ID(d) < ID(o)" +
                    "\nRETURN d AS disease, o AS other, COUNT(i) AS score" +
                    "\nORDER BY score DESC" +
                    "\nSKIP {skip}" +
                    "\nLIMIT {limit}")
    Collection<DiseasePairWithScoreDTO> findSimilarDiseases(@Param("skip") int skip, @Param("limit") int limit);


    @Query(
            "MATCH (d:Disease {cui:{cui}})-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(o:Disease)" +
                    "\nWHERE d <> o" +
                    "\nRETURN DISTINCT i AS indicator, COUNT(o) AS score" +
                    "\nORDER BY score DESC" +
                    "\nSKIP {skip}" +
                    "\nLIMIT {limit}")
    Collection<IndicatorWithScoreDTO> findConnectingIndicators(@Param("cui") String cui, @Param("skip") int skip, @Param("limit") int limit);
}

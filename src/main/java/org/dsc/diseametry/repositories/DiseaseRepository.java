package org.dsc.diseametry.repositories;

import java.util.Collection;
import java.util.Set;

import org.dsc.diseametry.data.DiseaseWithScoreDTO;
import org.dsc.diseametry.data.IndicatorWithScoreDTO;
import org.dsc.diseametry.domain.Disease;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface DiseaseRepository extends GraphRepository<Disease> {

	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Disease> get(@Param("skip") int skip, @Param("limit") int limit);

	public Disease findById(Long id);

	public Disease findByCui(String cui);
	
	
	@Query(
	"MATCH (n:Disease {cui:{cui}})-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(other:Disease)" + 
	"\nWHERE n <> other" +
	"\nRETURN DISTINCT other AS disease, COUNT(i) AS score")
	public Collection<DiseaseWithScoreDTO> findSimilarDiseases(@Param("cui") String cui);
	
	@Query(
	"MATCH (n:Disease {cui:{cui}})-[:HAS_FINDING|:HAS_SYM_OR_SIGN]->(i:Indicator)<-[:HAS_FINDING|:HAS_SYM_OR_SIGN]-(other:Disease)" + 
	"\nWHERE n <> other" +
	"\nRETURN DISTINCT i AS indicator, COUNT(other) AS score")
	public Collection<IndicatorWithScoreDTO> findConnectingIndicators(@Param("cui") String cui);	
}

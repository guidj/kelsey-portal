package org.dsc.diseametry.repositories;

import java.util.Collection;
import java.util.Set;

import org.dsc.diseametry.data.IndicatorWithScoreDTO;
import org.dsc.diseametry.domain.Indicator;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface IndicatiorRepository extends GraphRepository<Indicator> {

	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Indicator> get(@Param("skip") int skip, @Param("limit") int limit);

	public Indicator findById(Long id);

	public Indicator findByCui(String cui);

	@Query(
	"MATCH (i:Indicator)<-[r :HAS_FINDING|:HAS_SYM_OR_SIGN]-(:Disease)" + 
	"\nRETURN i AS indicator, COUNT(r) AS score" +
	"\nORDER BY score DESC" +
	"\nSKIP {skip}" +
	"\nLIMIT {limit}")
	public Collection<IndicatorWithScoreDTO> findMostRelevantIndicators(@Param("skip") int skip, @Param("limit") int limit);
}

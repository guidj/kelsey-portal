package org.dsc.diseametry.repositories;

import java.util.Set;

import org.dsc.diseametry.domain.Indicator;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface IndicatiorRepository extends GraphRepository<Indicator> {

	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Indicator> get(@Param("skip") int skip, @Param("limit") int limit);

//	@Query("MATCH (n {type: {type}}) RETURN n SKIP {skip} LIMIT {limit}")
//	Set<Indicator> get(@Param("skip") int skip, @Param("limit") int limit, @Param("type") IndicationType type);

	public Indicator findById(Long id);

	public Indicator findByCui(String cui);

//	public Indicator findByNameAndType(String name, IndicationType type);
}

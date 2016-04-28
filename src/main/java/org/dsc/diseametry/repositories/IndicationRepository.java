package org.dsc.diseametry.repositories;

import java.util.Set;

import org.dsc.diseametry.domain.Indication;
import org.dsc.diseametry.domain.IndicationType;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface IndicationRepository extends GraphRepository<Indication> {

	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Indication> get(@Param("skip") int skip, @Param("limit") int limit);

	@Query("MATCH (n {type: {type}}) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Indication> get(@Param("skip") int skip, @Param("limit") int limit, @Param("type") IndicationType type);

	public Indication findById(Long id);

	public Set<Indication> findByName(String name);

	public Indication findByNameAndType(String name, IndicationType type);
}

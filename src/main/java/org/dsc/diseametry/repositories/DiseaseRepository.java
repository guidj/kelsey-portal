package org.dsc.diseametry.repositories;

import java.util.Set;

import org.dsc.diseametry.domain.Disease;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface DiseaseRepository extends GraphRepository<Disease> {

	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	Set<Disease> get(@Param("skip") int skip, @Param("limit") int limit);

	public Disease findById(Long id);

	public Disease findByCui(String cui);
}

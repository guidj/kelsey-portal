package org.dsc.deseametry.repositories;

import java.util.Set;

import org.dsc.deseametry.domain.Concept;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface ConceptRepository extends GraphRepository<Concept>{
	
	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
	public Set<Concept> get(@Param("skip")int skip, @Param("limit")int limit);
	
	public Concept findById(Long id);
}

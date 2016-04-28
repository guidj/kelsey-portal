package org.dsc.deseametry.repositories;

import java.util.Set;

import org.dsc.deseametry.domain.Disease;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface DiseaseRepository extends GraphRepository<Disease> {
	
    // This is using the schema based index
	@Query("MATCH (n) RETURN n SKIP {skip} LIMIT {limit}")
    Set<Disease> get(@Param("skip")int skip, @Param("limit")int limit);

	public Disease findById(Long id);

	public Disease findByName(String url);
}

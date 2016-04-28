package org.dsc.deseametry.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Concept {
	
	@GraphId
	private Long id;
	
	@Indexed(unique = true, failOnDuplicate = true)
	protected String cui;
	
	public Concept() {
		
	}
	
	public Concept(String cui) {
		this.cui = cui;
	}
}

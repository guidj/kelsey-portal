package org.dsc.deseametry.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Disease {
	
	@GraphId
	private Long id;	

	@Indexed(unique = true, failOnDuplicate = true)
	private String name;
	
	protected String cui;	
	
	public Disease() {
		
	}
	
	public Disease(String cui, String name) {
		this.cui = cui;
		this.name = name;
	}
}

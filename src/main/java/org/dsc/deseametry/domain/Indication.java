package org.dsc.deseametry.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Indication {
	
	@GraphId
	private Long id;	
	
	@Indexed(indexType = IndexType.LABEL)
	private String name;
	
	protected String cui;
	
	@Indexed(indexType = IndexType.LABEL)
	private IndicationType type;
	
	public Indication() {
		
	}
	
	public Indication(String cui, String name, IndicationType type) {
//		super(cui);
		this.cui = cui;
		this.name = name;
		this.type = type;
	}
}

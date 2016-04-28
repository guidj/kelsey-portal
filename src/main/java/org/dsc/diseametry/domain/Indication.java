package org.dsc.diseametry.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Indication {
	
	@GraphId
	private Long id;	
	
	@Indexed(indexType = IndexType.LABEL)
	private String name;
	
	@Indexed(indexType = IndexType.LABEL)
	private String cui;
	
	@Indexed(indexType = IndexType.LABEL)
	private IndicationType type;
	
    @RelatedToVia
    private Set<HasIndication> linkedDiseases;		
	
	public Indication() {
		
	}
	
	public Indication(String cui, String name, IndicationType type) {
		this.cui = cui;
		this.name = name;
		this.type = type;
	}
	
	public Set<Disease> getDiseases(){
		Set<Disease> diseases = new HashSet<Disease>();
		
		if (this.linkedDiseases == null) {
			return diseases;
		}
		
		for(HasIndication hasIndication: this.linkedDiseases) {
			diseases.add(hasIndication.getDisease());
		}
		
		return diseases;
	}
}

package org.dsc.diseametry.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "HAS_FINDING")
public class HasIndication {
	@GraphId Long id;		
	@StartNode Disease disease;
    @EndNode Indication indication;
    
    public HasIndication(Disease disease, Indication indication) {
    	this.disease = disease;
    	this.indication = indication;
    }
    
    public HasIndication() {
    	
    }
    
    public Disease getDisease() {
		return disease;
	}

	public Indication getIndication() {
		return indication;
	}
}

package org.dsc.diseametry.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.cypherdsl.query.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Disease {
	
	@GraphId
	private Long id;	

	@Indexed(unique = true, failOnDuplicate = true)
	private String name;
	
	@Indexed(indexType = IndexType.LABEL)
	private String cui;	
	
    @RelatedToVia
    private Set<HasIndication> linkedIndications;	
	
	public Disease() {
		
	}
	
	public Disease(String cui, String name) {
		this.cui = cui;
		this.name = name;
	}
	
	public void addIndication(Indication i) {
		
		if (this.linkedIndications == null) {
			this.linkedIndications = new HashSet<HasIndication>();
		}
		
		this.linkedIndications.add(new HasIndication(this, i));
	}
	
	public Set<Indication> getIndications() {
		
		Set<Indication> indications = new HashSet<Indication>();
		
		if (this.linkedIndications == null) {
			return indications;
		}

		for(HasIndication hasIndication: this.linkedIndications) {
			indications.add(hasIndication.getIndication());
		}
		
		return indications;
	}
}

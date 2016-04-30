package org.dsc.diseametry.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Indicator {

	@GraphId
	private Long id;	
	
	@Indexed(unique = true, failOnDuplicate = true)
	private String cui;
	
	@Indexed(indexType = IndexType.LABEL)
	private Set<String> names;		
		
    public Indicator() {
		
	}
	
	public Indicator(String cui) {
		this.cui = cui;
	}
	
	public void setCui(String cui) {
		this.cui = cui;
	}
	
	public String getCui() {
		return cui;
	}	
	
	public void addName(String name) {
		if (this.names == null) {
			this.names = new HashSet<String>();
		}
		
		this.names.add(name);
	}
	
	public Set<String> getNames(){
		return this.names;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cui == null) ? 0 : cui.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Indicator other = (Indicator) obj;
		if (cui == null) {
			if (other.cui != null)
				return false;
		} else if (!cui.equals(other.cui))
			return false;
		return true;
	}
}

package org.dsc.diseametry.domain;

import java.util.HashSet;
import java.util.Set;

import org.dsc.diseametry.Utils;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Disease {

	public static final String HAS_SYMPTOM_OR_SIGN = "HAS_SYM_OR_SIGN";
	public static final String HAS_FINDING = "HAS_FINDING";

	@GraphId
	private Long id;

	@Indexed(unique = true, failOnDuplicate = true)
	private String cui;

	@Indexed(indexType = IndexType.LABEL)
	private Set<String> names;

	@RelatedTo(type = HAS_SYMPTOM_OR_SIGN, direction = Direction.OUTGOING)
	Set<Indicator> symptoms;

	@RelatedTo(type = HAS_FINDING, direction = Direction.OUTGOING)
	Set<Indicator> findings;

	public Disease() {

	}

	public Disease(String cui) {
		this.cui = cui;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public String getCui() {
		return this.cui;
	}

	public void addSymptomOrSign(Indicator indicator) {
		if (this.symptoms == null) {
			this.symptoms = new HashSet<Indicator>();
		}

		this.symptoms.add(indicator);
	}

	public void addFinding(Indicator finding) {
		if (this.findings == null) {
			this.findings = new HashSet<Indicator>();
		}

		this.findings.add(finding);
	}

	public void addName(String name) {
		if (this.names == null) {
			this.names = new HashSet<String>();
		}

		this.names.add(name);
	}

	public Set<String> getNames() {
		return this.names;
	}

	public Set<Indicator> getSymptoms() {
		return this.symptoms;
	}

	public Set<Indicator> getFindings() {
		return this.findings;
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
		Disease other = (Disease) obj;
		if (cui == null) {
			if (other.cui != null)
				return false;
		} else if (!cui.equals(other.cui))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s(cui=%s, names=[%s])", this.getClass().getName(), this.getCui(),
				Utils.collectionToString(this.getNames().toArray(new String[0])));
	}
}

package ru.skysoftlab.greenhouse.jpa.entitys.drools;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.skysoftlab.greenhouse.common.drools.ConditionOperator;
import ru.skysoftlab.greenhouse.common.drools.ConditionParam;
import ru.skysoftlab.greenhouse.common.drools.ConditionVals;
import ru.skysoftlab.skylibs.common.EditableEntity;

@Entity
public class Condition implements EditableEntity<Long> {

	private static final long serialVersionUID = -6856645960324940029L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Enumerated
	private ConditionParam field;
	@Enumerated
	private ConditionVals value;
	@Enumerated
	private ConditionOperator operator;

	public ConditionParam getField() {
		return field;
	}

	public void setField(ConditionParam field) {
		this.field = field;
	}

	public ConditionVals getValue() {
		return value;
	}

	public void setValue(ConditionVals value) {
		this.value = value;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	public void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

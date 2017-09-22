package ru.skysoftlab.greenhouse.jpa.entitys.drools;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import ru.skysoftlab.greenhouse.common.GableState;
import ru.skysoftlab.skylibs.common.EditableEntity;

@Entity
@NamedQuery(name = "Rule.getAll", query = "SELECT e FROM Rule e")
public class Rule implements EditableEntity<Long> {

	private static final long serialVersionUID = -8034361981658068357L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RULE_ID")
	private Long id;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "OWNER_ID", referencedColumnName = "RULE_ID")
	private List<Condition> conditions;
	@Enumerated
	private GableState state;

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		StringBuilder statementBuilder = new StringBuilder();

		for (Condition condition : getConditions()) {

			String operator = null;

			switch (condition.getOperator()) {
			case EQUAL_TO:
				operator = "==";
				break;
			case NOT_EQUAL_TO:
				operator = "!=";
				break;
			case GREATER_THAN:
				operator = ">";
				break;
			case LESS_THAN:
				operator = "<";
				break;
			case GREATER_THAN_OR_EQUAL_TO:
				operator = ">=";
				break;
			case LESS_THAN_OR_EQUAL_TO:
				operator = "<=";
				break;
			}

			statementBuilder.append(condition.getField()).append(" ").append(operator).append(" ");

			statementBuilder.append(condition.getValue());

			statementBuilder.append(" && ");
		}

		String statement = statementBuilder.toString();

		// remove trailing &&
		return statement.substring(0, statement.length() - 4);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GableState getState() {
		return state;
	}

	public void setState(GableState state) {
		this.state = state;
	}

}

package ru.skysoftlab.greenhouse.common.drools;

import java.util.HashMap;
import java.util.Map;

public enum ConditionOperator {
	NOT_EQUAL_TO("NOT_EQUAL_TO"), EQUAL_TO("EQUAL_TO"), GREATER_THAN("GREATER_THAN"), LESS_THAN(
			"LESS_THAN"), GREATER_THAN_OR_EQUAL_TO(
					"GREATER_THAN_OR_EQUAL_TO"), LESS_THAN_OR_EQUAL_TO("LESS_THAN_OR_EQUAL_TO");
	private final String value;
	private static Map<String, ConditionOperator> constants = new HashMap<>();

	static {
		for (ConditionOperator c : values()) {
			constants.put(c.value, c);
		}
	}

	private ConditionOperator(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static ConditionOperator fromValue(String value) {
		ConditionOperator constant = constants.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
}

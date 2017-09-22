package ru.skysoftlab.greenhouse.drools;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;

import org.drools.template.DataProvider;

import ru.skysoftlab.greenhouse.jpa.entitys.drools.Rule;

public class RulsDataProvider implements DataProvider, Serializable {

	private static final long serialVersionUID = 9210719750284276261L;

	@Inject
	private Iterator<Rule> iterator;

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public String[] next() {
		Rule nextRule = iterator.next();
		String[] row = new String[] {
				nextRule.toString().replaceAll("TEMPERATURE", "temperature").replaceAll("HUMIDITY", "humidity"),
				nextRule.getState().name() };
		return row;
	}

}

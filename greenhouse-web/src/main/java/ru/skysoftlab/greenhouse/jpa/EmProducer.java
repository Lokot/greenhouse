package ru.skysoftlab.greenhouse.jpa;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EmProducer implements Serializable {

	private static final long serialVersionUID = 5127966525383571663L;

	@PersistenceContext(unitName = "greenhouse-pu")
	protected EntityManager em;

	@Produces
	public EntityManager getEntityManager() {
		return em;
	}

}

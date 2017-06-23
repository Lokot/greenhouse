package ru.skysoftlab.greenhouse.jpa;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ru.skysoftlab.greenhouse.jpa.entitys.IrrigationCountur;

import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class IrrigationCounturEntityProviderBean extends MutableLocalEntityProvider<IrrigationCountur> {

	@Inject
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	public IrrigationCounturEntityProviderBean() {
		super(IrrigationCountur.class);
		setTransactionsHandledByProvider(true);
	}

	@Override
	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected void runInTransaction(Runnable operation) {
		assert operation != null : "operation must not be null";
		if (isTransactionsHandledByProvider()) {
			try {
				if (utx.getStatus() == 0) {
					// The transaction has been started outside of this method
					// and should also be committed/rolled back outside of
					// this method
					operation.run();
				} else {
					try {
						utx.begin();
						operation.run();
						utx.commit();
					} finally {
						if (utx.getStatus() == 0) {
							utx.rollback();
						}
					}
				}
			} catch (SecurityException | IllegalStateException | SystemException | NotSupportedException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
		} else {
			operation.run();
		}
	}

	@PostConstruct
	public void init() {
		setEntityManager(em);
		/*
		 * The entity manager is transaction-scoped, which means that the
		 * entities will be automatically detached when the transaction is
		 * closed. Therefore, we do not need to explicitly detach them.
		 */
		setEntitiesDetached(false);
	}
}
package com.midea.trade.sharding.core.tx;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.tx.DelegateSavepoint.SavepointDesc;


/**
 * Transaction
 */
public class DelegateTransaction implements Transaction {
	
	static Logger logger = LoggerFactory.getLogger(DelegateTransaction.class);
	
	Set<Transaction> transactions = new LinkedHashSet<Transaction>();
	
    boolean commited;
	public void addTransaction(Transaction tx){
		transactions.add(tx);
	}
	@Override
	public void commit() throws SQLException {
		for (Transaction tx : transactions) {
			tx.commit();
		}
		commited=true;
	}

	@Override
	public void rollback() throws SQLException {
		SQLException e = null;
		for (Transaction tx : transactions) {
			try {
				tx.rollback();
			} catch (SQLException ex) {
				e = ex;
				logger.error("事务回滚异常", ex.getMessage());
			}
		}
		if (e != null) {
			throw e;
		}

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		if (savepoint instanceof DelegateSavepoint) {
			DelegateSavepoint delegate = (DelegateSavepoint) savepoint;
			Set<SavepointDesc> savePoints = delegate.savePoints;
			if (savePoints != null) {
				SQLException e = null;
				for (SavepointDesc savepointDesc : savePoints) {
					try {
						savepointDesc.rollback();
					} catch (SQLException ex) {
						e = ex;
						logger.error("事务回滚异常", ex.getMessage());
					}
				}
				if (e != null) {
					throw e;
				}
			}

		} else {
			SQLException e = null;
			for (Transaction tx : transactions) {
				try {
					tx.rollback(savepoint);
				} catch (SQLException ex) {
					e = ex;
					logger.error("rollback事务回滚异常", ex.getMessage());
				}
			}
			if (e != null) {
				throw e;
			}
		}

	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		if (savepoint instanceof DelegateSavepoint) {
			DelegateSavepoint delegate = (DelegateSavepoint) savepoint;
			Set<SavepointDesc> savePoints = delegate.savePoints;
			if (savePoints != null) {
				SQLException e = null;
				for (SavepointDesc savepointDesc : savePoints) {
					try {
						savepointDesc.releaseSavepoint();
					} catch (SQLException ex) {
						e = ex;
						logger.error("releaseSavepoint异常", ex.getMessage());
					}
				}
				if (e != null) {
					throw e;
				}
			}

		} else {
			SQLException e = null;
			for (Transaction tx : transactions) {
				try {
					tx.releaseSavepoint(savepoint);
				} catch (SQLException ex) {
					e = ex;
					logger.error("releaseSavepoint异常", ex.getMessage());
				}
			}
			if (e != null) {
				throw e;
			}
		}

	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		DelegateSavepoint delegate = new DelegateSavepoint(name);
		for (Transaction tx : transactions) {
			SavepointWrapper savepoint = (SavepointWrapper) tx
					.setSavepoint(name);
			delegate.addSavepoint(savepoint);
		}
		return delegate;
	}
	@Override
	public boolean isCommited() { 
		return commited;
	}

}

package com.midea.trade.sharding.core.context;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.jdbc.ConnectionCallback;
import com.midea.trade.sharding.core.jdbc.ConnectionManager;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;
import com.midea.trade.sharding.core.tx.DelegateTransaction;
import com.midea.trade.sharding.core.tx.Transaction;


/**
 * 线程执行生命周期的上下文
 */
public class ConnectionContext {
	
	private static Logger logger = LoggerFactory.getLogger(ConnectionContext.class);

	DelegateTransaction transactions = null;
	/**
	 * callback根据statment的生命周期进行处理
	 */
	final List<ConnectionCallback> connectionCallbacks = new LinkedList<ConnectionCallback>();
	final Set<Connection> connections = new LinkedHashSet<Connection>(); //存储当前连接
	final Set<Connection> commitedConnections = new LinkedHashSet<Connection>();
	static final ThreadLocal<ConnectionContext> threadLocal = new ThreadLocal<ConnectionContext>();
	Set<Clob> clobs;
	Set<Blob> blobs;
	final Connection orgConnection;
	final Connection wrapper;
	ConnectionManager orgConnectionManager;
	ResultSet generateKeys;
	/**
	 * 当前使用的连接
	 */
	Connection currentConnection;
	
	
	public ConnectionContext(Connection orgConnection, Connection wrapper) {
		this.orgConnection = orgConnection;
		this.wrapper = wrapper;
	}

	public DelegateTransaction getTransaction() {
		return transactions;
	}

	public void addTransaction(Transaction tx) {
		transactions.addTransaction(tx);
	}

	
	public static void setContext(ConnectionContext context) {
		threadLocal.set(context);
		if(context == null) {
			TrackerExecutor.trackEnd(TrackPoint.CONNECTION_CONTEXT);
			TrackerExecutor.trackRelease();
		} else {
			TrackerExecutor.trackBegin(TrackPoint.CONNECTION_CONTEXT);
		}
	}

	public static ConnectionContext getContext() {
		return threadLocal.get();
	}

	public List<ConnectionCallback> getConnectionCallbacks() {
		return connectionCallbacks;
	}

	public Set<Connection> getConnections() {
		return connections;
	}

	public void addConnection(Connection connection) {
		connections.add(connection);
	}

	public void addCommitedConnection(Connection connection) {
		commitedConnections.add(connection);
	}

	public void addCallback(ConnectionCallback callback) {
		connectionCallbacks.add(callback);
	}

	public void beginTransaction() {
		if (transactions != null) {
		} else {
			transactions = new DelegateTransaction();
		}
	}

	
	public Connection getOrgConnection() {
		return orgConnection;
	}

	/**
	 * 因为后续的connection中可能还有事务操作 begin commit do something commit 。。。 end
	 * 所以能够接受这样的场景我们需要在这个时候释放一些连接
	 * @throws SQLException
	 */
	public void rollbackOrCommited() throws SQLException {
		transactions = new DelegateTransaction();
		Iterator<Connection> iterator = connections.iterator();
		while (iterator.hasNext()) {
			Connection connection = iterator.next();
			for (ConnectionCallback callback : connectionCallbacks) {// 恢复到执行前的状态
				callback.reset(connection);
			}
			if (connection == this.orgConnection) {
				orgConnectionManager.release(connection);
			} else if (!connection.isClosed()) {
				connection.close();
			}
		}
		connections.clear();
	}

	public void releaseAfterClosed() throws SQLException {
		setContext(null);
		transactions = null;
		for (Connection connection : connections) {
			for (ConnectionCallback callback : connectionCallbacks) {// 恢复到执行前的状态
				callback.reset(connection);
			}
			if (connection == this.orgConnection) {
				orgConnectionManager.release(connection);
			} else if (!connection.isClosed()) {
				connection.close();
			}
		}
		connectionCallbacks.clear();
		connections.clear();
	}

	public void setOrgConnectionManager(ConnectionManager orgConnectionManager) {
		this.orgConnectionManager = orgConnectionManager;
	}
	
	public boolean isTransaction(){
		return transactions != null;
	}

	public Connection getCurrentConnection() {
		return currentConnection;
	}

	public void setCurrentConnection(Connection currentConnection) {
		this.currentConnection = currentConnection;
	}
}

package com.midea.trade.sharding.core.context;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.core.util.JdbcUtil;
import com.midea.trade.sharding.core.util.Transporter;

/**
 * 一个事务处理的上下文
 */
public class TransactionContext {

    static final ThreadLocal<TransactionContext> threadLocal = new ThreadLocal<TransactionContext>();

    /**
     * 在一次事务中，一个数据源对应一个connection
     */
    final Map<String, Connection>                connsInTransaction;

    /**
     * 在一次事务中,一个connection中的相同table对应同一个statement
     */
    final Map<String, Statement>                 stmtsInTransaction;

    /**
     * 在一次事务中的sql
     */
    final List<String>                           sqlsInTransaction;

    public TransactionContext(){
        connsInTransaction = Maps.newLinkedHashMap();
        stmtsInTransaction = Maps.newLinkedHashMap();
        sqlsInTransaction = Lists.newLinkedList();
    }

    public static TransactionContext getContext() {
        if (threadLocal.get() == null) threadLocal.set(new TransactionContext());

        return threadLocal.get();
    }

    public void setContext(TransactionContext context) {
        threadLocal.set(context);
    }

    public Connection getTransactionConnection(DataNode dataNode, Transporter<Boolean> isNewConn) throws SQLException {

        Connection connection = connsInTransaction.get(dataNode.getId());
        if (connection == null) {
            connection = dataNode.getConnection();
            connsInTransaction.put(dataNode.getId(), connection);
            isNewConn.setValue(true);
        }

        return connection;
    }

    public Statement getTransactionStatement(Connection connection, RouteTarget target) {
        return stmtsInTransaction.get(connection.toString() + target.getNameNode().toString() + target.getBatchItem().sql);
    }

    public void setTransactionStatement(Connection connection, RouteTarget target, Statement statement) {
        stmtsInTransaction.put(connection.toString() + target.getNameNode().toString() + target.getBatchItem().sql,
            statement);
        sqlsInTransaction.add(target.getExecuteInfo().getExecuteSql());
    }

    
    
    public void release() {
        for (Statement statement : stmtsInTransaction.values()) {
            JdbcUtil.closeStatement(statement);
        }
        setContext(null);
        sqlsInTransaction.clear();
    }

}

package com.midea.trade.sharding.druid.route;


public class RouteResultsetNode {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final String name; // 数据节点名称
	private String statement; // 执行的语句
	private final String srcStatement;
	private final int sqlType;
	private volatile boolean canRunInReadDB;
	private final boolean hasBlanceFlag;

	private int limitStart;
	private int limitSize;
	private int totalNodeSize =0; //方便后续jdbc批量获取扩展


	public RouteResultsetNode(String name, int sqlType, String srcStatement) {
		this.name = name;
		limitStart=0;
		this.limitSize = -1;
		this.sqlType = sqlType;
		this.srcStatement = srcStatement;
		this.statement = srcStatement;
		hasBlanceFlag = (statement != null)
				&& statement.startsWith("/*balance*/");
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public void setCanRunInReadDB(boolean canRunInReadDB) {
		this.canRunInReadDB = canRunInReadDB;
	}

	public boolean getCanRunInReadDB() {
		return this.canRunInReadDB;
	}

	public void resetStatement() {
		this.statement = srcStatement;
	}

	public boolean canRunnINReadDB(boolean autocommit) {
		return canRunInReadDB && autocommit && !hasBlanceFlag
			|| canRunInReadDB && !autocommit && hasBlanceFlag;
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}

	public String getStatement() {
		return statement;
	}

	public int getLimitStart()
	{
		return limitStart;
	}

	public void setLimitStart(int limitStart)
	{
		this.limitStart = limitStart;
	}

	public int getLimitSize()
	{
		return limitSize;
	}

	public void setLimitSize(int limitSize)
	{
		this.limitSize = limitSize;
	}

	public int getTotalNodeSize()
	{
		return totalNodeSize;
	}

	public void setTotalNodeSize(int totalNodeSize)
	{
		this.totalNodeSize = totalNodeSize;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof RouteResultsetNode) {
			RouteResultsetNode rrn = (RouteResultsetNode) obj;
			if (equals(name, rrn.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(name);
		s.append('{').append(statement).append('}');
		return s.toString();
	}

	private static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}
		return str1.equals(str2);
	}

	public boolean isModifySQL() {
		return !canRunInReadDB;
	}

	public int compareTo(RouteResultsetNode obj) {
		if(obj == null) {
			return 1;
		}
		if(this.name == null) {
			return -1;
		}
		if(obj.name == null) {
			return 1;
		}
		return this.name.compareTo(obj.name);
	}

}

package com.midea.trade.sharding.core.exception.sqlexception;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql异常类型
 */
public enum MySqlExceptionType {
	
	ER_CON_COUNT_ERROR(1040), // 连接过多
	ER_BAD_HOST_ERROR(1042), // 无法获得该地址给出的主机名
	ER_HANDSHAKE_ERROR(1043), // 不良握手
	ER_DBACCESS_DENIED_ERROR(1044), // 拒绝用户访问数据库
	ER_ACCESS_DENIED_ERROR(1045), // 拒绝用户访问（使用密码）
	ER_UNKNOWN_COM_ERROR(1047), // 未知命令
	ER_IPSOCK_ERROR(1081), // 无法创建IP套接字
	ER_HOST_IS_BLOCKED(1129), // 由于存在很多连接错误，主机'%s'被屏蔽，请用'mysqladmin flush-hosts'解除屏蔽
	ER_HOST_NOT_PRIVILEGED(1130), // 不允许将主机'%s'连接到该MySQL服务器

    // Resource errors
	ER_CANT_CREATE_FILE(1004), // 无法创建文件'%s' 
	ER_CANT_CREATE_TABLE(1005), // 无法创建表'%s' 
	ER_CANT_LOCK(1015), // 无法锁定文件
	ER_DISK_FULL(1021), // 磁盘满(%s)；等待某人释放一些空间
	ER_OUT_OF_RESOURCES(1041), // 内存溢出，请检查是否mysqld或其他进程使用了所有可用内存，如不然，或许应使用'ulimit'允许mysqld使用更多内存，或增加交换空间的大小。

    // Out-of-memory errors(),
	ER_OUTOFMEMORY(1037), // 内存溢出，重启服务器并再次尝试（需要%d字节）
	ER_OUT_OF_SORTMEMORY(1038); // 分类内存溢出，增加服务器的分类缓冲区大小
	
	private static final Map<Integer, MySqlExceptionType> MAPPING = 
			new HashMap<Integer, MySqlExceptionType>();
	
	static {
		for(MySqlExceptionType type : values()){
			MAPPING.put(type.getErrorCode(), type);
		}
	}
	
	static MySqlExceptionType getMySqlExceptionType(int errorCode){
		return MAPPING.get(errorCode);
	}
	
	private int errorCode;
	
	private MySqlExceptionType(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}

}


package com.midea.trade.sharding.config;

/**
 * 连接池配置实体
 */
public class ThreadPoolConfig implements Config {

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_CORE_SIZE = 100;
	private static final Integer DEFAULT_QUEUE_SIZE = 2048;
	private static final Long DEFAULT_TIMEOUT = 10000L;
	
	private String id;
	private Integer coreSize = DEFAULT_CORE_SIZE;
	private Integer queueSize = DEFAULT_QUEUE_SIZE;
	private Long timeout = DEFAULT_TIMEOUT;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCoreSize() {
		return coreSize;
	}

	public void setCoreSize(Integer coreSize) {
		this.coreSize = coreSize;
	}

	public Integer getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(Integer queueSize) {
		this.queueSize = queueSize;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "ThreadPoolConfig [id=" + id + ", coreSize=" + coreSize
				+ ", queueSize=" + queueSize + ", timeout=" + timeout + "]";
	}

}

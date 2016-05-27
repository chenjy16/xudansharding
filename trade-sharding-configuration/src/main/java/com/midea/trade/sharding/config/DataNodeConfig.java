package com.midea.trade.sharding.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DataNode配置实体
 * 
 */
public class DataNodeConfig extends BaseDataNodeConfig implements Config {
	
	private static final long serialVersionUID = 1L;
	
	String id;
	String url;
	String username;
	String password;
	String parent;
	String ref;
	String accessMode;
	Long weight;
	String slaves[];
	Map<String, String> properties = new HashMap<String, String>();
	String alarmClass;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getSlaves() {
		return slaves;
	}

	public void setSlaves(String[] slaves) {
		this.slaves = slaves;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public String getAlarmClass() {
		return alarmClass;
	}

	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}

	public DataNodeConfig clone() {
		DataNodeConfig obj = null;
		try {
			obj = (DataNodeConfig) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		obj.setId(id);
		obj.setUrl(url);
		obj.setUsername(username);
		obj.setUrl(url);
		obj.setParent(parent);
		obj.setPassword(password);
		obj.setRef(ref);
		obj.setAccessMode(accessMode);
		obj.setWeight(weight);
		obj.setSlaves(slaves);
		obj.setProperties(properties);
		obj.setAlarmClass(alarmClass);
		return obj;
	}

	@Override
	public String toString() {
		return "DataNodeConfig [id=" + id + ", url=" + url + ", username="
				+ username + ", password=" + password + ", parent=" + parent
				+ ", ref=" + ref + ", accessMode=" + accessMode + ", weight="
				+ weight + ", slaves=" + Arrays.toString(slaves)
				+ ", properties=" + properties + ", alarmClass=" 
				+ alarmClass + "]";
	}

}

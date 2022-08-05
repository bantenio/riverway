package com.yomahub.liteflow.builder.prop;

/**
 * 构建 node 的中间属性
 */
public class NodePropBean {

	/**
	 * id
	 */
	String id;

	/**
	 * 名称
	 */
	String name;

	/**
	 * 类
	 */
	String clazz;

	/**
	 * 类型
	 */
	String type;

	public String getId() {
		return id;
	}

	public NodePropBean setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public NodePropBean setName(String name) {
		this.name = name;
		return this;
	}

	public String getClazz() {
		return clazz;
	}

	public NodePropBean setClazz(String clazz) {
		this.clazz = clazz;
		return this;
	}

	public String getType() {
		return type;
	}

	public NodePropBean setType(String type) {
		this.type = type;
		return this;
	}
}

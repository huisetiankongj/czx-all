package com.czx.big.common.persistence;

import java.io.Serializable;

import com.czx.big.common.config.Global;

public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体编号（唯一标识）
	 */
	protected String id;
	
	/**
	 * 删除标记（0：正常；1：删除；2：审核；）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	public static final String BIG_CP_TP = Global.getConfig("big.currentProject.tablePrefix");
	public static final String BIG_COMMON_TP = Global.getConfig("big.common.tablePrefix");
}

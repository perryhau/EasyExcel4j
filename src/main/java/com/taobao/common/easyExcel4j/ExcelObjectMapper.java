package com.taobao.common.easyExcel4j;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class ExcelObjectMapper implements Serializable {

	private static final long serialVersionUID = -58483139027577824L;

	private ExcelConfig config;

	/**
	 * 列与属性对应关系
	 */
	private List<ExcelObjectMapperDO> list = Lists.newArrayList();

	public List<ExcelObjectMapperDO> getList() {
		return list;
	}

	public void setList(List<ExcelObjectMapperDO> list) {
		this.list = list;
	}

	public ExcelConfig getConfig() {
		return config;
	}

	public void setConfig(ExcelConfig config) {
		this.config = config;
	}

}

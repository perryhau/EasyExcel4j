package com.taobao.common.easyExcel4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelObjectMapper implements Serializable {

	private static final long serialVersionUID = -58483139027577824L;

	private Class<?> clazz;

	private List<ExcelObjectMapperDO> list = new ArrayList<ExcelObjectMapperDO>();

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public List<ExcelObjectMapperDO> getList() {
		return list;
	}

	public void setList(List<ExcelObjectMapperDO> list) {
		this.list = list;
	}

}

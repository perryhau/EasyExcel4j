package com.taobao.common.easyExcel4j;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ExcelObjectMapperDO implements Serializable {

	private static final long serialVersionUID = 7745296502632710593L;

	private String objectFieldName;
	private Method method;
	private String excelColumnName;
	private Integer excelColumnNum;
	private boolean required;

	public String getObjectFieldName() {
		return objectFieldName;
	}

	public void setObjectFieldName(String objectFieldName) {
		this.objectFieldName = objectFieldName;
	}

	public String getExcelColumnName() {
		return excelColumnName;
	}

	public void setExcelColumnName(String excelColumnName) {
		this.excelColumnName = excelColumnName;
	}

	public Integer getExcelColumnNum() {
		return excelColumnNum;
	}

	public void setExcelColumnNum(Integer excelColumnNum) {
		this.excelColumnNum = excelColumnNum;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return this.excelColumnName.toString();
	}
}

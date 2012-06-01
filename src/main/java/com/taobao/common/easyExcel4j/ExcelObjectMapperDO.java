package com.taobao.common.easyExcel4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ExcelObjectMapperDO implements Serializable {

	private static final long serialVersionUID = 7745296502632710593L;

	private String objectFieldName;
	private Class<?> objectFieldType;
	private Method method;
	private String excelColumnName;
	private Integer excelColumnNum;
	private boolean required;

	private Map<String, Boolean> booleanMap = new HashMap<String, Boolean>();

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

	public Class<?> getObjectFieldType() {
		return objectFieldType;
	}

	public void setObjectFieldType(Class<?> objectFieldType) {
		this.objectFieldType = objectFieldType;
	}

	@Override
	public String toString() {
		return this.excelColumnName.toString();
	}

	public Map<String, Boolean> getBooleanMap() {
		return booleanMap;
	}

	public void setBooleanMap(Map<String, Boolean> booleanMap) {
		this.booleanMap = booleanMap;
	}

}

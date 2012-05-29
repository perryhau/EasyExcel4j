package com.taobao.common.easyExcel4j;

import java.io.Serializable;

public class ExcelObjectMapperDO implements Serializable {

	private static final long serialVersionUID = 7745296502632710593L;

	private String objectFieldName;
	private String excelColumnName;
	private int excelColumnNum;
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

	public int getExcelColumnNum() {
		return excelColumnNum;
	}

	public void setExcelColumnNum(int excelColumnNum) {
		this.excelColumnNum = excelColumnNum;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}

package com.taobao.common.easyExcel4j;

public class DefaultMapperStrategy extends AbstractMapperStrategy {

	public <T> DefaultMapperStrategy(Class<T> clazz) {
		super(clazz);
	}

	public <T> T getInstance() {
		EasyExcelCglibProxy proxy = new EasyExcelCglibProxy(clazz);
		return proxy.getProxyInstance();
	}

	public String anyString(String columnName) {
		return anyString(columnName, true);
	}

	public String anyString(String columnName, boolean required) {
		ExcelObjectMapperDO eom = new ExcelObjectMapperDO();
		eom.setExcelColumnName(columnName);
		eom.setRequired(required);
		add(clazz, eom);
		return null;
	}

}

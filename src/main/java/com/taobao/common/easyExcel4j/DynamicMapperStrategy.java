package com.taobao.common.easyExcel4j;

import java.util.HashMap;
import java.util.Map;

public class DynamicMapperStrategy extends AbstractMapperStrategy {

	public <T> DynamicMapperStrategy(Class<T> clazz) {
		super(clazz);
	}

	public <T> T getInstance() {
		EasyExcelCglibProxy proxy = new EasyExcelCglibProxy(clazz);
		return proxy.getProxyInstance();
	}

	private void add(String columnName, boolean required, Class<?> type, Map<String, Boolean> map) {
		ExcelObjectMapperDO eom = new ExcelObjectMapperDO();
		eom.setExcelColumnName(columnName);
		eom.setRequired(required);
		eom.setObjectFieldType(type);
		eom.setBooleanMap(map);
		add(clazz, eom);
	}

	// String
	public String anyString(String columnName) {
		return anyString(columnName, true);
	}

	public String anyString(String columnName, boolean required) {
		add(columnName, required, String.class, null);
		return null;
	}

	// Integer
	public int anyInteger(String columnName) {
		return anyInteger(columnName, true);
	}

	public int anyInteger(String columnName, boolean required) {
		add(columnName, required, Integer.class, null);
		return 0;
	}

	// Long
	public long anyLong(String columnName) {
		return anyLong(columnName, true);
	}

	public long anyLong(String columnName, boolean required) {
		add(columnName, required, Long.class, null);
		return 0L;
	}

	// Double
	public double anyDouble(String columnName) {
		return anyDouble(columnName, true);
	}

	public double anyDouble(String columnName, boolean required) {
		add(columnName, required, Double.class, null);
		return 0d;
	}

	// Float
	public float anyFloat(String columnName) {
		return anyFloat(columnName, true);
	}

	public float anyFloat(String columnName, boolean required) {
		add(columnName, required, Float.class, null);
		return 0f;
	}

	// Byte
	public byte anyByte(String columnName) {
		return anyByte(columnName, true);
	}

	public byte anyByte(String columnName, boolean required) {
		add(columnName, required, Byte.class, null);
		return '0';
	}

	// Short
	public short anyShort(String columnName) {
		return anyShort(columnName, true);
	}

	public short anyShort(String columnName, boolean required) {
		add(columnName, required, Short.class, null);
		return '0';
	}

	// Char
	public char anyCharacter(String columnName) {
		return anyCharacter(columnName, true);
	}

	public char anyCharacter(String columnName, boolean required) {
		add(columnName, required, Character.class, null);
		return '0';
	}

	// Boolean
	public boolean anyBoolean(String columnName, String isTrueString, String isFalseString) {
		return anyBoolean(columnName, true, isTrueString, isFalseString);
	}

	public boolean anyBoolean(String columnName, boolean required, String isTrueString, String isFalseString) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(isTrueString, Boolean.TRUE);
		map.put(isFalseString, Boolean.FALSE);
		add(columnName, required, Boolean.class, map);
		return true;
	}

	@Override
	protected void init() {

	}

}

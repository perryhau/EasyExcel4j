package com.taobao.common.easyExcel4j;

public class EasyExcel {

	public static <T> T target(Class<T> clazz) {
		EasyExcelCglibProxy<T> proxy = new EasyExcelCglibProxy<T>(clazz);
		return (T) proxy.getProxyInstance();
	}

	public static <T> EasyMethod<T> initProp() {
		return new EasyMethodImpl<T>(true);
	}

	public static void execute() {

	}

	public static String extractString(String columnName) {
		return extractString(columnName, true);
	}

	public static String extractString(String columnName, boolean required) {
		
		return null;
	}
}

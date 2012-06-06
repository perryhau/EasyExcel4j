package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

public class MapperStrategyFactory {

	private static class SingletonHolder {
		private static MapperStrategyFactory instance = new MapperStrategyFactory();
	}

	private MapperStrategyFactory() {

	}

	public static MapperStrategyFactory getInstance() {
		return SingletonHolder.instance;
	}

	public <T> CamelCaseMapperStrategy getCamelCaseMapperStrategy(Class<T> clazz, FileItem fileItem) throws Exception {
		return new CamelCaseMapperStrategy(clazz, fileItem);
	}

	public <T> DefaultMapperStrategy getDefaultMapperStrategy(Class<T> clazz) {
		return new DefaultMapperStrategy(clazz);
	}

	public <T> DynamicMapperStrategy getDynamicMapperStrategy(Class<T> clazz, FileItem fileItem) {
		return new DynamicMapperStrategy(clazz, fileItem);
	}

}

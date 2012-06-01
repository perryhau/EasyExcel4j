package com.taobao.common.easyExcel4j;

public class MapperStrategyFactory {

	private static class SingletonHolder {
		private static MapperStrategyFactory instance = new MapperStrategyFactory();
	}

	private MapperStrategyFactory() {

	}

	public static MapperStrategyFactory getInstance() {
		return SingletonHolder.instance;
	}

	public <T> MapperStrategy getCamelCaseMapperStrategy(Class<T> clazz) {
		return new CamelCaseMapperStrategy(clazz);
	}

	public <T> DefaultMapperStrategy getDefaultMapperStrategy(Class<T> clazz) {
		return new DefaultMapperStrategy(clazz);
	}

	public <T> DynamicMapperStrategy getDynamicMapperStrategy(Class<T> clazz) {
		return new DynamicMapperStrategy(clazz);
	}

}

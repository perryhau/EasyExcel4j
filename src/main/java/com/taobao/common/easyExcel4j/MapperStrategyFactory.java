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

	public <T> CamelCaseMapperStrategy getCamelCaseMapperStrategy(Class<T> clazz) throws Exception {
		return new CamelCaseMapperStrategy(clazz);
	}

	public <T> DefaultMapperStrategy getDefaultMapperStrategy(Class<T> clazz) {
		return new DefaultMapperStrategy(clazz);
	}

	public <T> DynamicMapperStrategy getDynamicMapperStrategy(Class<T> clazz) {
		return new DynamicMapperStrategy(clazz);
	}

	public <T> XmlMapperStrategy getXmlMapperStrategy(Class<T> clazz, String... xmlPath)
			throws Exception {
		return new XmlMapperStrategy(clazz, xmlPath);
	}

}

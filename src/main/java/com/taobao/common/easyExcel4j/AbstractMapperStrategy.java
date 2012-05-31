package com.taobao.common.easyExcel4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapperStrategy implements MapperStrategy {

	protected Class<?> clazz;

	private static final ThreadLocal<LinkedHashMap<Class<?>, LinkedList<ExcelObjectMapperDO>>> threadLocalMap = new ThreadLocal<LinkedHashMap<Class<?>, LinkedList<ExcelObjectMapperDO>>>();

	public static LinkedHashMap<Class<?>, LinkedList<ExcelObjectMapperDO>> getThreadLocal() {
		if (threadLocalMap.get() == null) {
			threadLocalMap.set(new LinkedHashMap<Class<?>, LinkedList<ExcelObjectMapperDO>>());
		}
		return threadLocalMap.get();
	}

	public <T> AbstractMapperStrategy(Class<T> clazz) {
		this.clazz = clazz;
		if (!AbstractMapperStrategy.getThreadLocal().containsKey(clazz)) {
			AbstractMapperStrategy.getThreadLocal().put(clazz, new LinkedList<ExcelObjectMapperDO>());
		} else {
			AbstractMapperStrategy.getThreadLocal().get(clazz).clear();
		}
	}

	protected static <T> void add(Class<T> clazz, ExcelObjectMapperDO eom) {
		getThreadLocal().get(clazz).add(eom);
	}

	@Override
	public ExcelObjectMapperDO get(int excelColumnNum) throws Exception {
		Map<Class<?>, LinkedList<ExcelObjectMapperDO>> mappers = AbstractMapperStrategy.getThreadLocal();
		if (mappers == null || !mappers.containsKey(clazz)) {
			throw new Exception("Can't find excel mapping.");
		}
		for (ExcelObjectMapperDO mapper : mappers.get(clazz)) {
			if (mapper.getExcelColumnNum() == excelColumnNum) {
				return mapper;
			}
		}
		return null;
	}

	@Override
	public List<ExcelObjectMapperDO> getMapperDOs() {
		return AbstractMapperStrategy.getThreadLocal().get(clazz);
	}

	@Override
	public List<ExcelObjectMapperDO> getAbsenceExcelColumn() {
		List<ExcelObjectMapperDO> absenceList = new ArrayList<ExcelObjectMapperDO>();
		for (ExcelObjectMapperDO eom : getMapperDOs()) {
			if (eom.isRequired() && eom.getExcelColumnNum() == null) {
				absenceList.add(eom);
			}
		}
		return absenceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getTargetObject() throws Exception {
		return (T) this.clazz.newInstance();
	}

}

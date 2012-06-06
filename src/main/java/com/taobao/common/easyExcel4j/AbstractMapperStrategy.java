package com.taobao.common.easyExcel4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

public abstract class AbstractMapperStrategy implements MapperStrategy {

	private static final ThreadLocal<Map<Class<?>, List<ExcelObjectMapperDO>>> threadLocalMap = new ThreadLocal<Map<Class<?>, List<ExcelObjectMapperDO>>>();

	protected Class<?> clazz;
	protected FileItem fileItem;

	public <T> AbstractMapperStrategy(Class<T> clazz, FileItem fileItem) {
		this.clazz = clazz;
		this.fileItem = fileItem;
	}

	protected static List<ExcelObjectMapperDO> getMapperDOs(Class<?> clazz) {
		Map<Class<?>, List<ExcelObjectMapperDO>> map = threadLocalMap.get();
		if (map == null) {
			map = new HashMap<Class<?>, List<ExcelObjectMapperDO>>();
			threadLocalMap.set(map);
		}
		List<ExcelObjectMapperDO> mappers = map.get(clazz);
		if (mappers == null) {
			mappers = new ArrayList<ExcelObjectMapperDO>();
			map.put(clazz, mappers);
		}
		return mappers;
	}

	protected static <T> void add(Class<T> clazz, ExcelObjectMapperDO eom) {
		getMapperDOs(clazz).add(eom);
	}

	@Override
	public ExcelObjectMapperDO get(int excelColumnNum) throws Exception {
		for (ExcelObjectMapperDO mapper : getMapperDOs(clazz)) {
			if (mapper.getExcelColumnNum() == excelColumnNum) {
				return mapper;
			}
		}
		return null;
	}

	@Override
	public List<ExcelObjectMapperDO> getMapperDOs() {
		return getMapperDOs(clazz);
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
	public <T> T newInstance() throws Exception {
		return (T) this.clazz.newInstance();
	}

	@Override
	public void clean() {
		threadLocalMap.remove();
	}
	
	@Override
	public void intValueMap(String objectFieldName, Map<String, ?> valueMap) {
		for (ExcelObjectMapperDO eom : getMapperDOs()) {
			if (eom.getObjectFieldName().equals(objectFieldName)) {
				eom.setValueMap(valueMap);
				break;
			}
		}
	}

}

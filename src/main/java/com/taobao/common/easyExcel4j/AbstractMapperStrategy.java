package com.taobao.common.easyExcel4j;

import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class AbstractMapperStrategy implements MapperStrategy {

	private static final ThreadLocal<Map<Class<?>, ExcelObjectMapper>> threadLocalMap = new ThreadLocal<Map<Class<?>, ExcelObjectMapper>>();

	protected Class<?> clazz;
	protected FileItem fileItem;

	public <T> AbstractMapperStrategy(Class<T> clazz, FileItem fileItem) {
		this.clazz = clazz;
		this.fileItem = fileItem;
	}

	protected static ExcelObjectMapper getMapper(Class<?> clazz) {
		Map<Class<?>, ExcelObjectMapper> map = threadLocalMap.get();
		if (map == null) {
			map = Maps.newHashMap();
			threadLocalMap.set(map);
		}
		ExcelObjectMapper mapper = map.get(clazz);
		if (mapper == null) {
			mapper = new ExcelObjectMapper();
			map.put(clazz, mapper);
		}
		return mapper;
	}

	protected static List<ExcelObjectMapperDO> getMapperDOs(Class<?> clazz) {
		return getMapper(clazz).getList();
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
		List<ExcelObjectMapperDO> absenceList = Lists.newArrayList();
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

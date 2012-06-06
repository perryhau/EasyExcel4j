package com.taobao.common.easyExcel4j;

import java.util.List;
import java.util.Map;

public interface MapperStrategy {

	ExcelObjectMapperDO get(int excelColumnNum) throws Exception;

	List<ExcelObjectMapperDO> getMapperDOs();

	List<ExcelObjectMapperDO> getAbsenceExcelColumn();

	<T> T newInstance() throws Exception;

	void intValueMap(String objectFieldName, Map<String, ?> valueMap);

	void clean();

}

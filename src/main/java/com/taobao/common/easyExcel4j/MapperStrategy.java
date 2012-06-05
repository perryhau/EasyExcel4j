package com.taobao.common.easyExcel4j;

import java.util.List;

public interface MapperStrategy {

	ExcelObjectMapperDO get(int excelColumnNum) throws Exception;

	List<ExcelObjectMapperDO> getMapperDOs();

	List<ExcelObjectMapperDO> getAbsenceExcelColumn();

	<T> T getTargetObject() throws Exception;
	
	void intExcelObjectMapperDO(ExcelObjectMapperDO eom, String excelColumnName, int excelColumnNum);
	
	void clean();

}

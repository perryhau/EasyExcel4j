package com.taobao.common.easyExcel4j;

import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

public interface MapperStrategy {

	ExcelObjectMapperDO get(int excelColumnNum) throws Exception;

	List<ExcelObjectMapperDO> getMapperDOs();

	List<ExcelObjectMapperDO> getAbsenceExcelColumn();

	void intValueMap(String objectFieldName, Map<String, ?> valueMap);

	ExcelConfig getConfig();

	/**
	 * 清除关系
	 */
	void clean();

	/**
	 * 跟Excel建立对应关系
	 */
	void init(FileItem fileItem) throws Exception;

}

package com.taobao.common.easyExcel4j;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

public class CamelCaseMapperStrategy extends AbstractMapperStrategy {

	private <T> CamelCaseMapperStrategy(Class<T> clazz) {
		super(clazz);
	}

	public <T> CamelCaseMapperStrategy(Class<T> clazz, FileItem fileItem) throws Exception {
		super(clazz);
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return;
		}

		for (Field field : fields) {
			ExcelObjectMapperDO eom = new ExcelObjectMapperDO();
			eom.setObjectFieldName(field.getName());
			eom.setObjectFieldType(field.getType());
			add(clazz, eom);
		}

		// 找到第一行
		HSSFRow row = EasyExcelUtils.getRow(fileItem, 0, 0);
		// 根据字段名称找列号
		Iterator<?> it = row.cellIterator();
		for (int i = 0; it.hasNext(); i++) {
			HSSFCell cell = (HSSFCell) it.next();
			for (ExcelObjectMapperDO eom : getMapperDOs()) {
				if (eom.getObjectFieldName()
						.equals(EasyExcelUtils.toCamelCase(EasyExcelUtils.getCellStringValue(cell)))) {
					eom.setExcelColumnName(EasyExcelUtils.getCellStringValue(cell));
					eom.setExcelColumnNum(i);
				}
			}
		}
	}

	public void intExcelObjectMapperDO(String objectFieldName, Map<String, ?> valueMap) {
		for (ExcelObjectMapperDO eom : getMapperDOs()) {
			if (eom.getObjectFieldName().equals(objectFieldName)) {
				eom.setValueMap(valueMap);
				break;
			}
		}
	}

}

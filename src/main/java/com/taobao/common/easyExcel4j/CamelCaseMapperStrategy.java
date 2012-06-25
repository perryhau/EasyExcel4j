package com.taobao.common.easyExcel4j;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.taobao.common.easyExcel4j.util.CamelCase;
import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

/**
 * 驼峰映射法<br>
 * 将Excel第一行的列名称, 通过驼峰计算后, 与指定对象相同的属性名称匹配<br>
 * 默认所有属性都是required=false
 * 
 * @author feiying.gh
 */
public class CamelCaseMapperStrategy extends AbstractMapperStrategy {

	public <T> CamelCaseMapperStrategy(Class<T> clazz) throws Exception {
		super(new ExcelConfig(clazz));
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return;
		}

		for (Field field : fields) {
			ExcelObjectMapperDO eom = new ExcelObjectMapperDO();
			eom.setObjectFieldName(field.getName());
			eom.setObjectFieldType(field.getType());
			eom.setRequired(false);
			add(clazz, eom);
		}
	}

	@Override
	public void init(FileItem fileItem) throws Exception {
		try {
			// 找到第一行
			Row row = EasyExcelUtils.getRow(fileItem, 0, 0);
			// 根据字段名称找列号
			Iterator<?> it = row.cellIterator();
			for (int i = 0; it.hasNext(); i++) {
				Cell cell = (Cell) it.next();
				for (ExcelObjectMapperDO eom : getMapperDOs()) {
					if (eom.getObjectFieldName().equals(CamelCase.valueOf(EasyExcelUtils.getCellStringValue(cell)))) {
						eom.setExcelColumnName(EasyExcelUtils.getCellStringValue(cell));
						eom.setExcelColumnNum(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			clean();
			throw new Exception("init fileItem error.", e);
		}

	}

}

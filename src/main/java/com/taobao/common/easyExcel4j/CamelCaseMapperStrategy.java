package com.taobao.common.easyExcel4j;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

/**
 * �շ�ӳ�䷨<br>
 * ��Excel��һ�е�������, ͨ���շ�����, ��ָ��������ͬ����������ƥ��<br>
 * Ĭ���������Զ���required=false
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
			// �ҵ���һ��
			HSSFRow row = EasyExcelUtils.getRow(fileItem, 0, 0);
			// �����ֶ��������к�
			Iterator<?> it = row.cellIterator();
			for (int i = 0; it.hasNext(); i++) {
				HSSFCell cell = (HSSFCell) it.next();
				for (ExcelObjectMapperDO eom : getMapperDOs()) {
					if (eom.getObjectFieldName().equals(
							EasyExcelUtils.toCamelCase(EasyExcelUtils
									.getCellStringValue(cell)))) {
						eom.setExcelColumnName(EasyExcelUtils
								.getCellStringValue(cell));
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

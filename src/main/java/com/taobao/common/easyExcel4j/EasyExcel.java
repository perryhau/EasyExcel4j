package com.taobao.common.easyExcel4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

public class EasyExcel {

	private FileItem fileItem;
	private MapperStrategy mapperStrategy;

	public EasyExcel(FileItem fileItem, MapperStrategy mapperStrategy) {
		this.fileItem = fileItem;
		this.mapperStrategy = mapperStrategy;
	}

	public <T> List<T> export() throws Exception {
		try {
			List<T> result = new ArrayList<T>();
			// 检查缺省的Excel column
			List<ExcelObjectMapperDO> absence = mapperStrategy.getAbsenceExcelColumn();
			// 验证所有列明是否存在
			if (absence.size() > 0) {
				throw new Exception("required column [" + absence.toString() + "]");
			}
			// 抽取指定字段的数据
			HSSFSheet sheet = EasyExcelUtils.getSheet(fileItem);
			HSSFRow row = null;
			int maxRowNum = sheet.getLastRowNum();
			for (int i = 1; i <= maxRowNum; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				T t = fill(row, mapperStrategy);
				result.add(t);

			}
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			mapperStrategy.clean();
		}
	}

	/**
	 * 将Excel中的一行转换成DO
	 * 
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private static <T> T fill(HSSFRow row, MapperStrategy mapperStrategy) throws Exception {
		T t = mapperStrategy.getTargetObject();
		for (ExcelObjectMapperDO eom : mapperStrategy.getMapperDOs()) {

			if (!eom.isRequired() && eom.getExcelColumnNum() == null) {
				continue;
			}
			HSSFCell cell = row.getCell(eom.getExcelColumnNum());
			if (eom.isRequired() && StringUtils.isEmpty(EasyExcelUtils.getCellStringValue(cell))) {
				throw new Exception(eom.getExcelColumnName() + "is empty.");
			}

			// 其他的String类型
			if (EasyExcelUtils.getCellStringValue(cell) != null) {
				try {
					Class<?> fieldType = eom.getObjectFieldType();
					if (fieldType.equals(Integer.class) || fieldType.getSimpleName().equals("int")) {
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								new BigDecimal(EasyExcelUtils.getCellStringValue(cell)).intValue());
						continue;
					}
					if (fieldType.getSimpleName().equalsIgnoreCase("long")) {
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								new BigDecimal(EasyExcelUtils.getCellStringValue(cell)).longValue());
						continue;
					}
					if (fieldType.getSimpleName().equalsIgnoreCase("boolean")) {
						if (eom.getValueMap() != null && !eom.getValueMap().isEmpty()) {
							Map<String, ?> valueMap = eom.getValueMap();
							boolean value = (Boolean) valueMap.get(EasyExcelUtils.getCellStringValue(cell));
							BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
									value);
						} else {
							BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
									Boolean.valueOf(EasyExcelUtils.getCellStringValue(cell)));
						}
						continue;
					}
					if (fieldType.getSimpleName().equalsIgnoreCase("Date")) {
						Date value = new Date(Long.parseLong(EasyExcelUtils.getCellStringValue(cell)));
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								value);
						continue;
					}
					BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
							EasyExcelUtils.getCellStringValue(cell));
				} catch (Exception e) {
					throw new Exception(eom.getExcelColumnName() + "set value to object error.", e);
				}
			}
		}
		return t;
	}

}

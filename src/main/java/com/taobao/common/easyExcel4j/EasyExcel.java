package com.taobao.common.easyExcel4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

public class EasyExcel {

	public static List<ExcelObjectMapperDO> getAbsenceExcelColumn(FileItem fileItem, MapperStrategy mapperStrategy)
			throws Exception {
		try {
			// 首先建立映射
			mapperStrategy.init(fileItem);
			return mapperStrategy.getAbsenceExcelColumn();
		} catch (Exception e) {
			throw e;
		}
	}

	public static <T> List<T> export(FileItem fileItem, MapperStrategy mapperStrategy) throws Exception {
		try {

			// 首先建立映射
			mapperStrategy.init(fileItem);

			List<T> result = new ArrayList<T>();
			// 检查缺省的Excel column
			List<ExcelObjectMapperDO> absence = mapperStrategy.getAbsenceExcelColumn();
			// 验证所有列明是否存在
			if (absence.size() > 0) {
				throw new Exception("required column [" + absence.toString() + "]");
			}
			// 抽取指定字段的数据
			Sheet sheet = EasyExcelUtils.getSheet(fileItem, mapperStrategy.getConfig().sheetNum(fileItem));
			Row row = null;
			int maxRowNum = sheet.getLastRowNum();
			Integer rowS = mapperStrategy.getConfig().startRowNum(fileItem);
			Integer rowE = mapperStrategy.getConfig().endRowNum();
			for (int i = 0; i <= maxRowNum; i++) {
				// 开始行
				if (mapperStrategy.getConfig().getMapType().getValue() == MapperEnum.horizontal.getValue() && i < rowS) {
					continue;
				}
				if (mapperStrategy.getConfig().getMapType().getValue() == MapperEnum.vertical.getValue() && i <= rowS) {
					continue;
				}

				row = sheet.getRow(i);
				if (EasyExcelUtils.isNull(row)) {
					continue;
				}

				// 到下一个alias前终止
				// TODO

				// 结束行
				if (rowE != null && i >= rowE) {
					break;
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
	private static <T> T fill(Row row, MapperStrategy mapperStrategy) throws Exception {
		@SuppressWarnings("unchecked")
		T t = (T) mapperStrategy.getConfig().getClazz().newInstance();
		for (ExcelObjectMapperDO eom : mapperStrategy.getMapperDOs()) {

			if (!eom.isRequired() && eom.getExcelColumnNum() == null) {
				continue;
			}
			Cell cell = row.getCell(eom.getExcelColumnNum());
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
						if (!eom.getValueMap().isEmpty()) {
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
					if (fieldType.getSimpleName().equalsIgnoreCase("byte")) {
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								new BigDecimal(EasyExcelUtils.getCellStringValue(cell)).byteValue());
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

package com.taobao.common.easyExcel4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class EasyExcel {

	/**
	 * 点号
	 */
	private static final String DOT = ".";

	public static <T> List<T> export(FileItem fileItem, MapperStrategy mapperStrategy) throws Exception {
		try {
			return doExport(fileItem, mapperStrategy);
		} catch (Exception e) {
			throw e;
		} finally {
			mapperStrategy.clean();
		}
	}

	private static <T> List<T> doExport(FileItem fileItem, MapperStrategy mapperStrategy) throws Exception {
		List<T> result = new ArrayList<T>();
		if (fileItem == null) {
			return result;
		}
		// 检查文件后缀
		String suffix = getTypeByName(fileItem.getName());
		if (suffix == null || !suffix.equalsIgnoreCase("xls")) {
			throw new Exception("Error file type.");
		}
		// 读取文件
		POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 获取第一个sheet
		HSSFSheet sheet = wb.getSheetAt(0);
		// 验证有误数据
		if (sheet == null || sheet.getLastRowNum() < 1) {
			return result;
		}
		// 找到第一行
		HSSFRow row = sheet.getRow(0);
		// check columnNum
		List<ExcelObjectMapperDO> absence = mapperStrategy.getAbsenceExcelColumn();
		if (!absence.isEmpty()) {
			// 根据字段名称找列号
			Iterator<?> it = row.cellIterator();
			for (int i = 0; it.hasNext(); i++) {
				HSSFCell cell = (HSSFCell) it.next();
				for (ExcelObjectMapperDO eom : mapperStrategy.getMapperDOs()) {
					if (eom.getExcelColumnName().equals(getCellStringValue(cell))) {
						eom.setExcelColumnNum(i);
						break;
					}
				}
			}
			absence = mapperStrategy.getAbsenceExcelColumn();
		}

		// 验证所有列明是否存在
		if (absence.size() > 0) {
			throw new Exception("required column [" + absence.toString() + "]");
		}
		// 抽取指定字段的数据
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
	}

	private static String getTypeByName(String fileName) {
		if (fileName == null) {
			return fileName;
		}
		String fileType = fileName;
		int index = -1;
		if ((index = fileName.lastIndexOf(DOT)) > -1) {
			fileType = fileName.substring(index + DOT.length()).toLowerCase();
		}

		return fileType;
	}

	/**
	 * 判断Excel单元格中的内容，统一返回String类型
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellStringValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
		case HSSFCell.CELL_TYPE_FORMULA:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = String.valueOf(cell.getDateCellValue().getTime());
			} else {
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		}

		return value;

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
			if (eom.isRequired() && StringUtils.isEmpty(getCellStringValue(cell))) {
				throw new Exception(eom.getExcelColumnName() + "is empty.");
			}

			// 其他的String类型
			if (getCellStringValue(cell) != null) {
				try {
					Class<?> fieldType = eom.getObjectFieldType();
					if (fieldType.equals(Integer.class)) {
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								new BigDecimal(getCellStringValue(cell)).intValue());
						continue;
					}
					if (fieldType.equals(Long.class)) {
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								new BigDecimal(getCellStringValue(cell)).longValue());
						continue;
					}
					if (fieldType.equals(Boolean.class)) {
						if (eom.getValueMap() != null && !eom.getValueMap().isEmpty()) {
							Map<String, ?> valueMap = eom.getValueMap();
							boolean value = (Boolean) valueMap.get(getCellStringValue(cell));
							BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
									value);
						} else {
							BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
									Boolean.valueOf(getCellStringValue(cell)));
						}
						continue;
					}
					if (fieldType.equals(Date.class)) {
						Date value = new Date(Long.parseLong(getCellStringValue(cell)));
						BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
								value);
						continue;
					}
					BeanUtils.setProperty(t, mapperStrategy.get(eom.getExcelColumnNum()).getObjectFieldName(),
							getCellStringValue(cell));
				} catch (Exception e) {
					throw new Exception(eom.getExcelColumnName() + "set value to object error.", e);
				}
			}
		}
		return t;
	}

}

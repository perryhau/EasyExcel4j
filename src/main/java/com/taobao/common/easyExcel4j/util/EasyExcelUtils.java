package com.taobao.common.easyExcel4j.util;

import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EasyExcelUtils {

	private static final String DOT = ".";

	public static Sheet getSheet(FileItem fileItem) throws Exception {
		return getSheet(fileItem, 0);
	}

	public static Sheet getSheet(FileItem fileItem, int sheetNum) throws Exception {
		Workbook wb = getWorkBook(fileItem);
		int max = wb.getNumberOfSheets();
		if (sheetNum >= max) {
			sheetNum = max - 1;
		}
		if (sheetNum < 0) {
			sheetNum = 0;
		}

		// 获取sheet
		return wb.getSheetAt(sheetNum);
	}

	public static int getSheetNum(FileItem fileItem, String sheetName) throws Exception {
		if (sheetName == null) {
			return 0;
		}
		Workbook wb = getWorkBook(fileItem);
		return wb.getSheetIndex(sheetName);
	}

	public static Workbook getWorkBook(FileItem fileItem) throws Exception {
		if (fileItem == null) {
			throw new IllegalArgumentException("Argument is null.");
		}
		// 检查文件后缀
		String suffix = getFileSuffix(fileItem.getName());
		if (suffix == null || !(suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx"))) {
			throw new IllegalArgumentException("Error file type.");
		}
		// 读取文件
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(fileItem.getInputStream());
		} catch (Exception e) {
			POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
			wb = new HSSFWorkbook(fs);
		}
		return wb;
	}

	public static Row getRow(Sheet sheet, int rowNum) throws Exception {
		// 验证有误数据
		if (sheet == null || sheet.getLastRowNum() < 1) {
			throw new IllegalArgumentException("file content is null.");
		}
		if (rowNum < 0) {
			rowNum = 0;
		}
		// 找到第一行
		return sheet.getRow(rowNum);
	}

	public static int getRowNum(FileItem fileItem, int sheetNum, String name) throws Exception {
		Sheet sheet = getSheet(fileItem, sheetNum);
		Row row = null;
		int maxRowNum = sheet.getLastRowNum();
		for (int i = 0; i <= maxRowNum; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			String alias = getCellStringValue(row.getCell(0));
			if (alias != null && alias.equals(name)) {
				return i + 1;
			}
		}
		return -1;
	}

	public static Row getRow(FileItem fileItem, int sheetNum, int rowNum) throws Exception {
		Sheet sheet = getSheet(fileItem, sheetNum);
		return getRow(sheet, rowNum);
	}

	public static boolean isNull(Row row) {
		if (row == null) {
			return true;
		}
		Iterator<?> it = row.cellIterator();
		for (int i = 0; it.hasNext(); i++) {
			Cell cell = (Cell) it.next();
			String value = getCellStringValue(cell);
			if (StringUtils.isNotBlank(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 得到文件后缀
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName) {
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
	public static String getCellStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
		case Cell.CELL_TYPE_FORMULA:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = String.valueOf(cell.getDateCellValue().getTime());
			} else {
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		}

		return value;

	}

}

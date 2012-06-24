package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

public class ExcelConfig {
	/**
	 * 默认取第一个工作表
	 */
	public static final int DEFAULT_SHEET_NUM = 0;

	/**
	 * 默认从工作表的第1行开始
	 */
	public static final int DEFAULT_ROW_START_NUM = 0;

	/**
	 * 工作表位置
	 */
	private Integer sheetNum;

	/**
	 * 工作表名称
	 */
	private String sheetName;

	/**
	 * 工作表的开始行
	 */
	private Integer startRowNum;

	private Integer rowLength;

	/**
	 * 工作表的结束行
	 */
	private Integer endRowNum;

	/**
	 * 別名，一个新的映射的开始<br>
	 * 默认没有别名
	 */
	private String alias;

	/**
	 * 对应的类
	 */
	private Class<?> clazz;

	/**
	 * 结构：垂直、水平
	 */
	private MapperEnum mapType;

	public <T> ExcelConfig(Class<T> clazz) {
		this.clazz = clazz;

		// 默认为垂直结构
		mapType = MapperEnum.vertical;
	}

	/**
	 * 返回对应的Class，不能被修改，无set方法
	 * 
	 * @return
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setSheetNum(Integer sheetNum) {
		this.sheetNum = sheetNum;
	}

	public void setStartRowNum(Integer startRowNum) {
		this.startRowNum = startRowNum;
	}

	public int sheetNum(FileItem fileItem) throws Exception {
		if (sheetName != null) {
			int n = EasyExcelUtils.getSheetNum(fileItem, sheetName);
			if (sheetNum != null && n != sheetNum) {
				throw new Exception("工作表实际序列是" + n + ", 期望是" + sheetNum);
			}
			sheetNum = n;
		}
		if (sheetNum == null) {
			sheetNum = DEFAULT_SHEET_NUM;
		}
		return sheetNum;
	}

	public int startRowNum(FileItem fileItem) throws Exception {
		if (alias != null) {
			int n = EasyExcelUtils.getRowNum(fileItem, sheetNum, alias);
			if (startRowNum != null && n != startRowNum) {
				throw new Exception("工作表实际序列是" + n + ", 期望是" + startRowNum);
			}
			startRowNum = n;
		}
		if (startRowNum == null) {
			startRowNum = DEFAULT_ROW_START_NUM;
		}
		return startRowNum;
	}

	public Integer endRowNum() {
		if (rowLength != null && rowLength > 0) {
			endRowNum = startRowNum + rowLength;
		}
		return endRowNum;
	}

	public MapperEnum getMapType() {
		return mapType;
	}

	public void setMapType(MapperEnum mapType) {
		this.mapType = mapType;
	}

	public Integer getRowLength() {
		return rowLength;
	}

	public void setRowLength(Integer rowLength) {
		this.rowLength = rowLength;
	}

}

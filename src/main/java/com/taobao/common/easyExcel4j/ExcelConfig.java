package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

import com.taobao.common.easyExcel4j.util.EasyExcelUtils;

public class ExcelConfig {
	/**
	 * Ĭ��ȡ��һ��������
	 */
	public static final int DEFAULT_SHEET_NUM = 0;

	/**
	 * Ĭ�ϴӹ�����ĵ�1�п�ʼ
	 */
	public static final int DEFAULT_ROW_START_NUM = 0;

	/**
	 * ������λ��
	 */
	private Integer sheetNum;

	/**
	 * ����������
	 */
	private String sheetName;

	/**
	 * ������Ŀ�ʼ��
	 */
	private Integer startRowNum;

	private Integer rowLength;

	/**
	 * ������Ľ�����
	 */
	private Integer endRowNum;

	/**
	 * �e����һ���µ�ӳ��Ŀ�ʼ<br>
	 * Ĭ��û�б���
	 */
	private String alias;

	/**
	 * ��Ӧ����
	 */
	private Class<?> clazz;

	/**
	 * �ṹ����ֱ��ˮƽ
	 */
	private MapperEnum mapType;

	public <T> ExcelConfig(Class<T> clazz) {
		this.clazz = clazz;

		// Ĭ��Ϊ��ֱ�ṹ
		mapType = MapperEnum.vertical;
	}

	/**
	 * ���ض�Ӧ��Class�����ܱ��޸ģ���set����
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
				throw new Exception("������ʵ��������" + n + ", ������" + sheetNum);
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
				throw new Exception("������ʵ��������" + n + ", ������" + startRowNum);
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

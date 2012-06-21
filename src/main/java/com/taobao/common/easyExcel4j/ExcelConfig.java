package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

public class ExcelConfig {

	private FileItem fileItem;
	private Class<?> clazz;
	private String xmlPath;
	
	public ExcelConfig(){
		
	}

	public ExcelConfig(FileItem fileItem, Class<?> clazz, String xmlPath) {
		this.fileItem = fileItem;
		this.clazz = clazz;
		this.xmlPath = xmlPath;
	}

	public FileItem getFileItem() {
		return fileItem;
	}

	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

}

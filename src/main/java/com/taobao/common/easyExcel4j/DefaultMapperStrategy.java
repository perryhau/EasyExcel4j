package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

public class DefaultMapperStrategy extends AbstractMapperStrategy {

	public <T> DefaultMapperStrategy(Class<T> clazz, FileItem fileItem) {
		super(clazz, fileItem);
	}

}

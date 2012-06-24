package com.taobao.common.easyExcel4j;

import org.apache.commons.fileupload.FileItem;

public class DefaultMapperStrategy extends AbstractMapperStrategy {

	public <T> DefaultMapperStrategy(Class<T> clazz) {
		super(new ExcelConfig(clazz));
	}

	@Override
	public void init(FileItem fileItem) throws Exception {
		// TODO Auto-generated method stub
	}

}

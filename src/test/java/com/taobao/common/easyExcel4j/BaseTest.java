package com.taobao.common.easyExcel4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

public class BaseTest {

	static {
		PropertyConfigurator.configure(BaseTest.class.getClassLoader().getResource("log4j.properties"));
	}

	protected FileItem createFileItem(String path, String fileName) {
		FileItemFactory factory = new DiskFileItemFactory(16, null);
		String textFieldName = "textField";
		FileItem item = factory.createItem(textFieldName, "text/plain", true, fileName);
		File newfile = new File(path + fileName);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		try {
			FileInputStream fis = new FileInputStream(newfile);
			OutputStream os = item.getOutputStream();
			while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return item;
	}

	@Test
	public void testApp() {

	}

}

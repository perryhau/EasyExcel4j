package com.taobao.common.easyExcel4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Test;

public class EasyExcelTest {

	@Test
	public void testTarget() throws Exception {
		MapperStrategyFactory factory = MapperStrategyFactory.getInstance();
		DefaultMapperStrategy strategy = factory.getDefaultMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("ÐÕÃû"));

		String path = this.getClass().getResource("/").getPath();
		FileItem fileItem = createFileItem(path, "test1.xls");
		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals("ÕÅÈý", list.get(0).getUserName());
	}

	private FileItem createFileItem(String path, String fileName) {
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

}

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
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class EasyExcelTest extends BaseTest {

	@Test
	public void testDynamicMapperStrategy() throws Exception {

		MapperStrategyFactory factory = MapperStrategyFactory.getInstance();
		DynamicMapperStrategy strategy = factory.getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("姓名"));
		user.setAge(strategy.anyInteger("年龄"));
		user.setGender(strategy.anyBoolean("性别", "男", "女"));
		user.setPhone(strategy.anyLong("手机"));
		user.setBirth(strategy.anyDate("出生日期"));
		user.setSalary(strategy.anyDouble("工资"));

		String path = this.getClass().getResource("/").getPath();
		FileItem fileItem = createFileItem(path, "test1.xls");

		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		EeUser result = list.get(0);
		Assert.assertEquals("张三", result.getUserName());
		Assert.assertEquals(25, result.getAge());
		Assert.assertEquals(true, result.isGender());
		Assert.assertEquals(12312341234L, result.getPhone());
		Assert.assertEquals(DateUtils.parseDate("1985/5/3", new String[] { "yyyy/M/d" }), result.getBirth());
		Assert.assertEquals(100.25d, result.getSalary());

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

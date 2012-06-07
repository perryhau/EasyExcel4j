package com.taobao.common.easyExcel4j;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class XmlMapperTest extends BaseTest {

	private FileItem fileItem = createFileItem(this.getClass().getResource("/").getPath(), "XmlMapperTest.xls");

	@Test
	public void testDynamicMapperStrategy() throws Exception {

		String path = this.getClass().getResource("/").getPath() + "xmltest1.xml";
		XmlMapperStrategy strategy = MapperStrategyFactory.getInstance().getXmlMapperStrategy(EeUser.class, fileItem,
				path);

		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		EeUser result = list.get(0);
		Assert.assertEquals("ÕÅÈý", result.getUserName());
		Assert.assertEquals(25, result.getAge());
		Assert.assertEquals(true, result.isGender());
		Assert.assertEquals(12312341234L, result.getPhone());
		Assert.assertEquals(DateUtils.parseDate("1985/5/3", new String[] { "yyyy/M/d" }), result.getBirth());
		Assert.assertEquals(100.25d, result.getSalary());

	}

}

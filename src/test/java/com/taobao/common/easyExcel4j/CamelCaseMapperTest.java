package com.taobao.common.easyExcel4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class CamelCaseMapperTest extends BaseTest {

	private FileItem fileItem = createFileItem(this.getClass().getResource("/").getPath(), "CamelCaseMapperTest.xls");

	@Test
	public void testDynamicMapperStrategy() throws Exception {

		CamelCaseMapperStrategy strategy = MapperStrategyFactory.getInstance().getCamelCaseMapperStrategy(EeUser.class, fileItem);
		Map<String, Boolean> valueMap = new HashMap<String, Boolean>();
		valueMap.put("ÄÐ", true);
		valueMap.put("Å®", false);
		strategy.intExcelObjectMapperDO("gender", valueMap);
		EasyExcel ee = new EasyExcel(fileItem, strategy);
		List<EeUser> list = ee.export();
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

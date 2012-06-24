package com.taobao.common.easyExcel4j;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.google.common.collect.Maps;

public class CamelCaseMapperTest extends BaseTest {

	private FileItem fileItem = createFileItem(this.getClass().getResource("/").getPath(), "CamelCaseMapperTest.xls");

	@Test
	public void testDynamicMapperStrategy() throws Exception {

		CamelCaseMapperStrategy strategy = MapperStrategyFactory.getInstance().getCamelCaseMapperStrategy(EeUser.class);
		Map<String, Boolean> valueMap = Maps.newHashMap();
		valueMap.put("男", true);
		valueMap.put("女", false);
		strategy.intValueMap("gender", valueMap);
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
	
	@Test
	public void testDynamicMapperStrategy1() throws Exception {

		MapperStrategy strategy = MapperStrategyFactory.getInstance().getCamelCaseMapperStrategy(EeUser.class);
		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		EeUser result = list.get(0);
		Assert.assertEquals("张三", result.getUserName());
		Assert.assertEquals(25, result.getAge());
		Assert.assertEquals(false, result.isGender());
		Assert.assertEquals(12312341234L, result.getPhone());
		Assert.assertEquals(DateUtils.parseDate("1985/5/3", new String[] { "yyyy/M/d" }), result.getBirth());
		Assert.assertEquals(100.25d, result.getSalary());

	}

}

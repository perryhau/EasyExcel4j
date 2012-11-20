package com.taobao.common.easyExcel4j;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class DynamicMapperTest extends BaseTest {

	private FileItem fileItem = createFileItem(this.getClass().getResource("/").getPath(), "DynamicMapperTest.xls");

	@Test
	public void testAbsence_1() throws Exception {
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setDescription(strategy.anyString("不存在的"));

		List<ExcelObjectMapperDO> list = EasyExcel.getAbsenceExcelColumn(fileItem, strategy);

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		strategy.clean();
	}

	@Test
	public void testAbsence_2() throws Exception {
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setDescription(strategy.anyString("不存在的", false));

		List<ExcelObjectMapperDO> list = EasyExcel.getAbsenceExcelColumn(fileItem, strategy);

		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());

		strategy.clean();
	}

	@Test
	public void testDynamicMapperStrategy() throws Exception {
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("姓名"));
		user.setAge(strategy.anyInteger("年龄"));
		user.setGender(strategy.anyBoolean("性别", "男", "女"));
		user.setPhone(strategy.anyLong("手机"));
		user.setBirth(strategy.anyDate("出生日期"));
		user.setSalary(strategy.anyDouble("工资"));
		user.setStatus(strategy.anyByte("状态"));

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
		Assert.assertEquals(Byte.valueOf("1"), result.getStatus());

	}
	
	/**
	 * 检查column后，导出
	 * @throws Exception
	 */
	@Test
	public void testDynamicMapperStrategy_1() throws Exception {
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("姓名", true));
		user.setAge(strategy.anyInteger("年龄"));
		user.setGender(strategy.anyBoolean("性别", "男", "女"));
		user.setPhone(strategy.anyLong("手机"));
		user.setBirth(strategy.anyDate("出生日期"));
		user.setSalary(strategy.anyDouble("工资"));
		
		List<ExcelObjectMapperDO> absence = EasyExcel.getAbsenceExcelColumn(fileItem, strategy);
		Assert.assertEquals(0, absence.size());

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
	
	/**
	 * 只有title，返回list.size()=0
	 * @throws Exception
	 */
	@Test
	public void testDynamicMapperStrategy_only_title() throws Exception {
		fileItem = createFileItem(this.getClass().getResource("/").getPath(), "DynamicMapperTest_only_title.xls");
		
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("姓名", true));
		user.setAge(strategy.anyInteger("年龄"));
		user.setGender(strategy.anyBoolean("性别", "男", "女"));
		user.setPhone(strategy.anyLong("手机"));
		user.setBirth(strategy.anyDate("出生日期"));
		user.setSalary(strategy.anyDouble("工资"));
		
		List<ExcelObjectMapperDO> absence = EasyExcel.getAbsenceExcelColumn(fileItem, strategy);
		Assert.assertEquals(0, absence.size());

		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());
	}
	
	/**
	 * 没有内容，抛异常
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testDynamicMapperStrategy_null() throws Exception {
		fileItem = createFileItem(this.getClass().getResource("/").getPath(), "DynamicMapperTest_null.xls");
		
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(EeUser.class);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("姓名", true));
		user.setAge(strategy.anyInteger("年龄", false));
		user.setGender(strategy.anyBoolean("性别", "男", "女"));
		user.setPhone(strategy.anyLong("手机"));
		user.setBirth(strategy.anyDate("出生日期"));
		user.setSalary(strategy.anyDouble("工资"));
		
		List<ExcelObjectMapperDO> absence = EasyExcel.getAbsenceExcelColumn(fileItem, strategy);
		Assert.assertEquals(5, absence.size());

		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());
	}


	@Test
	public void testWT() throws Exception {
		fileItem = createFileItem(this.getClass().getResource("/").getPath(), "合作方范本.xlsx");

		// 所属频道 所属图集
		ExcelConfig config_1 = new ExcelConfig(WanTuGroup.class);
		config_1.setAlias("图集信息");
		config_1.setMapType(MapperEnum.horizontal);
		config_1.setRowLength(1);
		DynamicMapperStrategy strategy_1 = new DynamicMapperStrategy(config_1);
		WanTuGroup wanTuGroup = strategy_1.getInstance();
		wanTuGroup.setChannel(strategy_1.anyString("所属频道"));
		wanTuGroup.setGroup(strategy_1.anyString("所属图集"));

		List<WanTuGroup> list_1 = EasyExcel.export(fileItem, strategy_1);
		Assert.assertEquals(1, list_1.size());

		WanTuGroup wtg = list_1.get(0);
		Assert.assertEquals("明星", wtg.getChannel());
		Assert.assertEquals("锋芝复合", wtg.getGroup());

		// 图片描述 图片标签 图片ＵＲＬ 图片商品ＵＲＬ 图片来源
		ExcelConfig config_2 = new ExcelConfig(WanTuInfo.class);
		config_2.setAlias("图片信息");
		DynamicMapperStrategy strategy_2 = new DynamicMapperStrategy(config_2);
		WanTuInfo wanTuInfo = strategy_2.getInstance();
		wanTuInfo.setDesc(strategy_2.anyString("图片描述"));
		wanTuInfo.setFrom(strategy_2.anyString("图片来源"));
		wanTuInfo.setItemUrl(strategy_2.anyString("图片商品ＵＲＬ"));
		wanTuInfo.setPicUrl(strategy_2.anyString("图片ＵＲＬ"));
		wanTuInfo.setTag(strategy_2.anyString("图片标签"));

		List<WanTuInfo> list_2 = EasyExcel.export(fileItem, strategy_2);

		Assert.assertEquals(1, list_2.size());

		WanTuInfo wti = list_2.get(0);
		Assert.assertEquals("好可爱好可爱好可爱", wti.getDesc());
		Assert.assertEquals("女装", wti.getTag());
		Assert.assertEquals(
				"http://img03.taobaocdn.com/imgextra/i3/17577017030820661/T1e7zjXeJkXXXXXXXX_!!397007577-0-pix.jpg",
				wti.getPicUrl());
		Assert.assertEquals("http://item.taobao.com/item.htm?spm=161.1000655.0.146&id=1", wti.getItemUrl());
		Assert.assertEquals("www.lc.com", wti.getFrom());
	}

}

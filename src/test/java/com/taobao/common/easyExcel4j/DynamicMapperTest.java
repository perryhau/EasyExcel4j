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
		ExcelConfig config = new ExcelConfig();
		config.setClazz(EeUser.class);
		config.setFileItem(fileItem);
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(config);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setDescription(strategy.anyString("�����ڵ�"));
		strategy.generic();

		List<ExcelObjectMapperDO> list = strategy.getAbsenceExcelColumn();

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		strategy.clean();
	}

	@Test
	public void testAbsence_2() throws Exception {
		ExcelConfig config = new ExcelConfig();
		config.setClazz(EeUser.class);
		config.setFileItem(fileItem);
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(config);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setDescription(strategy.anyString("�����ڵ�", false));

		List<ExcelObjectMapperDO> list = strategy.getAbsenceExcelColumn();

		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());

		strategy.clean();
	}

	@Test
	public void testDynamicMapperStrategy() throws Exception {

		ExcelConfig config = new ExcelConfig();
		config.setClazz(EeUser.class);
		config.setFileItem(fileItem);
		DynamicMapperStrategy strategy = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(config);
		EeUser user = strategy.getInstance();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(strategy.anyString("����"));
		user.setAge(strategy.anyInteger("����"));
		user.setGender(strategy.anyBoolean("�Ա�", "��", "Ů"));
		user.setPhone(strategy.anyLong("�ֻ�"));
		user.setBirth(strategy.anyDate("��������"));
		user.setSalary(strategy.anyDouble("����"));

		strategy.generic();
		List<EeUser> list = EasyExcel.export(fileItem, strategy);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		EeUser result = list.get(0);
		Assert.assertEquals("����", result.getUserName());
		Assert.assertEquals(25, result.getAge());
		Assert.assertEquals(true, result.isGender());
		Assert.assertEquals(12312341234L, result.getPhone());
		Assert.assertEquals(DateUtils.parseDate("1985/5/3", new String[] { "yyyy/M/d" }), result.getBirth());
		Assert.assertEquals(100.25d, result.getSalary());

	}

	@Test
	public void testWT() throws Exception {
		fileItem = createFileItem(this.getClass().getResource("/").getPath(), "����������.xlsx");

		DynamicMapperStrategy strategy1 = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(
				WanTuGroup.class, fileItem);
		strategy1.setAlias("ͼ����Ϣ");

		DynamicMapperStrategy strategy2 = MapperStrategyFactory.getInstance().getDynamicMapperStrategy(WanTuInfo.class,
				fileItem);
		strategy2.setAlias("ͼƬ��Ϣ");
	}

}

package com.taobao.common.easyExcel4j;

import junit.framework.Assert;

import org.junit.Test;

public class EasyExcelTest {

	@Test
	public void testTarget() {
		EeUser user = EasyExcel.target(EeUser.class);
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof EeUser);

		user.setUserName(EasyExcel.extractString(columnName))
		//EasyExcel.initProp(user.setUserName("abc"));
		
	}

}

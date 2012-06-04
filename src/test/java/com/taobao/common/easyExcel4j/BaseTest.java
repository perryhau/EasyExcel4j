package com.taobao.common.easyExcel4j;

import org.apache.log4j.PropertyConfigurator;

public class BaseTest {

	static {
		PropertyConfigurator.configure(BaseTest.class.getClassLoader().getResource("log4j.properties"));
	}

}

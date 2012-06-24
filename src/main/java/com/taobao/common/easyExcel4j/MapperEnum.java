package com.taobao.common.easyExcel4j;

public enum MapperEnum {

	horizontal(0, "水平结构"), vertical(1, "垂直结构");

	private int value;
	private String desc;

	MapperEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}

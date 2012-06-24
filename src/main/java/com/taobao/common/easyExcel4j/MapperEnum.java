package com.taobao.common.easyExcel4j;

public enum MapperEnum {

	horizontal(0, "ˮƽ�ṹ"), vertical(1, "��ֱ�ṹ");

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

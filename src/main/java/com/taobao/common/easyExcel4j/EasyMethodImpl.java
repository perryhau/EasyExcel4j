package com.taobao.common.easyExcel4j;

public class EasyMethodImpl<T> implements EasyMethod<T> {

	private boolean required;

	public EasyMethodImpl(boolean b) {
		this.required = true;
	}

	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

}

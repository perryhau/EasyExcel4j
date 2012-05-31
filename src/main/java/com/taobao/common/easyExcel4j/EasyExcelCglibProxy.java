package com.taobao.common.easyExcel4j;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class EasyExcelCglibProxy implements MethodInterceptor {

	public Class<?> clazz;

	public <T> EasyExcelCglibProxy(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		ExcelObjectMapperDO eom = AbstractMapperStrategy.getThreadLocal().get(clazz).getLast();
		if (eom != null) {
			eom.setMethod(method);
			eom.setObjectFieldName(toLowerCaseInitial(method.getName().substring(3), true));
		}
		return proxy.invokeSuper(obj, args);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProxyInstance() {
		Enhancer enhancer = new Enhancer();
		// ����Ҫ�����������
		enhancer.setSuperclass(clazz);
		// ���ûص��Ķ���
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	/**
	 * ��һ���ַ���������ĸ��Ϊ��д����Сд
	 * 
	 * @param srcString
	 * @param flag
	 *            ��Сд��ʶ��tureСд��false��Щ
	 * @return ��д������ַ���
	 */
	private static String toLowerCaseInitial(String srcString, boolean flag) {
		StringBuilder sb = new StringBuilder();
		if (flag) {
			sb.append(Character.toLowerCase(srcString.charAt(0)));
		} else {
			sb.append(Character.toUpperCase(srcString.charAt(0)));
		}
		sb.append(srcString.substring(1));
		return sb.toString();
	}

}

package com.taobao.common.easyExcel4j;

import java.lang.reflect.Method;
import java.util.List;

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
		List<ExcelObjectMapperDO> list = AbstractMapperStrategy.getMapperDOs(clazz);
		ExcelObjectMapperDO eom = list.get(list.size() > 0 ? (list.size() - 1) : 0);
		if (eom != null) {
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

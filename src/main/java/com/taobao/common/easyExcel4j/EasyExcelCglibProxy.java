package com.taobao.common.easyExcel4j;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class EasyExcelCglibProxy<T> implements MethodInterceptor {

	private static final ThreadLocal<Method> threadLocal = new ThreadLocal<Method>();

	public Class<T> clazz;

	public EasyExcelCglibProxy(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		threadLocal.set(method);
		// ͨ��������ʵ�����ø���ķ���������Ŀ��ҵ���෽���ĵ���
		return proxy.invokeSuper(obj, args);
	}

	// ����MethodInterceptor�ӿڵ�getProxy()����������
	@SuppressWarnings("unchecked")
	public T getProxyInstance() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz); // ����Ҫ�����������
		enhancer.setCallback(this); // ���ûص��Ķ���
		return (T) enhancer.create(); // ͨ���ֽ��뼼����̬��������ʵ��,
	}

}

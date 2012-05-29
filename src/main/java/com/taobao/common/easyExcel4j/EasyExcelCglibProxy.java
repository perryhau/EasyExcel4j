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
		// 通过代理类实例调用父类的方法，即是目标业务类方法的调用
		return proxy.invokeSuper(obj, args);
	}

	// 覆盖MethodInterceptor接口的getProxy()方法，设置
	@SuppressWarnings("unchecked")
	public T getProxyInstance() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz); // 设者要创建子类的类
		enhancer.setCallback(this); // 设置回调的对象
		return (T) enhancer.create(); // 通过字节码技术动态创建子类实例,
	}

}

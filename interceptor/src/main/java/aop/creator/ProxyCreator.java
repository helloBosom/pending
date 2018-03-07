package aop.creator;

import aop.interceptor.ProxyInterceptor;

import java.util.List;

public interface ProxyCreator {
    <T> T createProxy(final Class<?> targetClass, final List<ProxyInterceptor> interceptors);
}

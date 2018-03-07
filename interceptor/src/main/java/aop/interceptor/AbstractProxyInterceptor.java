package aop.interceptor;

import aop.chain.DefaultProxyChain;

import java.lang.reflect.Method;

/**
 * interceptor的拦截还可以提供不同的逻辑。
 * 例如：1.  只写before(targetClass, method, params);
 * result = chain.doChain();就和spring aop的aop:before的实现一样。
 * 只写result = chain.doChain();  after(targetClass, method, params, result);
 * 就和spring aop的aop:after的实现一样。
 */
public abstract class AbstractProxyInterceptor implements ProxyInterceptor {
    @Override
    public Object intercept(DefaultProxyChain chain) throws Throwable {
        Object result;
        Class<?> targetClass = chain.getTargetClass();
        Method method = chain.getMethod();
        Object[] params = chain.getParams();

        if (support()) {
            before(targetClass, method, params);
            result = chain.doChain();
            after(targetClass, method, params, result);
        } else {
            result = chain.doChain();
        }
        return result;
    }

    public abstract void before(Class<?> targetClass, Method method, Object[] params);

    public abstract void after(Class<?> targetClass, Method method, Object[] params, Object result);

    @Override
    public boolean support() {
        return true;
    }
}

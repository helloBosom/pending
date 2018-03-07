package aop.interceptor;

import aop.chain.DefaultProxyChain;

/**
 * Created by lv on 2017/11/18 0011.
 */
public interface ProxyInterceptor {

    Object intercept(DefaultProxyChain chain) throws Throwable;

    boolean support();
}

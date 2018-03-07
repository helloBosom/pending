package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationImpl implements InvocationHandler {

    private Object object;

    public ProxyInvocationImpl(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("为动态代理对象进行代理----before");
        Object returnValue = method.invoke(object, args);
        System.out.println("为动态代理对象进行代理----after");
        return returnValue;
    }
}

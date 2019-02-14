package proxy.dynamic;

import proxy.UserInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserHandler implements InvocationHandler {

    private UserInfo userImpl;

    public UserHandler(UserInfo userImpl) {
        this.userImpl = userImpl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object = null;
        //代理操作
        if(method.getName().equals("queryUser")) {
            object = method.invoke(userImpl, args);
        }
        //代理操作
        return object;
    }
}

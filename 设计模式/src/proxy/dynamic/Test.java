package proxy.dynamic;

import proxy.UserImpl;
import proxy.UserInfo;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        UserInfo userImpl = new UserImpl();
        UserHandler handler = new UserHandler(userImpl);
        UserInfo userProxy = (UserInfo) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{UserInfo.class}, handler);
        userProxy.queryUser();
    }
}

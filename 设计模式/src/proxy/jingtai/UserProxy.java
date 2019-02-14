package proxy.jingtai;

import proxy.UserInfo;

public class UserProxy implements UserInfo {

    private UserInfo userImpl;

    public UserProxy(UserInfo userImpl) {
        this.userImpl = userImpl;
    }

    @Override
    public void queryUser() {
        //扩展
        userImpl.queryUser();
        //扩展
    }

    @Override
    public void updateUser() {
        //扩展
        userImpl.updateUser();
        //扩展
    }
}

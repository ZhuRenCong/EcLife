package weifen.com.common.app;

import android.app.Application;

import weifen.com.common.db.User;

/**
 * Created by zhurencong on 2017/9/17.
 */
public class BaseApplication extends Application {

    //登录成功保存用户信息
    public static User user=new User();

    @Override
    public void onCreate() {
        super.onCreate();
    }

}

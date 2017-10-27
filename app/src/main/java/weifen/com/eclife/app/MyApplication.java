package weifen.com.eclife.app;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import weifen.com.common.app.BaseApplication;
import weifen.com.common.utils.SPUtil;
import weifen.com.eclife.domain.RongYunInfo;
import weifen.com.request.utils.RequestUtil;

/**
 * Created by zhurencong on 2017/9/17.
 */
public class MyApplication extends BaseApplication{

    private static final String TAG = "MyApplication";

    //保存当前位置
    public static String location="";
    //qq第三方登录的appid
    public static String AA_PP="1106329566";

    public static double longitude;//经度
    public static double latitude;//纬度

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化共享参数
        SPUtil.init(this);
        //给还没注册的用户一个唯一标识
        if("".equals(SPUtil.getUserId("userId"))){
            SPUtil.saveUserId("userId", UUID.randomUUID().toString());
        }

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                @Override
                public UserInfo getUserInfo(String userId) {
                    if(!userId.isEmpty()&&userId!=null){
                        return new UserInfo(SPUtil.getUserId("userId"),SPUtil.getUserId("userId"),Uri.parse("http://hiphotos.baidu.com/%D2%B9_%BC%C5%BE%B2/pic/item/194f3e37d95d1b070b55a9f2.jpg"));
                    }
                    return null;
                }
            }, true);
        }

        //建立连接
        HashMap<String,String>map=new HashMap<>();
        map.put("userid",SPUtil.getUserId("userId"));
        map.put("username",SPUtil.getUserId("userId"));

        RequestUtil.post("http://60.205.204.56/TOKEN/token.php", map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                try {
                    JSONArray jsonArray=new JSONArray("["+param+"]");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if("200".equals(jsonObject.getString("code"))&&jsonObject.getString("token")!=null){
                        RongIM.connect(jsonObject.getString("token"), new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                
                            }

                            @Override
                            public void onSuccess(String s) {
                                Log.d(TAG, "onError: "+"连接成功");
                                Toast.makeText(getApplicationContext(),"chenggong",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.d(TAG, "onError: "+"连接失败");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String error) {

            }
        });

    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}

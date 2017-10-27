package weifen.com.eclife.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.NumberIsValid;
import weifen.com.eclife.R;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.requestResult.RequestResult;

/**
 * Created by zhurencong on 2017/10/9.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText account,password;
    private ImageView showPassword,qqLogin,wechatLogin;
    private Button login,register;
    private TextView foget;
    //qq第三方登录
    private Tencent mTencent;
    private BaseUiListener mListener;

    //是否显示密码 0----不显示  1-----显示
    private int isShow=0;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        account= (EditText) findViewById(R.id.account);
        password= (EditText) findViewById(R.id.password);
        foget= (TextView) findViewById(R.id.foget);
        showPassword= (ImageView) findViewById(R.id.showpassword);
        qqLogin= (ImageView) findViewById(R.id.qq_login);
        wechatLogin= (ImageView) findViewById(R.id.wechat_login);
        login= (Button) findViewById(R.id.login);
        register= (Button) findViewById(R.id.register);

        foget.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        wechatLogin.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()){
            //忘记密码
            case R.id.foget:
                intent=new Intent(this,ForgetActivity.class);
                startActivity(intent);
                finish();
                break;
            //显示密码
            case R.id.showpassword:
                if(isShow==0){
                    //显示密码
                    showPassword.setImageResource(R.mipmap.eye);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.length());
                    isShow=1;
                }else {
                    //隐藏密码
                    showPassword.setImageResource(R.mipmap.not_eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.length());
                    isShow=0;
                }
                break;
            //qq登录
            case R.id.qq_login:
                mTencent=Tencent.createInstance(MyApplication.AA_PP,this.getApplicationContext());
                mListener=new BaseUiListener();
                mTencent.login(LoginActivity.this, "all", mListener);
                //TODO
                break;
            //微信登录
            case R.id.wechat_login:
                break;
            //登录
            case R.id.login:
                if("".equals(account.getText()+"")){
                    Toast.makeText(this,"账号不能为空!",Toast.LENGTH_LONG).show();
                }else if("".equals(password.getText()+"")){
                    Toast.makeText(this,"密码不能为空!",Toast.LENGTH_LONG).show();
                }else if(!NumberIsValid.verifyPhoneNum(account.getText()+"")){
                    Toast.makeText(this,"非法手机号!",Toast.LENGTH_LONG).show();
                }else {
                    RequestResult.login(account.getText()+"",this,true,password.getText()+"");
                }
                break;
            //注册
            case R.id.register:
                intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    private class BaseUiListener implements IUiListener {
        //登录、快速支付登录、应用分享、应用邀请等接口，需传入该回调的实例
        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_LONG).show();

            JSONObject jsonObject = (JSONObject) o;

            //设置openid和token，否则获取不到下面的信息
            initOpenidAndToken(jsonObject);
            System.out.println("o.toString() ------------------------->        " + jsonObject.toString());
            //获取QQ用户的各信息
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {

            Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String openid = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");

            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {

        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);

        userInfo.getUserInfo(new IUiListener() {
                                 @Override
                                 public void onComplete(final Object o) {
                                     JSONObject userInfoJson = (JSONObject) o;
                                     System.out.println("-------"+userInfoJson.toString());
                                     Message msgNick = new Message();
                                     //{"ret":0,"msg":"","is_lost":0,"nickname":"坚持就是胜利","gender":"男","province":"广东","city":"湛江","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1106329566\/A236C2F7AC4390B831FC840AFE462E5F\/30","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1106329566\/A236C2F7AC4390B831FC840AFE462E5F\/50","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1106329566\/A236C2F7AC4390B831FC840AFE462E5F\/100","figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1106329566\/A236C2F7AC4390B831FC840AFE462E5F\/40","figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1106329566\/A236C2F7AC4390B831FC840AFE462E5F\/100","is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}
                                     msgNick.what = 0;//昵称
                                     try {
                                         msgNick.obj = userInfoJson.getString("nickname");//直接传递一个昵称的内容过去
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }
                                  //   mHandler.sendMessage(msgNick);
                                     //子线程 获取并传递头像图片，由Handler更新
                                     new Thread(new Runnable() {
                                         @Override
                                         public void run() {
                                             Bitmap bitmapHead = null;
                                             if (((JSONObject) o).has("figureurl")) {
                                                 try {
                                                     String headUrl = ((JSONObject) o).getString("figureurl_qq_2");
                                            //         bitmapHead = Util.getbitmap(headUrl);
                                                 } catch (JSONException e) {
                                                     e.printStackTrace();
                                                 }
                                                 Message msgHead = new Message();
                                                 msgHead.what = 1;
                                                 msgHead.obj = bitmapHead;
                                              //   mHandler.sendMessage(msgHead);
                                             }
                                         }
                                     }).start();

                                 }

                                 @Override
                                 public void onError(UiError uiError) {
                                     Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onCancel() {
                                     Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                                 }
                             }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }
}

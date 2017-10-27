package weifen.com.eclife.requestResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import weifen.com.common.app.BaseApplication;
import weifen.com.common.db.User;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.DeleteBom;
import weifen.com.common.utils.MD5Util;
import weifen.com.common.utils.SPUtil;
import weifen.com.common.utils.UserDataUtil;
import weifen.com.eclife.activity.IndexActivity;
import weifen.com.eclife.activity.InfoActivity;
import weifen.com.eclife.activity.PublishActivity;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.request.utils.JsonUtil;
import weifen.com.request.utils.RequestUtil;

/**
 * 请求处理结果用
 * Created by zhurencong on 2017/10/12.
 */
public class RequestResult {
    //请求验证码
    public static void getVerify(String tel, final Context context,String url){
        Map<String,String> map=new HashMap<>();
        map.put("request","200");
        map.put("tel",tel);
        RequestUtil.post(url, map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(final String param) {
                try {
                    JSONArray jsonArray=new JSONArray("["+DeleteBom.JSONTokener(param)+"]");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                    String message=jsonObject.getString("message");
                    if("404".equals(code)){
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                        ((Activity)context).finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String error) {
                //发送失败
            }
        });
    }

    //携带验证码注册
    public static void Verify(String tel,String code,String psd, final Context context){
        Map<String,String> map=new HashMap<>();
        map.put("request","100");
        if(MD5Util.toMD5(tel)==null&&MD5Util.toMD5(psd)==null){
            Toast.makeText(context,"手机号或密码不能为空！",Toast.LENGTH_LONG).show();
            return;
        }

        map.put("tel",tel);
        map.put("psd",MD5Util.toMD5(psd));
        map.put("code",code);
        RequestUtil.post("http://60.205.204.56/register.php", map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(final String param) {
                try {
                    JSONArray jsonArray=new JSONArray("["+DeleteBom.JSONTokener(param)+"]");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String codeStr=jsonObject.getString("code");
                    String message=jsonObject.getString("message");
                    if("400".equals(codeStr)){
                        if("".equals(BaseApplication.user.getTel()+"")){
                            BaseApplication.user=new User();
                        }
                        UserDataUtil.dealUserData(jsonObject.getString("data"));
                        Intent intent=new Intent(context, IndexActivity.class);
                        context.startActivity(intent);
                        ActivityCollector.finishActivity((Activity) context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String error) {
                //发送失败
            }
        });
    }

    /***
     *
     * @param tel 账号
     * @param context
     * @param isPwd 密码登录201或验证码登录202
     */
    public static void login(String tel, final Context context,boolean isPwd,String psd){
        Map<String,String> map=new HashMap<>();
        if(MD5Util.toMD5(tel)==null||MD5Util.toMD5(psd)==null){
            return;
        }
        map.put("tel",tel);
        //密码登录
        if (isPwd) {
            map.put("request","201");
            map.put("psd",MD5Util.toMD5(psd));
        }else {
            map.put("request","202");
            map.put("code",psd);
        }
        RequestUtil.post("http://60.205.204.56/login.php", map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(final String param) {
                try {
                    JSONArray jsonArray=new JSONArray("["+ DeleteBom.JSONTokener(param)+"]");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String codeStr=jsonObject.getString("code");
                    String message=jsonObject.getString("message");
                    if("400".equals(codeStr)){
                        if(!"".equals(BaseApplication.user.getTel())){
                            BaseApplication.user=new User();
                        }
                        UserDataUtil.dealUserData(jsonObject.getString("data"));
                        Intent intent=new Intent(context, IndexActivity.class);
                        context.startActivity(intent);
                        ActivityCollector.finishActivity((Activity) context);
                    }else {
                        Toast.makeText(context,"账号或密码错误！",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String error) {
                Toast.makeText(context,"验证码错误！",Toast.LENGTH_LONG).show();
            }
        });
    }

    //上传图片
    public static void upLoad(final Context context, String publishImagePath, String coverImagePath, String url){
        HashMap<String,Object> map=new HashMap<>();
        map.put("photo1",new File(coverImagePath));
        map.put("photo2",new File(publishImagePath));
        RequestUtil.uploadFile(url, map, null, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Log.d("fghsdjgfhjs", "success: "+param);
                //{"code":400,"message":"上传成功","data":[".\/Uploads\/\/9b9eba2835cd66ac1a0512e9b916536b89.png;.\/Uploads\/9b9eba2835cd66ac1a0512e9b916536b27.png"]}
                try {
                    JSONObject jsonObject = new JSONObject(param);
                    int resultCode=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    if(resultCode==400){
                        //1.shoppingImage   2.coverImage
                        String imagePaths=jsonObject.getString("data");
                        PublishActivity publishActivity= (PublishActivity) context;
                        publishActivity.publishShoppingInfo(imagePaths);
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }else {
                        //上传失败
                        Toast.makeText(context,"图片上传失败",Toast.LENGTH_SHORT).show();
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
     * 保存用户信息
     * @param tel
     */
    public static void setUserInfo(final String tel) {
        //http://60.205.204.56/information.php?tel=13544373643
        Map<String,String> params=new HashMap<String,String>();
        params.put("tel",tel);
        BaseApplication.user.setTel(tel);
        RequestUtil.post(URLUtil.QUERY_USER_INFO, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                try {
//                    if(param=="[]"){//TODO 不做任何修改
//                        return;
//                    }
                    JSONObject jsonObject = new JSONObject(param);
                    BaseApplication.user.setUserId(jsonObject.getString("user_id"));
                    BaseApplication.user.setImageUrl(jsonObject.getString("image_url"));
                    BaseApplication.user.setNickName(jsonObject.getString("name"));
                    BaseApplication.user.setIsProve(jsonObject.getString("is_prove"));
                    BaseApplication.user.setGrade(jsonObject.getString("grade"));
                    BaseApplication.user.setMoney(jsonObject.getString("money"));
                    BaseApplication.user.setSignature(jsonObject.getString("signature"));
                    BaseApplication.user.setTel(jsonObject.getString("tel"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void error(String error) {

            }
        });
    }

    //上传图片
    public static void uploadUserPhoto(final Context context, String imagePath){
        HashMap<String,Object> param=new HashMap<>();
        param.put("photo",new File(imagePath));
        RequestUtil.uploadFile(URLUtil.UPLOAD_USER_PHOTO, param, null, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Log.d("uploadUserPhot-----", "success: "+param);
                Log.e("uploadUserPhot-----", "success: "+param);
                try {
                    JSONObject jsonObject=new JSONObject(param);
                    int resultCode=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    JSONObject data=jsonObject.getJSONObject("data");
                    if(resultCode==400){
                        String image_url=data.getString("image_url");
                        BaseApplication.user.setImageUrl(image_url);//设置图片路径
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        //保存个人信息
                        InfoActivity infoActivity= (InfoActivity) context;
                        infoActivity.saveUserInfo();
                    }else{
                        //上传失败
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                {"code":400,"message":"头像上传成功","data":{"image_url":".\/Uploads\/\/1044f5728a7ee6824ff0c67d27ba3f2f36.png;"}}
            }

            @Override
            public void error(String error) {

            }
        });
    }

    //修改用户信息
    public static void updateUserInfo(final Context context, String name, String tel, String signature, String image_url) {
        Map<String,String> params=new HashMap<String,String>();
        params.put("name",name);
        params.put("tel",tel);
        params.put("signature",signature);
        params.put("image_url",image_url);

        RequestUtil.post(URLUtil.UPDATE_USER_INFO, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                try {
                    JSONObject jsonObject=new JSONObject(param);
                    int code=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    String data=jsonObject.getString("data");
                    JSONArray jsonArray=new JSONArray(data);
                    JSONObject userJSON=jsonArray.getJSONObject(0);
                    if(code==400){
                        BaseApplication.user.setNickName(userJSON.getString("name"));
                        BaseApplication.user.setTel(userJSON.getString("tel"));
                        BaseApplication.user.setImageUrl(userJSON.getString("image_url"));
                        BaseApplication.user.setSignature(userJSON.getString("signature"));
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }else{
                        //提示失败
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
     * 商品发布
     * @param bc 大类
     * @param sc 小类
     * @param name 名字
     * @param title 标题
     * @param publish_tel 发布的电话
     * @param money 商品价格
     * @param longitude 经度
     * @param latitude 纬度大类
    地址			POST名adress
     * @param detail 详情
     * @param imagePath 图片路径
     */
    public static void publishShopping(final Context context, int bc, int sc, String name, String title, String publish_tel, String money, double longitude, double latitude, String detail, final String imagePath,String adress) {
        Map<String,String> params=new HashMap<String,String>();
        params.put("bc",String.valueOf(bc));
        params.put("sc",String.valueOf(sc));
        params.put("name",name);
        params.put("title",title);
        params.put("publish_tel",publish_tel);
        params.put("money",money);
//        params.put("adress_e",String.valueOf(longitude));
        params.put("adress_e","700");
        params.put("adress_w","700");
//        params.put("adress_w",String.valueOf(latitude));
        params.put("detail",detail);
        params.put("image",imagePath);
        params.put("adress",adress);

        params.put("request","300");//请求
        params.put("tel",BaseApplication.user.getTel());//TODO 用户电话
        Log.e("userid",BaseApplication.user.getUserId());
//        params.put("userid",BaseApplication.user.getUserId());//userid
        params.put("userid","43");//userid

        RequestUtil.post(URLUtil.PUBLISH_SHOPPING, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Log.d("publishShopping--",param);
                try {
                    JSONObject jsonObject=new JSONObject(param);
                    int resultCode=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    if(resultCode==103){//返回成功
                    }else{
                    }
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context,param,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(String error) {
                //上传失败
                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //提交建议
    public static void commitSuggest(final Context context, String url, String content){

        HashMap<String,String>map=new HashMap<>();
        map.put("tel", BaseApplication.user.getTel());
        map.put("content",content);
        RequestUtil.post(url, map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Map<String,Object> result= JsonUtil.jsonToObject(param);
                String code=result.get("code")+"";
                String message=result.get("message")+"";
                if("400".equals(code)){
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"提交失败！",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error(String error) {
                Toast.makeText(context,"提交失败！",Toast.LENGTH_LONG).show();
            }
        });
    }

    //更换客服
    public static void changeService(final Context context, final String request, String userid){
        HashMap<String,String>map=new HashMap<>();
        if(request!=null){
            map.put("request",request);
            map.put("userid",userid);
        }

        RequestUtil.post(URLUtil.CONVERSATION_SERVICE, map, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                if(!"205".equals(request)&&!"206".equals(request)){
                    Map<String,Object> result= JsonUtil.jsonToObject(param);
                    String code=result.get("code")+"";
                    if("400".equals(code)){
                        ((Activity)context).finish();
                        Map<String,Object> data= JsonUtil.jsonToObject(result.get("data")+"");
                        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                        RongIM.getInstance().startPrivateChat(context,data.get("userid")+"",data.get("userid")+"");
                    }
                }
            }

            @Override
            public void error(String error) {

            }
        });
    }

}

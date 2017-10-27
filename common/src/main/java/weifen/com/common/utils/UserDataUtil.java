package weifen.com.common.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weifen.com.common.app.BaseApplication;

/**
 * 对后台返回的用户json数据解析,保存在BaseApplication中，方便调用
 * Created by zhurencong on 2017/10/13.
 */
public class UserDataUtil{
    public static void dealUserData(String data){

        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray("["+data+"]");
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            BaseApplication.user.setUserId(jsonObject.getString("user_id"));
            BaseApplication.user.setImageUrl(jsonObject.getString("image_url"));
            BaseApplication.user.setNickName(jsonObject.getString("name"));
            BaseApplication.user.setIsProve(jsonObject.getString("is_prove"));
            BaseApplication.user.setGrade(jsonObject.getString("grade"));
            BaseApplication.user.setMoney(jsonObject.getString("money"));
            BaseApplication.user.setSignature(jsonObject.getString("signature"));
            BaseApplication.user.setTel(jsonObject.getString("tel"));
            BaseApplication.user.setTransactionNums(jsonObject.getString("transaction_nums"));
            SPUtil.saveUserId("sessionId",jsonObject.getString("sessionid"));
            SPUtil.saveUserId("userId",jsonObject.getString("tel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

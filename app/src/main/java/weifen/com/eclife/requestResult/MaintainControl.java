package weifen.com.eclife.requestResult;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weifen.com.eclife.activity.IndexActivity;
import weifen.com.eclife.activity.MaintainActivity;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.eclife.fragment.NeighbourTabFragment;
import weifen.com.request.utils.JsonUtil;
import weifen.com.request.utils.RequestUtil;

/**
 * 封装附近维修的控制类
 */
public class MaintainControl {
    Context context;
    public MaintainControl(Context context){
        this.context=context;
    }

    /**
     * 加载维修列表
     * @param smallId
     */
    public void onLoadMaintainList(int smallId) {
        Map<String,String> params=new HashMap<String,String>();
        params.put("sc",smallId+"");
        params.put("adress_e", MyApplication.longitude+"");//经度
        params.put("adress_w", MyApplication.latitude+"");//纬度
        params.put("request","301");//纬度
//        params.put("sort","money");
        RequestUtil.post(URLUtil.MAINTAIN_LIST, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Map<String,Object> result= JsonUtil.jsonToObject(param);
                int resultCode= (int) result.get("code");
                String msg= (String) result.get("message");
                if(resultCode==400){
                    JSONArray data= (JSONArray) result.get("data");
                    List<Map<String,Object>> datas = JsonUtil.parseArray(data.toString(),Map.class);
                    MaintainActivity maintainActivity= (MaintainActivity) context;
                    maintainActivity.onRenderLoadMaintainList(datas);
                }else{//请求失败

                }
            }
            @Override
            public void error(String error) {
                //请求失败
            }
        });
    }
    /**
     * 查询排序后的维修列表
     * @param smallId
     */
    public void onLoadSortMaintainList(int smallId,String sortType) {
        Map<String,String> params=new HashMap<String,String>();
        params.put("sc",smallId+"");
        params.put("adress_e", MyApplication.longitude+"");//经度
        params.put("adress_w", MyApplication.latitude+"");//纬度
        params.put("request","302");//请求
        params.put("sort",sortType);//排序的类型
        RequestUtil.post(URLUtil.MAINTAIN_LIST, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                Map<String,Object> result= JsonUtil.jsonToObject(param);
                int resultCode= (int) result.get("code");
                String msg= (String) result.get("message");
                if(resultCode==400){
                    JSONArray data= (JSONArray) result.get("data");
                    List<Map<String,Object>> datas = JsonUtil.parseArray(data.toString(),Map.class);
                    MaintainActivity maintainActivity= (MaintainActivity) context;
                    maintainActivity.onRenderLoadMaintainList(datas);
                }else{//请求失败

                }
            }
            @Override
            public void error(String error) {
                //请求失败
            }
        });
    }


    /**
     * 附近加载列表请求数据
     * @param fragment  传回是哪个fragment
     * @param bigType 大类
     * @param smallType 小类
     */
    public void onLoadNeighbourList(final Fragment fragment, int bigType, int smallType) {
        Map<String,String> params=new HashMap<String,String>();
        params.put("adress_e", MyApplication.longitude+"");//经度
        params.put("adress_w", MyApplication.latitude+"");//纬度
        params.put("bc",bigType+"");
        if(smallType!=0)params.put("sc",smallType+"");//等于0时，先加载全部
        params.put("request","301");
        RequestUtil.post(URLUtil.NEIGHBOUR_LIST, params, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void success(String param) {
                try {
                    NeighbourTabFragment neighbourTabFragment= (NeighbourTabFragment) fragment;
                    JSONObject jsonObject=new JSONObject(param);
                    int resultCode=jsonObject.getInt("code");
                    String resultMsg=jsonObject.getString("message");
                    if(resultCode==7){//返回数据成功
                        List<Map<String,Object>> dataArray=JsonUtil.parseArray(jsonObject.getString("data"),Map.class);
                        neighbourTabFragment.onRenderNeighbourList(dataArray);
                        neighbourTabFragment.tipMessage(true,resultMsg);
                    }else{
                        //提示出现错误
                        neighbourTabFragment.tipMessage(false,resultMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("onLoadNeighbourList--",param);
            }

            @Override
            public void error(String error) {

            }
        });
    }
}

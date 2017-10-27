package weifen.com.request.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/3.
 */
public class RequestUtil {

    /**
     * 发送post请求
     * @param URL
     * @param param  请求参数
     * @param header 请求头
     */
    public static void post(String URL, Map<String,String> param, Map<String,String> header, final boolean isXML, final MyCallBack myCallBack){
        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder().url(URL);


        if(header!=null && !header.isEmpty() ){
            Set<Map.Entry<String,String>> entries = header.entrySet();
            for(Map.Entry<String,String> entry : entries){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }

        FormBody.Builder requestBuilder = new FormBody.Builder();
        if(param!=null && !param.isEmpty() ){
            Set<Map.Entry<String,String>> entries = param.entrySet();
            for(Map.Entry<String,String> entry : entries){
                requestBuilder.add(entry.getKey(),entry.getValue());
            }
        }
        Request request = builder.post(requestBuilder.build()).build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                if(myCallBack!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.error("请求失败");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("ME", Thread.currentThread().getName());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(TextUtils.isEmpty(result)){
                            myCallBack.error("请求失败");
                        }else{
                            if(!isXML){
                                //JSON
                                if(myCallBack!=null){
                                    myCallBack.success(result);
                                }
                            }else{
                                //xml
                            }
                        }
                    }
                });
            }
        });
    }

    public static void uploadFile(String URL, Map<String,Object> params, Map<String,String> headers, final MyCallBack myCallBack){

        OkHttpClient okHttpClient = new OkHttpClient();
        //构造请求(Builder)

        Request.Builder builder = new Request.Builder().url(URL);

        //设置请求头
        if(headers!=null && !headers.isEmpty() ){
            Set<Map.Entry<String,String>> entries = headers.entrySet();
            for(Map.Entry<String,String> entry : entries){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        //设置请求参数'
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MediaType.parse("multipart/form-data"));
        //TODO
        if(params!=null && !params.isEmpty() ){
            Set<Map.Entry<String,Object>> entries = params.entrySet();
            for(Map.Entry<String,Object> entry : entries){
                if(entry.getValue() instanceof File){//文件
                    File file = (File) entry.getValue();
                    multipartBuilder.addFormDataPart(entry.getKey(),file.getName(),MultipartBody.create(MediaType.parse("application/octet-stream"),file));
                }else{
                    multipartBuilder.addFormDataPart(entry.getKey(),entry.getValue()+"");
                }
            }
        }
        builder.post(multipartBuilder.build());

        //发送请求
        Request request = builder.build();


        //回调

        okHttpClient.newCall(request).enqueue(new Callback() {

            Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                if(myCallBack!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.error("请求失败");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("ME",Thread.currentThread().getName());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(TextUtils.isEmpty(result)){
                            myCallBack.error("请求失败");
                        }else{
                            if(myCallBack!=null){
                                myCallBack.success(result);
                            }
                        }
                    }
                });
            }
        });

    }

    public interface MyCallBack{
        void success(String param);
        void error(String error);
    }
}

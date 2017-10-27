package weifen.com.request.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * JsonUtil工具类
 */
public class JsonUtil {

    /**
     * 解析json数据返回List集合
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String jsonStr, Class clazz) {
        try {
            return JSON.parseArray(jsonStr,clazz);
        }catch (Exception e){
            return null;
        }
    }



    /**
     * 泛型反序列化
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String jsonStr){
        try {
            return JSON.parseObject(jsonStr, new TypeReference<T>(){});
        }catch (Exception e){
            Log.e("My",e.getMessage());
            //TODO 记录日志
            return null;
        }
    }
}

package weifen.com.common.utils;

/**
 * Created by zhurencong on 2017/10/28.
 */
public class DistanceUtil {

    public static String getDistance(String distance){
        if(Integer.parseInt(distance)<1000){
            return Integer.parseInt(distance)+"米";
        }else {
            return (int)((Integer.parseInt(distance))/1000)+"公里";
        }
    }
}

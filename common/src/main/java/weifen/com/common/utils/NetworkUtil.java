package weifen.com.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 判断网络状态
 * Created by Administrator on 2017/3/4.
 */
public class NetworkUtil {

    //判断有没有网路
    public static boolean isActivited(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Toast.makeText(context,networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        boolean isAvailable =  networkInfo.isAvailable();
        if(isAvailable){
            return true;
        }else{
            return false;
        }
    }
}

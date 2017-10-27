package weifen.com.common.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/18.
 */
public class DateFormatUtils {
    public static String getDate(long date,String format){
        if(TextUtils.isEmpty(format)){
            format="yyyy-MM-dd hh:mm:ss";
        }
        return new SimpleDateFormat(format).format(date).toString();
    }

    public static String parseDate(String dateString,String newFormat){
        if(TextUtils.isEmpty(dateString)){return null;}
        Date date=null;
        try {
            date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(newFormat).format(date);
    }
}

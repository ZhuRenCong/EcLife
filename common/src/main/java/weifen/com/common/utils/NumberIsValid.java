package weifen.com.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhurencong on 2017/10/15.
 */
public class NumberIsValid {
    public static boolean verifyPhoneNum(String phone) {
        String pattern = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}

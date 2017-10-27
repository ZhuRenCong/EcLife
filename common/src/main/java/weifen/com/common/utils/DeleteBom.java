package weifen.com.common.utils;

/**
 * Created by zhurencong on 2017/10/22.
 */
public class DeleteBom {

    //除去后台返回的数据中的bom
    public static String JSONTokener(String str_json) {
        // consume an optional byte order mark (BOM) if it exists
        if (str_json != null && str_json.startsWith("\uFEFF")) {
            str_json = str_json.substring(1);
        }
        return str_json;
    }
}

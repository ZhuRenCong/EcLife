package weifen.com.eclife.domain;

import java.net.URL;

/**
 * Created by Administrator on 2017/10/17.
 */
public class URLUtil {

    public static String URL_PATH="http://60.205.204.56";

    public static String QUERY_USER_INFO=URL_PATH+"/information.php";//查询个人信息

    public static String USER_IMAGE_URL=URL_PATH+"/Uploads/headimg";//获取文件
    public static String USER_INFORMATION=URL_PATH+"/information.php";//查询个人信息
    public static String UPDATE_USER_INFO=URL_PATH+"/update.php";//修改个人信息

    public static String UPLOAD_USER_PHOTO=URL_PATH+"/headimg.php";
    public static String PUBLISH_SHOPPING=URL_PATH+"/release.php";

    public static String MAINTAIN_LIST=URL_PATH+"/OSRepair.php";//维修查询
    public static String NEIGHBOUR_LIST=URL_PATH+"/products.php";//附近查询

    public static String CONTACT_US_SUGGEST= URL_PATH+"/suggest.php";//建议

    public static String CONVERSATION_SERVICE= URL_PATH+"/service.php";//建议

}

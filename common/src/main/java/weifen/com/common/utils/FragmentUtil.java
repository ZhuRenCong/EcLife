package weifen.com.common.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by Administrator on 2017/2/27.
 */
public class FragmentUtil {

    /**
     * 添加framgent
     * @param fragmentManager
     * @param contentLayoutId
     * @param content
     * @param tag
     */
    public static void showFrament(FragmentManager fragmentManager, int contentLayoutId, Fragment content, String tag){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment temp = fragmentManager.findFragmentByTag(tag);
        if(temp!=null){
            fragmentTransaction.show(temp);
        }else{
            fragmentTransaction.add(contentLayoutId,content,tag);
        }
        fragmentTransaction.commit();
    }

    /**
     * 隐藏Fragment
     * @param fragmentManager
     * @param content
     */
    public static void hideFrament(FragmentManager fragmentManager, Fragment content){
        if(content==null){
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(content);
        fragmentTransaction.commit();
    }
}

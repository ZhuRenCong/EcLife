package weifen.com.common.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhurencong on 2017/7/31.
 * 一键退出app
 */
public class ActivityCollector {

    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishActivity(Activity activity){
        if(!activity.isFinishing()){
            activity.finish();
        }
    }

    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

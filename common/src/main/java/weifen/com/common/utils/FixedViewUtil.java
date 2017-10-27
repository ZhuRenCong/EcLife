package weifen.com.common.utils;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 测量view的工具类
 */
public class FixedViewUtil {
    //测量listView的高度
    public static void resetListViewHeight(ListView listView){
        //测量所有itemview的总体高度

        int totalHeight=0;
        Adapter adapter=listView.getAdapter();
        if(adapter==null){
            return;
        }
        for(int i=0;i<adapter.getCount();i++){
            View itemView=adapter.getView(i,null,listView);
            itemView.measure(0,0);
            totalHeight+=itemView.getMeasuredHeight();
        }
        //添加listView的分割线高度
        ViewGroup.LayoutParams layoutParams=listView.getLayoutParams();
        layoutParams.height=0;
        layoutParams.height+=listView.getDividerHeight()*(adapter.getCount()-1)+totalHeight;
        listView.setLayoutParams(layoutParams);
    }

    //测量GridView的高度
    public static void resetGridViewHight(GridView gridView, int col){
        ListAdapter listAdapter=gridView.getAdapter();
        if(listAdapter==null){
            return;
        }
        int count=listAdapter.getCount();
        int totalHeight=0;
        for(int i=0;i<count;i+=col){//循环计算有多少行
            View itemView=listAdapter.getView(i,null,gridView);
            itemView.measure(0,0);
            totalHeight+=itemView.getMeasuredHeight();//获取每一项的高度
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                totalHeight+=gridView.getVerticalSpacing();//获取垂直间距
            }
        }
        ViewGroup.LayoutParams layoutParams=gridView.getLayoutParams();//重新设置高度
        layoutParams.height=totalHeight+100;
        gridView.setLayoutParams(layoutParams);


    }
}

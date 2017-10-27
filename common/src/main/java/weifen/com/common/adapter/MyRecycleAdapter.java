package weifen.com.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/11.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyHolder> {

    MyHolderCallback myHolderCallback;

    int size;

    Context context;
    List<Map<String,Object>> datas;
    int itemResId;  //item的布局文件

    String []from;
    int []to;

    public MyRecycleAdapter(Context context, List<Map<String, Object>> datas, int itemResId, String[] from, int[] to, MyHolderCallback myHolderCallback) {
        this.context = context;
        this.datas = datas;
        this.itemResId = itemResId;
        this.from = from;
        this.to = to;
        this.myHolderCallback = myHolderCallback;
        size = from == null ? 0 :from.length;
    }

    /**
     * 返回Hoder【ItemView对象】
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //xml -- View
        View itemView = LayoutInflater.from(context).inflate(itemResId,parent,false);
        if(myHolderCallback!=null){
            myHolderCallback.init(itemView);
        }

        MyHolder myHolder = new MyHolder(itemView);
        for(int tempTo:to){
            myHolder.tempView.put(tempTo,itemView.findViewById(tempTo));
        }
        return myHolder;
    }

    /**
     * itemView和数据绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Map<String,Object> itemData = datas.get(position);

        if(myHolderCallback!=null){
            myHolderCallback.renderData(position,holder.itemView,itemData);
        }

        //itemData在指定控件渲染
        for(int i=0;i<size;i++){
            Object value = itemData.get(from[i]);
            View view =holder.tempView.get(to[i]);
            //绑定数据
            bindData(view,value);
        }
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    /**
     * 为指定控件绑定数据
     * @param view
     * @param value
     */
    private void bindData(View view,Object value){
        if(view instanceof TextView){
            if(value instanceof Integer){
                ((TextView) view).setText((Integer)value);
            }else{
                ((TextView) view).setText(value+"");
            }
        }else if(view instanceof ImageView){
            if(value instanceof Integer){
                ((ImageView) view).setImageResource((Integer) value);
            }else{
                //网络地址 TODO
            }
        }
    }


    /**
     *
     */
    public static class MyHolder extends RecyclerView.ViewHolder {
        //ItemView 中需要渲染数据的View
        private Map<Integer,View> tempView  =  new HashMap<Integer, View>();
        public MyHolder(View itemView) {
            super(itemView);
        }
    }


    public interface MyHolderCallback{
        /**
         * 每一个ItemView渲染数据回调
         * @param currentData  当前渲染的数据
         * @param itemView     当前渲染的ItemView
         */
        public void renderData(int position, View itemView, Map<String, Object> currentData);

        /**
         * MyHoder初始化回调
         * @param itemView
         */
        public void init(View itemView);
    }
}

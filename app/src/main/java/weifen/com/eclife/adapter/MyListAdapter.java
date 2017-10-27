package weifen.com.eclife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weifen.com.eclife.domain.URLUtil;

/**
 * Created by Administrator on 2017/6/29.
 */
public class MyListAdapter extends BaseAdapter{

    Context context;
    List<Map<String,Object>> datas;
    int ResId;
    String[] from;
    int[] to;
    int size;
    MyListCallBack myListCallBack;

    public MyListAdapter(Context context, List<Map<String,Object>> datas, int ResId, String[] from, int[] to, MyListCallBack myListCallBack){
        this.context=context;
        this.datas=datas;
        this.ResId=ResId;
        this.from=from;
        this.to=to;
        this.size=from==null?0:from.length;
        this.myListCallBack=myListCallBack;
    }
    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> itemData=datas.get(position);
        Map<Integer,View> itemHolder=new HashMap<Integer,View>();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(ResId,parent,false);
            for(int i=0;i<size;i++){
                Object data=itemData.get(from[i]);
                View view=convertView.findViewById(to[i]);
                bindData(view,data);
                itemHolder.put(to[i],view);
                view.setTag(itemHolder);
            }
            if(myListCallBack!=null){
                myListCallBack.init(convertView);
            }
        }else{
            itemHolder=new HashMap<Integer, View>();
            for(int i=0;i<size;i++){
                Object data=itemData.get(from[i]);
                View view=itemHolder.get(to[i]);//如果是重用的view,则不用findViewById
                bindData(view,data);
            }
        }
        if(myListCallBack!=null){
            myListCallBack.renderView(position,convertView,itemData);//设置回调
        }
        return convertView;
    }

    private void bindData(View view,Object values){
        if(view instanceof TextView){
            if(values instanceof Integer){
                ((TextView) view).setText(values+"");
            }else{
                ((TextView) view).setText(values+"");
            }
        }else if(view instanceof ImageView){//如果是图片资源
            if(values instanceof Integer){
                ((ImageView) view).setImageResource((Integer) values);
            }else if(values instanceof String){//如果是网络路径
                Picasso.with(context).load(URLUtil.URL_PATH+values).into((ImageView) view);
            }
        }
    }

    public interface MyListCallBack{
        void renderView(int position, View view, Map<String, Object> data);
        void init(View view);
    }
}

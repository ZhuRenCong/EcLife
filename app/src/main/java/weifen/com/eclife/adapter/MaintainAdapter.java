package weifen.com.eclife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import weifen.com.common.utils.DateFormatUtils;
import weifen.com.eclife.R;
import weifen.com.eclife.domain.URLUtil;

/**
 * Created by Administrator on 2017/10/21.
 */
public class MaintainAdapter extends BaseAdapter{
    Context context;
    List<Map<String,Object>> datas;
    ChooseAddressCallBack chooseAddressCallBack;
    public MaintainAdapter(Context context, List<Map<String,Object>> datas, ChooseAddressCallBack chooseAddressCallBack){
        this.context=context;
        this.datas=datas;
        this.chooseAddressCallBack=chooseAddressCallBack;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Map<String,Object> itemDatas=datas.get(position);
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_category,null,false);
        }
        ViewHolder viewHolder= ViewHolder.getInstance(convertView);
        //"title","content","publish_time","400","money"
//        viewHolder.titleTV.setText(itemDatas.get("title")+"");
        if(itemDatas.get("distance") instanceof BigDecimal){
            BigDecimal bigDecimal= (BigDecimal)itemDatas.get("distance") ;//将距离转成int
            viewHolder.distanceTV.setText(bigDecimal.intValue()+"m");
        }else if(itemDatas.get("distance") instanceof Integer){
            viewHolder.distanceTV.setText(itemDatas.get("distance")+"m");
        }
        viewHolder.contentTV.setText(itemDatas.get("title")+"");
        viewHolder.updateTimeTV.setText(DateFormatUtils.parseDate(itemDatas.get("publish_time")+"","yyyy.MM.dd"));
        viewHolder.priceTV.setText("￥"+itemDatas.get("money")+"");


//                "image_url1": "[\"./Uploads/d24f06b733aab059b014e6d2e64d47a512.png",
//                "image_url2": "./Uploads/d24f06b733aab059b014e6d2e64d47a513.png\"]",
//                "publish_tel": "10086",
//                "small_id": "7",
//                "tel": "13640387514",
//                "publish_time": "2017-10-23 20:32:40",
//                "sale_nums": "0",
//                "content": "测试的详细秒速",
//                "adress": "增城市",
//                "adress_w": "23.240987",
//                "adress_e": "113.868315",
//                "distance": 12651.780169921
        String imagePath=itemDatas.get("image_url1").toString();
        if(imagePath.startsWith("[\"./")){
            imagePath= URLUtil.URL_PATH+imagePath.substring(imagePath.indexOf("/")).trim();
        }
        Picasso.with(context).load(imagePath).error(R.mipmap.person_iocn).into(viewHolder.icon);
        if(chooseAddressCallBack!=null){
            chooseAddressCallBack.renderData(position,convertView,itemDatas);
        }
        return convertView;
    }


    static class ViewHolder{
        TextView contentTV,updateTimeTV,distanceTV,priceTV;
        ImageView icon;
        public ViewHolder(View convertView){
            icon= (ImageView) convertView.findViewById(R.id.category_icon);
            contentTV= (TextView) convertView.findViewById(R.id.tv_category_info);
            updateTimeTV= (TextView) convertView.findViewById(R.id.tv_category_update_time);
            distanceTV= (TextView) convertView.findViewById(R.id.tv_category_distance);
            priceTV= (TextView) convertView.findViewById(R.id.tv_category_price);
        }

        public static ViewHolder getInstance(View convertView){
            ViewHolder viewHolder= (ViewHolder) convertView.getTag();
            if(viewHolder==null){
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }

    public interface ChooseAddressCallBack{
        void renderData(int position, View itemView, Map<String, Object> itemData);
    }
}

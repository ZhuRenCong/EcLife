package weifen.com.eclife.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import weifen.com.common.base.BaseFragment;
import weifen.com.common.commonAdapter.ListViewAdapter;
import weifen.com.common.commonAdapter.ViewHolder;
import weifen.com.common.db.User;
import weifen.com.common.utils.DeleteBom;
import weifen.com.common.utils.FixedViewUtil;
import weifen.com.common.utils.SPUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.activity.ConversationListActivity;
import weifen.com.eclife.activity.SelectPublishSortActivity;
import weifen.com.eclife.activity.SettingActivity;
import weifen.com.eclife.activity.ShopActivity;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.db.ShoppingCom;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.request.utils.JsonUtil;
import weifen.com.request.utils.RequestUtil;

/**
 * 我的界面
 * Created by Administrator on 2017/2/27.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView message,publish,nick,motto,publishNumber,orderNumber,gradeNumber;
    private ImageView setting,headImage,backgroundImage;
    private LinearLayout managerSalary,myCommunity,infoCertification,hasData;
    private ListView listView;
    private static final String TAG = "MineFragment";
    private CircleImageView image;
    private ScrollView scrollView;
    private ListViewAdapter<ShoppingCom>adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view) {
        initView(view);

        initListener();

        getMyShopping();
        //刷新商品
        swipeRefresh.setColorSchemeResources(R.color.red);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyShopping();
                swipeRefresh.setRefreshing(false);
            }
        });

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if(i==0)
//                    swipeRefresh.setRefreshing(true);
//                else
//                    swipeRefresh.setRefreshing(false);
//            }
//        });
//
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                swipeRefresh.setEnabled(scrollView.getScrollY()==0);
//            }
//        });
    }

    private void getMyShopping(){
        if(!"".equals(SPUtil.getUserId("sessionId"))&& !"".equals(MyApplication.user.getTel())){
            HashMap<String,String> map=new HashMap<>();
            map.put("tel","1234567890");
            RequestUtil.post("http://60.205.204.56/myproduct.php", map, null, false, new RequestUtil.MyCallBack() {
                @Override
                public void success(String param) {
                    Map<String,Object> result= JsonUtil.jsonToObject(param);
                    String code=result.get("code")+"";
                    if("400".equals(code)){
                        String shopClassStr = result.get("data")+"";

                        if(shopClassStr.length()==0){
                            return;
                        }

                        listView.setVisibility(View.VISIBLE);
                        hasData.setVisibility(View.GONE);
                        final String[]smallType=getResources().getStringArray(R.array.maintain);

                        final List<ShoppingCom> datas = JsonUtil.parseArray(shopClassStr,ShoppingCom.class);

                        //设置发布个数
                        publishNumber.setText(datas.size()+"");

                        adapter=new ListViewAdapter<ShoppingCom>(activity,datas,R.layout.item_category) {
                            @Override
                            public void convert(ViewHolder holder, ShoppingCom item) {
                                holder.setImageByUrl(R.id.category_icon,URLUtil.URL_PATH+"/"+(item.getImage_url1()).substring(2));
                                holder.setText(R.id.tv_category_small_type,smallType[Integer.parseInt(item.getSmall_id())]);
                                holder.setText(R.id.tv_category_info,item.getTitle());
                                holder.setText(R.id.tv_category_update_time,(item.getPublish_time()).split(" ")[0]);
                                holder.setText(R.id.tv_category_distance,"广州");
                                holder.setText(R.id.tv_category_price,"￥"+item.getMoney());
                            }
                        };
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(activity, ShopActivity.class);
                                intent.putExtra("data",datas.get(i));
                                startActivity(intent);
                            }
                        });

                    }else {
                        Log.d(TAG, "success: "+"获取我的商品失败");
                    }
                }

                @Override
                public void error(String error) {

                }
            });
        }else {
            hasData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        message= (TextView) view.findViewById(R.id.tv_message_mine);
        publish= (TextView) view.findViewById(R.id.tv_publish_mine);
        nick= (TextView) view.findViewById(R.id.nick);
        motto= (TextView) view.findViewById(R.id.motto);
        publishNumber= (TextView) view.findViewById(R.id.tv_publish_number_mine);
        orderNumber= (TextView) view.findViewById(R.id.tv_order_number_mine);
        gradeNumber= (TextView) view.findViewById(R.id.tv_grade_number_mine);
        setting= (ImageView) view.findViewById(R.id.iv_setting_mine);
        headImage= (ImageView) view.findViewById(R.id.head_image);
        managerSalary= (LinearLayout) view.findViewById(R.id.manager_salary_mine);
        myCommunity= (LinearLayout) view.findViewById(R.id.my_community_mine);
        infoCertification= (LinearLayout) view.findViewById(R.id.info_certification_mine);
        listView= (ListView) view.findViewById(R.id.list_order_mine);
        backgroundImage= (ImageView) view.findViewById(R.id.iv_background_mine);
        hasData= (LinearLayout) view.findViewById(R.id.has_data);
        image= (CircleImageView) view.findViewById(R.id.head_image);
        swipeRefresh= (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        scrollView= (ScrollView) view.findViewById(R.id.scroll_view_mine);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!"".equals(MyApplication.user.getTel()+"")){
            setUserInfo();
        }
    }

    private void initListener() {
        message.setOnClickListener(this);
        publish.setOnClickListener(this);
        managerSalary.setOnClickListener(this);
        myCommunity.setOnClickListener(this);
        infoCertification.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    public void setUserInfo(){
        gradeNumber.setText(MyApplication.user.getGrade()!="null" ?  MyApplication.user.getGrade():"0");
        orderNumber.setText(MyApplication.user.getTransactionNums()!=null ? MyApplication.user.getTransactionNums():"0");
        nick.setText((MyApplication.user.getNickName())!=null ? MyApplication.user.getNickName()+"" : "家教男神");
        motto.setText((MyApplication.user.getSignature())!=null ? MyApplication.user.getSignature()+"" : "授人以渔,不如授之以渔,对于每一个...");
        Picasso.with(activity).load(URLUtil.URL_PATH+ MyApplication.user.getImageUrl()).error(getResources().getDrawable(R.mipmap.person_iocn)).into(image);
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()){
            //消息
            case R.id.tv_message_mine:
                intent=new Intent(activity, ConversationListActivity.class);
                startActivity(intent);
                break;
            //发布
            case R.id.tv_publish_mine:
                intent=new Intent(activity, SelectPublishSortActivity.class);
                startActivity(intent);
                break;
            //设置
            case R.id.iv_setting_mine:
                intent=new Intent(activity, SettingActivity.class);
                startActivity(intent);
                break;
            //资金去管理
            case R.id.manager_salary_mine:
                break;
            //我的社区
            case R.id.my_community_mine:
                break;
            //信息认证
            case R.id.info_certification_mine:
                break;
        }
    }
}

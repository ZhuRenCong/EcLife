package weifen.com.eclife.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import weifen.com.common.adapter.MyRecycleAdapter;
import weifen.com.common.base.BaseFragment;
import weifen.com.common.utils.DateFormatUtils;
import weifen.com.common.utils.FixedViewUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.adapter.MyListAdapter;
import weifen.com.eclife.pop.BasePopupwindow;
import weifen.com.eclife.pop.SortPopupWindow;
import weifen.com.widget.GradualScrollView;

/**
 * 社区界面
 */
public class CommunityFragment extends BaseFragment implements View.OnClickListener {

    CircleImageView communityIcon;//社区头像
    ImageView isHasRegisterIV,searchCommunityIV,mineCommunityIcon,joinCommunityIcon;//显示是否获得认证|查找社区
    TextView communityName,joinDayTV;//社区名|加入的天数
    LinearLayout mineCommunityRoot, joinCommunityRoot;//我的社区|我加入的社区
    GridView mineCommunityGridView,joinCommunityGridView;//我创建的社区|加入的社区
    TextView sortTV;

    RecyclerView popularCommunityView;//热门社区
    ListView pcListView;//社区列表

    GradualScrollView gradualScroll;

    MyRecycleAdapter myRecycleAdapter;
    List<Map<String,Object>> popularCommunityDatas=new ArrayList<Map<String,Object>>();

    //作为传入适配器的key
    private static final String PC_ICON="popularCommunityIcon";
    private static final String PC_INFO="popularCommunityInfo";

    //假数据
    String[] pcInfoData=new String[]{"罗马家园小区","碧泉小区","微风科技","凤凰城"};

    List<Map<String,Object>> shoppingList=new ArrayList<Map<String,Object>>();
    MyListAdapter shoppingAdapter;

    MyListAdapter joinCommunityAdapter;
    MyListAdapter mineCommunityAdapter;
    List<Map<String,Object>> joinCommunityDatas=new ArrayList<Map<String,Object>>();
    List<Map<String,Object>> mineCommunityDatas=new ArrayList<Map<String,Object>>();
    @Override
    protected int getLayout() {
        return R.layout.fragment_community;
    }

    @Override
    protected void init(View view) {
        initView(view);
        initListener();
        initData();
        initAdapter();
    }


    private void initListener() {//初始化监听
        mineCommunityRoot.setOnClickListener(this);
        joinCommunityRoot.setOnClickListener(this);

        sortTV.setOnClickListener(this);
    }

    private void initAdapter() {
        addJoinCommunityData(4);
        addMineCommunityData(4);
        //初始化我加入的社区布局的适配器
        joinCommunityAdapter =new MyListAdapter(getActivity(), joinCommunityDatas, R.layout.item_join_community,
                new String[]{"icon","name","groupCount","message"},
                new int[]{R.id.iv_join_community_icon,
                R.id.tv_join_community_name, R.id.tv_join_group_community_count,
                        R.id.tv_join_message_community}, new MyListAdapter.MyListCallBack() {
            @Override
            public void renderView(int position, View view, Map<String, Object> data) {

            }
            @Override
            public void init(View view) {

            }
        });
        //初始化我创建的社区布局的适配器
        mineCommunityAdapter =new MyListAdapter(getActivity(), mineCommunityDatas, R.layout.item_mine_community,
                new String[]{"icon","name","groupCount"},
                new int[]{R.id.iv_mine_community_icon, R.id.tv_mine_community_name,
                R.id.tv_community_count},
                new MyListAdapter.MyListCallBack() {
            @Override
            public void renderView(int position, View view, Map<String, Object> data) {

            }

            @Override
            public void init(View view) {

            }
        });
        joinCommunityGridView.setAdapter(joinCommunityAdapter);
        mineCommunityGridView.setAdapter(mineCommunityAdapter);
        FixedViewUtil.resetGridViewHight(joinCommunityGridView,2);
        FixedViewUtil.resetGridViewHight(mineCommunityGridView,2);
    }

    private void addMineCommunityData(int size) {
        //TODO 到时候用来直接从网络上请求数据
        for(int i=0;i<size;i++){
            Map<String,Object> itemData=new HashMap<String,Object>();
            itemData.put("icon", R.mipmap.test00+i);
            itemData.put("name","万达广场"+i);
            itemData.put("groupCount",5*i);
            mineCommunityDatas.add(itemData);
        }
    }

    private void addJoinCommunityData(int size) {
        //TODO 到时候用来直接从网络上请求数据
        for(int i=0;i<size;i++){
            Map<String,Object> itemData=new HashMap<String,Object>();
            itemData.put("icon", R.mipmap.test00+i);
            itemData.put("name","万达广场"+i);
            itemData.put("groupCount",5*i);
            itemData.put("message",15*i);
            joinCommunityDatas.add(itemData);
        }
    }


    private void initData() {
        addPopularCommunityDatas(pcInfoData.length);
        myRecycleAdapter=new MyRecycleAdapter(getActivity(),popularCommunityDatas, R.layout.item_popular_community,
                new String[]{PC_ICON,PC_INFO},new int[]{R.id.iv_icon_pc, R.id.tv_info_pc},myHolderCallback);
        popularCommunityView.setAdapter(myRecycleAdapter);

        //社区列表适配器
        addCommunityListDatas(5);
        shoppingAdapter=new MyListAdapter(getActivity(), shoppingList, R.layout.item_community_list,
                new String[]{"icon", "smallIcon", "bigType", "title", "smallTitle", "time", "distance", "price"},
                new int[]{R.id.category_icon, R.id.iv_small_icon_community,
                        R.id.tv_category_small_type, R.id.tv_category_info, R.id.tv_small_title_community,
                        R.id.tv_category_update_time, R.id.tv_category_distance, R.id.tv_category_price
                }, new MyListAdapter.MyListCallBack() {
            @Override
            public void renderView(int position, View view, Map<String, Object> data) {

            }

            @Override
            public void init(View view) {

            }
        });
        pcListView.setAdapter(shoppingAdapter);
        FixedViewUtil.resetListViewHeight(pcListView);
    }

    private void initView(View view) {
        communityIcon= (CircleImageView) view.findViewById(R.id.iv_community_icon);
        communityName= (TextView) view.findViewById(R.id.tv_community_name);
        isHasRegisterIV = (ImageView) view.findViewById(R.id.iv_has_already_register);
        searchCommunityIV = (ImageView) view.findViewById(R.id.iv_search_community);
        joinDayTV= (TextView) view.findViewById(R.id.tv_join_day);
        mineCommunityRoot = (LinearLayout) view.findViewById(R.id.my_community_root);
        joinCommunityRoot = (LinearLayout) view.findViewById(R.id.my_join_root);
        mineCommunityGridView= (GridView) view.findViewById(R.id.gridView_i_create);
        joinCommunityGridView= (GridView) view.findViewById(R.id.gridView_i_join);
        mineCommunityIcon= (ImageView) mineCommunityRoot.getChildAt(0);
        joinCommunityIcon= (ImageView) joinCommunityRoot.getChildAt(0);

        popularCommunityView= (RecyclerView) view.findViewById(R.id.recycle_view_community);
        popularCommunityView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        pcListView= (ListView) view.findViewById(R.id.listView_community);

        sortTV= (TextView) view.findViewById(R.id.tv_sort);
        //TODO
//        gradualScroll= (GradualScrollView) view.findViewById(R.id.gradualScroll);
//        gradualScroll.setTopView(topView);

    }

    private void addPopularCommunityDatas(int size){
        //先传入假数据,后期再加网络请求 TODO
        for(int i=0;i<size;i++){
            Map<String,Object> itemData=new HashMap<String,Object>();
            itemData.put(PC_ICON, R.mipmap.popular_community_01+i);
            itemData.put(PC_INFO,pcInfoData[i]);
            popularCommunityDatas.add(itemData);
        }
    }

    /**
     * 加载社区列表的数据
     */
    private void addCommunityListDatas(int size){
        //shoppingList的数据
        for(int i=0;i<size;i++){
            Map<String,Object> itemData=new HashMap<String,Object>();
            itemData.put("icon", R.mipmap.category_list_01);
            itemData.put("smallIcon", R.mipmap.popular_community_03);
            itemData.put("bigType","家教");
            itemData.put("title","高中英语教学");
            itemData.put("smallTitle","万达广场");
            itemData.put("time", DateFormatUtils.getDate(System.currentTimeMillis(),"yyyy.MM.dd"));
            itemData.put("distance","距离"+(int)(Math.random()*10*i)+"m");
            itemData.put("price","￥"+(int)(Math.random()*1000)+".0");

            shoppingList.add(itemData);
        }
    }



    /**
     * 热门列表适配器的回调接口
     */
    MyRecycleAdapter.MyHolderCallback myHolderCallback=new MyRecycleAdapter.MyHolderCallback() {
        @Override
        public void renderData(int position,View itemView, final Map<String, Object> currentData) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO 点击进入社区简介
                    Toast.makeText(getActivity(),"点击了"+currentData.get(PC_INFO),Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void init(View itemView) {
        }
    };


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            //点击出现我创建的社区的布局
            case R.id.my_community_root:
                mineCommunityIcon.setImageResource(mineCommunityGridView.getVisibility()==View.VISIBLE? R.mipmap.icon_mine_community_unselect: R.mipmap.icon_mine_community_select);
                joinCommunityIcon.setImageResource(R.mipmap.icon_join_community_unselect);

                mineCommunityRoot.setBackgroundColor(getResources().getColor(mineCommunityGridView.getVisibility()==View.VISIBLE? R.color.community_tab_unSelect: R.color.community_tab_select));
                joinCommunityRoot.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));

                mineCommunityGridView.setVisibility(mineCommunityGridView.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                if(joinCommunityGridView.getVisibility()==View.VISIBLE)joinCommunityGridView.setVisibility(View.GONE);
                break;
            //点击出现我加入的社区的布局
            case R.id.my_join_root:
                mineCommunityIcon.setImageResource(R.mipmap.icon_mine_community_unselect);
                joinCommunityIcon.setImageResource(joinCommunityGridView.getVisibility()==View.VISIBLE? R.mipmap.icon_join_community_unselect: R.mipmap.icon_join_community_select);
                //则实现隐藏
                joinCommunityRoot.setBackgroundColor(getResources().getColor(joinCommunityGridView.getVisibility()==View.VISIBLE? R.color.community_tab_unSelect: R.color.community_tab_select));
                mineCommunityRoot.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                joinCommunityGridView.setVisibility(joinCommunityGridView.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                if(mineCommunityGridView.getVisibility()==View.VISIBLE)mineCommunityGridView.setVisibility(View.GONE);
                break;

            case R.id.tv_sort://排序
                SortPopupWindow sortPopupWindow=new SortPopupWindow(getActivity(), new BasePopupwindow.PopCallBack() {
                    @Override
                    public void setText(int type) {

                    }
                });
                sortPopupWindow.init();
                int[] popupWindowLoc=sortPopupWindow.calculatePopupWindow(sortTV);
                sortPopupWindow.open(sortTV, Gravity.TOP|Gravity.START,popupWindowLoc[0],popupWindowLoc[1]);
                break;
        }
    }
}

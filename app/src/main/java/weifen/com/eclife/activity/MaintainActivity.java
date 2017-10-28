package weifen.com.eclife.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weifen.com.common.base.BaseActivity;
import weifen.com.common.commonAdapter.PageAdapter;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.FixedViewUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.adapter.MaintainAdapter;
import weifen.com.eclife.pop.BasePopupwindow;
import weifen.com.eclife.pop.ScreenPopupWindow;
import weifen.com.eclife.pop.SortPopupWindow;
import weifen.com.eclife.requestResult.MaintainControl;

/**
 * Created by zhurencong on 2017/9/19.
 * 上门维修
 */
public class MaintainActivity extends BaseActivity implements View.OnClickListener, BasePopupwindow.PopCallBack {

    private ImageView back,search;
    private ViewPager viewPager;
    private CirclePageIndicator indicator;
    private TextView sortTV,allTV,screeningTV;
    private TabLayout maintainTablayout;
    private ListView listView;
    private MaintainAdapter maintainAdapter;
    private LinearLayout sortTabLayout,nullDataLayout;//排序条|空数据布局
    private static int type;//区别传入的是哪个分类
    String[] tabTitles;
    int[] tabID;
    int smallId;//点击的小类id
    int currentSortType=1;//0-排序 1-全部 2-筛选
    ScrollView scrollView;
    LinearLayout maintainHeadRoot;

    //广告图片集合
    private List<ImageView> advImages;
    List<Map<String,Object>> listDatas=new ArrayList<Map<String,Object>>();


    MaintainControl maintainConctrol;
    @Override
    protected int setLayout() {
        return R.layout.maintain_layout;
    }

    @Override
    protected void initView() {
        tabID=getResources().getIntArray(R.array.maintain_small_id);
        smallId=tabID[0];
        ActivityCollector.addActivity(this);

        findView();
        initTabLayout();
        initListener();
        initAdapter();

        advImages=new ArrayList<>();
        //网络加载数据
        int []imageResId = new int[]{R.mipmap.test00, R.mipmap.test01, R.mipmap.test02, R.mipmap.test03, R.mipmap.test04, R.mipmap.test05};

        for(int i=0;i<imageResId.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResId[i]);
            advImages.add(imageView);
        }

        viewPager.setAdapter(new PageAdapter(advImages));
        indicator.setViewPager(viewPager);
        indicator.setFillColor(getResources().getColor(R.color.divider));


        maintainConctrol=new MaintainControl(this);
        maintainConctrol.onLoadMaintainList(tabID[0]);//加载房屋维修数据

    }

    private void initTabLayout() {
        //添加tab的几个参数
        tabTitles=getResources().getStringArray(R.array.maintain);
        for(String tab:tabTitles){
            maintainTablayout.addTab(maintainTablayout.newTab().setText(tab));
        }
    }

    private void findView() {
        back= (ImageView) findViewById(R.id.back);
        search= (ImageView) findViewById(R.id.search);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        indicator= (CirclePageIndicator) findViewById(R.id.indicator);
        sortTV= (TextView) findViewById(R.id.tv_sort);
        screeningTV= (TextView) findViewById(R.id.tv_screen);
        allTV= (TextView) findViewById(R.id.tv_all_filtrate);
        listView= (ListView) findViewById(R.id.list_view);

        maintainTablayout= (TabLayout) findViewById(R.id.tab_layout_maintain);
        sortTabLayout= (LinearLayout) findViewById(R.id.sort_tab_layout);
        nullDataLayout= (LinearLayout) findViewById(R.id.has_data);

        scrollView= (ScrollView) findViewById(R.id.maintain_scrollView);
        maintainHeadRoot= (LinearLayout) findViewById(R.id.maintain_head_root);
    }

    private void initAdapter() {
        maintainAdapter=new MaintainAdapter(this, listDatas, new MaintainAdapter.ChooseAddressCallBack() {
            @Override
            public void renderData(int position, View itemView, Map<String, Object> itemData) {
                String smallid= (String) itemData.get("small_id");
                TextView titleTV= (TextView) itemView.findViewById(R.id.tv_category_small_type);
                for(int i=0;i<tabID.length;i++){
                    if(tabID[i]==Integer.valueOf(smallid)){
                        titleTV.setText(tabTitles[i]);
                        return;
                    }
                }
            }
        });
        listView.setAdapter(maintainAdapter);
    }

    private void initListener() {
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        sortTV.setOnClickListener(this);
        screeningTV.setOnClickListener(this);
        allTV.setOnClickListener(this);

        //点击排序---弹出pop或者直接
        maintainTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                smallId=tabID[tab.getPosition()];
                if(maintainConctrol!=null){
                    if(currentSortType!=1){
                        allTV.setBackgroundColor(getResources().getColor(R.color.community_tab_select));
                        sortTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                        screeningTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                        sortTV.setText("排序");
                        screeningTV.setText("筛选");
                        currentSortType=1;
                    }
                    maintainConctrol.onLoadMaintainList(smallId);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //返回
            case R.id.back:
                finish();
                break;
            //搜索
            case R.id.search:
                break;
            //排序
            case R.id.tv_sort:
                currentSortType=0;
                sortTV.setBackgroundColor(getResources().getColor(R.color.community_tab_select));
                screeningTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                allTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));

                SortPopupWindow sortPopupWindow=new SortPopupWindow(this,this);
                sortPopupWindow.init();
                sortPopupWindow.open(sortTV);
                dealPopupWindowShower(sortPopupWindow);
                break;
            //全部
            case R.id.tv_all_filtrate:
                currentSortType=1;
                allTV.setBackgroundColor(getResources().getColor(R.color.community_tab_select));
                sortTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                screeningTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                sortTV.setText("排序");
                screeningTV.setText("筛选");
                //加载当前类的全部
                maintainConctrol.onLoadMaintainList(smallId);
                break;
            //筛选
            case R.id.tv_screen:
                currentSortType=2;
                screeningTV.setBackgroundColor(getResources().getColor(R.color.community_tab_select));
                sortTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));
                allTV.setBackgroundColor(getResources().getColor(R.color.community_tab_unSelect));

                ScreenPopupWindow screenPopupWindow=new ScreenPopupWindow(this,this);
                screenPopupWindow.init();
                screenPopupWindow.open(maintainHeadRoot);//显示在最上方
                dealPopupWindowShower(screenPopupWindow);
                break;
        }
    }


    @Override
    public void setText(int type) {//popupwindow的回调
        //发送相对的请求
        switch (type){
            case 1:
                sortTV.setText("综合");
                maintainConctrol.onLoadSortMaintainList(smallId,"comprehensive");
                break;
            case 2:
                sortTV.setText("价格");
                maintainConctrol.onLoadSortMaintainList(smallId,"money");
                break;
            case 3:
                sortTV.setText("好评");
                maintainConctrol.onLoadSortMaintainList(smallId,"score");
                break;
            case 4:
                sortTV.setText("销量");
                maintainConctrol.onLoadSortMaintainList(smallId,"sale_nums");
                break;
            case 5:
                sortTV.setText("距离");
                maintainConctrol.onLoadSortMaintainList(smallId,"distance");
                break;
            case 6:
                screeningTV.setText("价格");
                break;
            case 7:
                screeningTV.setText("距离");
                break;
            case 8:
                screeningTV.setText("关键字");
                break;
        }
    }


    /**
     * 请求控制类的回调,加载列表数据
     * @param datas
     */
    public void onRenderLoadMaintainList(List<Map<String, Object>> datas) {
        listDatas.clear();
        if(datas!=null&&datas.size()>0){
            sortTabLayout.setVisibility(View.VISIBLE);
            nullDataLayout.setVisibility(View.GONE);
            listDatas.addAll(datas);
        }else{//数据为空
            sortTabLayout.setVisibility(View.GONE);
            nullDataLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this,"暂时还没有数据哦",Toast.LENGTH_SHORT).show();
        }
        FixedViewUtil.resetListViewHeight(listView);
        maintainAdapter.notifyDataSetChanged();
    }

    @Override
    public void sureSelect(int type,String min,String max,String keyword) {//点击确定,这边不会用到
        maintainConctrol.onLoadScreenMaintainList(3,smallId,type,min,max,keyword);
    }

    /**
     * 处理pop的其他部分的阴影效果
     * @param pop
     */
    private void dealPopupWindowShower(BasePopupwindow pop) {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.7f;
        getWindow().setAttributes(lp);
        pop.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}

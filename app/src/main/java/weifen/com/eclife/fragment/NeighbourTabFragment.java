package weifen.com.eclife.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import weifen.com.common.adapter.MyRecycleAdapter;
import weifen.com.common.base.BaseFragment;
import weifen.com.common.utils.DateFormatUtils;
import weifen.com.eclife.R;
import weifen.com.eclife.adapter.MaintainAdapter;
import weifen.com.eclife.adapter.MyListAdapter;
import weifen.com.eclife.domain.Constant;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.eclife.requestResult.MaintainControl;

/**
 * 附近的分类列表
 */
public class NeighbourTabFragment extends BaseFragment {
    RecyclerView smallTypeRecyclerView;
    MyRecycleAdapter smallTypeRecycleAdapter;
    ListView shoppingListView;
    MaintainAdapter shoppingListAdapter;

    List<Map<String, Object>> recycleDatas = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> listDatas = new ArrayList<Map<String, Object>>();

    String[] cleanArray, maintainArray;//清洁|维修
    int[] cleanSmallIdArray, maintainIdArray;

    private Map<Integer, LinkedHashMap<Integer, String>> IdNameMap = new HashMap<Integer, LinkedHashMap<Integer, String>>();//保存大类小类的id和name
    public static final String CATEGORY_NAME = "categoryName";

    public int bigType;//用于区别是来自于哪个点击类型
    private int LastSmallTypeSelectPosition=-1;

    MaintainControl maintainControl;//发送附近请求的控制类
    LinearLayout dataEmptyView;

    public void setType(int typeCode) {
        bigType = typeCode + 1;//+1是因为数据表从1开始
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_neighbour_tab;
    }

    @Override
    protected void init(View view) {
        maintainControl = new MaintainControl(getActivity());
        initView(view);
        initIdNameMap();
        initAdapter();
        initData();
    }

    private void initIdNameMap() {//先初始化维修
        IdNameMap.put(Constant.GUIDE, initSmallNameIdMap(new String[]{}, new int[]{}));
        IdNameMap.put(Constant.ALL, initSmallNameIdMap(new String[]{}, new int[]{}));

        cleanArray=getResources().getStringArray(R.array.clean);
        cleanSmallIdArray=getResources().getIntArray(R.array.clean_small_id);
        IdNameMap.put(Constant.HOMETEACH,initSmallNameIdMap(cleanArray,cleanSmallIdArray));

        maintainArray = getResources().getStringArray(R.array.maintain);
        maintainIdArray = getResources().getIntArray(R.array.maintain_small_id);
        IdNameMap.put(Constant.MAINTAIN, initSmallNameIdMap(maintainArray, maintainIdArray));
    }

    private LinkedHashMap<Integer, String> initSmallNameIdMap(String[] nameArray, int[] idArray) {
        if (nameArray.length == 0) return null;
        LinkedHashMap<Integer, String> temp = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < nameArray.length; i++) {//将对应的id和name一一对应
            temp.put(idArray[i], nameArray[i]);
        }
        return temp;
    }


    private void initData() {
        fillRecycleData(IdNameMap.get(bigType));//维修
        maintainControl.onLoadNeighbourList(this,bigType,0);//一开始加载全部数据
    }

    private void fillRecycleData(Map<Integer, String> smallNameIdMap) {
        recycleDatas.clear();
        if (smallNameIdMap == null) {
            smallTypeRecyclerView.setVisibility(View.GONE);//隐藏布局
        } else {
            Collection<String> values = smallNameIdMap.values();
            for (String value : values) {
                Map<String, Object> itemData = new HashMap<String, Object>();
                itemData.put(CATEGORY_NAME, value);
                recycleDatas.add(itemData);
            }
            smallTypeRecycleAdapter.notifyDataSetChanged();
        }
    }

    private void initAdapter() {
        smallTypeRecycleAdapter = new MyRecycleAdapter(getActivity(), recycleDatas, R.layout.item_neighnour_category,
                new String[]{CATEGORY_NAME}, new int[]{R.id.tv_name_neighbour}, new MyRecycleAdapter.MyHolderCallback() {
            @Override
            public void renderData(final int position, View itemView, final Map<String, Object> currentData) {
                //TODO 设置点击事件
                final TextView nameTV = (TextView) itemView.findViewById(R.id.tv_name_neighbour);
                nameTV.setSelected(LastSmallTypeSelectPosition == position);
                nameTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (LastSmallTypeSelectPosition != position) {
                            LastSmallTypeSelectPosition = position;
                            //点击加载对应的类
                            loadSmallTypeListByCategory_name((String) currentData.get(CATEGORY_NAME));
                        }else{//如果再次点击则加载默认
                            LastSmallTypeSelectPosition = -1;
                            maintainControl.onLoadNeighbourList(NeighbourTabFragment.this,bigType,0);//一开始加载全部数据
                        }
                        smallTypeRecycleAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void init(View itemView) {

            }
        });
        smallTypeRecyclerView.setAdapter(smallTypeRecycleAdapter);


        shoppingListAdapter=new MaintainAdapter(activity, listDatas, new MaintainAdapter.ChooseAddressCallBack() {
            @Override
            public void renderData(int position, View itemView, Map<String, Object> itemData) {
                //设置小标题是那一个小类
                TextView smallTypeTV = (TextView) itemView.findViewById(R.id.tv_category_small_type);
                String smallTypeName=IdNameMap.get(bigType).get(Integer.parseInt((String) itemData.get("small_id")));
                smallTypeTV.setText(smallTypeName);
            }
        });
        shoppingListView.setAdapter(shoppingListAdapter);
    }


    private void initView(View view) {
        smallTypeRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_tab_neighbour);
        smallTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        shoppingListView = (ListView) view.findViewById(R.id.list_tab_neighbour);

        dataEmptyView= (LinearLayout) view.findViewById(R.id.has_data);
    }

    /**
     *  获取点击的小类的名字进行比较，获得小类的id,发送请求更新数据
     * @param categoryName
     */
    private void loadSmallTypeListByCategory_name(String categoryName) {
        Set<Map.Entry<Integer,String>> entries=IdNameMap.get(bigType).entrySet();
        for(Map.Entry<Integer,String> entry:entries){
            if(entry.getValue()==categoryName){
                //加载数据
                maintainControl.onLoadNeighbourList(NeighbourTabFragment.this,bigType,entry.getKey());
                break;
            }
        }
    }

    /**
     * 从网络加载数据
     * @param dataArray
     */
    public void onRenderNeighbourList(List<Map<String, Object>> dataArray) {
        if(IdNameMap.get(bigType)==null){//判断是否有初始化好id和name
            dataEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        if(dataArray!=null&&dataArray.size()>1){//有数据
            dataEmptyView.setVisibility(View.GONE);
            shoppingListView.setVisibility(View.VISIBLE);
            listDatas.clear();
            int size=dataArray.size();
            Map<String, Object> smallTypes=dataArray.get(0);//TODO 返所有的小类的名称

            for(int i=1;i<size;i++){//保存所有的商品数据
                listDatas.add(dataArray.get(i));
            }
            shoppingListAdapter.notifyDataSetChanged();
        }else{//TODO 显示listview为空
            shoppingListView.setVisibility(View.GONE);
            dataEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示提示信息
     * @param isSuccess
     * @param resultMsg
     */
    public void tipMessage(boolean isSuccess, String resultMsg) {
        if(!isSuccess){//查询失败在提示
            Toast.makeText(activity,resultMsg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LastSmallTypeSelectPosition=-1;//界面被暂停时，设置会默认
    }
}

package weifen.com.eclife.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weifen.com.common.adapter.RecycleViewAdapter;
import weifen.com.common.adapter.ViewHolder;
import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.FixedViewUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.bean.Address;
import weifen.com.eclife.bean.AddressAdapter;
import weifen.com.widget.QuickIndexBar;

/**
 * Created by zhurencong on 2017/9/19.
 */
public class SelectAddressActivity extends BaseActivity implements View.OnClickListener {

    private String[] hotCity={"北京","广州","上海","南京","杭州","武汉","成都","天津","深圳","香港特别行政区","重庆","长沙"};
    private GridView gridView;

    private static final int SCAN_CODE_PERMISSION = 0x1110;

    private List<String>dataCity;
    private ListView addressListView;
    private TextView currentWord;
    QuickIndexBar quickIndexBar;//快速索引栏
    ScrollView scrollView;

    private ImageView back;
    TextView relocationCity,relocation;

    //地图实例化
    public LocationClient mLocationClient;
    public LocationClientOption option;


    private ArrayList<Address> address = new ArrayList<Address>();
    @Override
    protected int setLayout() {
        return R.layout.select_address;
    }

    @Override
    protected void initView() {

        ActivityCollector.addActivity(this);

        back= (ImageView) findViewById(R.id.back);
        relocationCity= (TextView) findViewById(R.id.relocation_city);
        relocation= (TextView) findViewById(R.id.relocation);

        addressListView= (ListView) findViewById(R.id.list_view_address);
        quickIndexBar= (QuickIndexBar) findViewById(R.id.quick_index_address);
        currentWord = (TextView) findViewById(R.id.currentWord);

        scrollView= (ScrollView) findViewById(R.id.scroll_view);

        gridView= (GridView) findViewById(R.id.grid_view);

        dataCity=new ArrayList<>();
        int length=hotCity.length;

        for(int i=0;i<length;i++){
            dataCity.add(hotCity[i]);
        }

        gridView.setAdapter(new RecycleViewAdapter<String>(this,dataCity,R.layout.hot_city_item) {
            @Override
            public void convert(ViewHolder holder, final String item) {
                holder.setText(R.id.city,item);
                holder.getView(R.id.city).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.location=item;
                        finish();
                    }
                });
            }
        });

        //1.准备数据
        fillList();
        //2.对数据进行排序
        Collections.sort(address);
        //3.设置Adapter
        addressListView.setAdapter(new AddressAdapter(this, address));
        initListener();
        //测量listView的高度
        FixedViewUtil.resetListViewHeight(addressListView);
        FixedViewUtil.resetGridViewHight(gridView,3);

        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyApplication.location=((TextView)view.findViewById(R.id.name)).getText()+"";
                finish();
            }
        });

        //通过缩小currentWord来隐藏
        ViewHelper.setScaleX(currentWord, 0);
        ViewHelper.setScaleY(currentWord, 0);

        scrollView.setSmoothScrollingEnabled(false);

        back.setOnClickListener(this);
        relocation.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        relocationCity.setText(MyApplication.location);
        super.onResume();
    }

    //请求位置
    private void requestLocation(){
        //重新定位初始化和设置参数
        mLocationClient=new LocationClient(this);
        mLocationClient.registerLocationListener(new MyLocationListener());
        option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        if(!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SCAN_CODE_PERMISSION:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"定位失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发送未知错误",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition=new StringBuilder();
//            currentPosition.append("纬度:").append(bdLocation.getLatitude()).append("\n");
//            currentPosition.append("经度:").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("定位方式:");
//            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
//                currentPosition.append("GPS");
//            }else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
//                currentPosition.append("网络");
//            }

            currentPosition.append(bdLocation.getDistrict());
            MyApplication.location=currentPosition.toString();
            relocationCity.setText(MyApplication.location);
            mLocationClient.unRegisterLocationListener(this);
        }
    }

    private void initListener() {
        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //获取当前点击的字母
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                for (int i = 0; i < address.size(); i++) {
                    String firstWord = address.get(i).getPinyin().charAt(0)+"";
                    if(letter.equals(firstWord)){
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        scrollView.scrollTo(0,getListSelectY(i));//用scrollView的方法去设置滚动的位置
                        break;//只需要找到第一个就行
                    }
                }

                //显示当前触摸的字母
                showCurrentWord(letter);
            }
        });
    }

    private boolean isScale = false;
    private Handler handler = new Handler();
    protected void showCurrentWord(String letter) {
        currentWord.setText(letter);
        if(!isScale){
            isScale = true;
            ViewPropertyAnimator.animate(currentWord).scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(450).start();
            ViewPropertyAnimator.animate(currentWord).scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(450).start();
        }

        //先移除之前的任务
        handler.removeCallbacksAndMessages(null);

        //延时隐藏currentWord
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//				currentWord.setVisibility(View.INVISIBLE);
                ViewPropertyAnimator.animate(currentWord).scaleX(0f).setDuration(450).start();
                ViewPropertyAnimator.animate(currentWord).scaleY(0f).setDuration(450).start();
                isScale = false;
            }
        }, 1500);
    }

    /**
     * 填充数据
     */
    private void fillList() {
        // 虚拟数据
        address.add(new Address("天津"));
        address.add(new Address("北京"));
        address.add(new Address("山西"));
        address.add(new Address("河北"));
        address.add(new Address("辽宁"));
        address.add(new Address("内蒙古"));
        address.add(new Address("北京"));
        address.add(new Address("江苏"));
        address.add(new Address("上海"));
        address.add(new Address("福建"));
        address.add(new Address("江西"));
        address.add(new Address("浙江省"));
        address.add(new Address("香港特别行政区"));
        address.add(new Address("台湾"));
        address.add(new Address("新疆"));
        address.add(new Address("宁夏"));
        address.add(new Address("青海"));
        address.add(new Address("陕西"));
        address.add(new Address("甘肃省"));
        address.add(new Address("西藏"));
        address.add(new Address("云南"));
        address.add(new Address("贵州"));
        address.add(new Address("重庆"));
        address.add(new Address("海南"));
        address.add(new Address("广西"));
        address.add(new Address("广东"));
        address.add(new Address("河南"));
        address.add(new Address("黑龙江"));
        address.add(new Address("吉林"));
        address.add(new Address("安徽"));
        address.add(new Address("四川"));
        address.add(new Address("山东"));
        address.add(new Address("湖北"));
        address.add(new Address("湖南"));
    }
    //所有选中的测量
    private int getListSelectY(int position){
        int top=addressListView.getTop();//获得listView的top
        View itemView= addressListView.getAdapter().getView(position,null,addressListView);

        addressListView.getChildAt(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.location=((TextView)view).getText()+"";
                finish();
            }
        });

        itemView.measure(0,0);
        int cellHeight=itemView.getMeasuredHeight();//获得一个itemView的高度
        return cellHeight*position+top;//计算有多少个item的高加上listView top的坐标
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            //重新定位
            case R.id.relocation:
                getLocationPermission();
                break;
        }
    }

    private void getLocationPermission(){
        //权限申请
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if(!permissionList.isEmpty()){
            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
            requestLocation();
        }
    }
}

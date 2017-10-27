package weifen.com.eclife.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import weifen.com.common.base.BaseFragment;
import weifen.com.common.commonAdapter.MyAdapter;
import weifen.com.common.commonAdapter.PageAdapter;
import weifen.com.common.commonAdapter.ViewHolder;
import weifen.com.common.utils.FixedViewUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.activity.MaintainActivity;
import weifen.com.eclife.activity.SelectAddressActivity;
import weifen.com.eclife.activity.ShopActivity;
import weifen.com.eclife.app.MyApplication;

/**主界面
 * Created by Administrator on 2017/2/27.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private TextView address,publish,preferential;
    private EditText search;
    private ImageView scan;
    private ViewPager viewpager;
    private LinearLayout guide,homeTeach,clean,maintain,all;
    //扫描权限
    private static final int REQ_CODE_PERMISSION = 0x1111;
    //请求地址
    private static final int SCAN_CODE_PERMISSION = 0x1110;
    //广告图片集合
    private List<ImageView> advImages;
    private CirclePageIndicator indicator;
    //控制发布及活动特惠的下划线的显示
    private View publishView,preferentialView;

    //在发布与活动特惠之间切换
    FrameLayout frameLayout;
    //上一次选中的Id
    int lastSelectId;

//    //填充发布与活动特惠里面的数据
//    private List<Shopping>datas;
//    private ListView shoppingList;
//    private MyAdapter<Shopping> adapter;

    //地图实例
    public LocationClient mLocationClient;
    public LocationClientOption option;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(View view) {

        address= (TextView) view.findViewById(R.id.address);
        publish= (TextView) view.findViewById(R.id.publish);
        preferential= (TextView) view.findViewById(R.id.preferential);
        search= (EditText) view.findViewById(R.id.search);
        scan= (ImageView) view.findViewById(R.id.scan);
        guide= (LinearLayout) view.findViewById(R.id.guide);
        homeTeach= (LinearLayout) view.findViewById(R.id.home_teach);
        clean= (LinearLayout) view.findViewById(R.id.clean);
        maintain= (LinearLayout) view.findViewById(R.id.maintain);
        all= (LinearLayout) view.findViewById(R.id.all);
        viewpager= (ViewPager) view.findViewById(R.id.viewpager);
        indicator= (CirclePageIndicator) view.findViewById(R.id.indicator);
        preferentialView=view.findViewById(R.id.preferential_view);
        publishView=view.findViewById(R.id.publish_view);

        address.setOnClickListener(this);
        scan.setOnClickListener(this);
        guide.setOnClickListener(this);
        homeTeach.setOnClickListener(this);
        clean.setOnClickListener(this);
        maintain.setOnClickListener(this);
        all.setOnClickListener(this);
        publish.setOnClickListener(this);
        preferential.setOnClickListener(this);
        search.setOnClickListener(this);

        mLocationClient=new LocationClient(activity);
        mLocationClient.registerLocationListener(new MyLocationListener());
        option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        //请求权限
        getLocationPermission();

//        shoppingList= (ListView) view.findViewById(R.id.shopping_list);
//        datas=new ArrayList<>();

//        //加载数据并摆放数据
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BmobUtil.queryShopping(activity, new BmobUtil.ShoppingCallback() {
//                    @Override
//                    public void personOk(List<Shopping> list) {
//                        if(list.size()>0){
//                            getData(list);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter=new MyAdapter<Shopping>(activity,datas,R.layout.item_category) {
//                                        @Override
//                                        public void convert(ViewHolder holder, Shopping item) {
//                                            holder.setImageByUrl(R.id.category_icon,item.getIconUrl());
//                                            //TODO注意，以后这里要换item.getType()   item.getSmallType()
//                                            holder.setText(R.id.tv_category_title,item.getType());
//                                            holder.setText(R.id.tv_category_info,item.getSmallType());
//                                            holder.setText(R.id.tv_category_update_time,(item.getCreatedAt()).split(" ")[0]);
//                                            holder.setText(R.id.tv_category_address,item.getDistance());
//                                            holder.setText(R.id.tv_category_price,item.getPrice());
//                                        }
//                                    };
//                                    shoppingList.setAdapter(adapter);
//                                    FixedViewUtil.resetListViewHeight(shoppingList);
//
//                                    //设置子项点击事件
//                                    shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                            Toast.makeText(activity,"你点击了"+((TextView)view.findViewById(R.id.tv_category_info)).getText(),Toast.LENGTH_LONG).show();
//                                            Intent intent=new Intent(activity, ShopActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        }).start();


        advImages=new ArrayList<>();
        //对接网络加载数据
        int []imageResId = new int[]{R.mipmap.test00,R.mipmap.test01,R.mipmap.test02,R.mipmap.test03,R.mipmap.test04,R.mipmap.test05};

        for(int i=0;i<imageResId.length;i++){
            ImageView imageView = new ImageView(activity);
            imageView.setBackgroundResource(imageResId[i]);
            advImages.add(imageView);
        }

        viewpager.setAdapter(new PageAdapter(advImages));
        indicator.setViewPager(viewpager);
        indicator.setFillColor(getResources().getColor(R.color.divider));

        lastSelectId=R.id.publish;
    }


//    //由于向bmob请求数据是异步的，所以添加这个方法把回调中数据取出来
//    private void getData(List<Shopping>datas){
//        this.datas=datas;
//    }

    private void getLocationPermission(){
        //权限申请
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if(!permissionList.isEmpty()){
            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity,permissions,SCAN_CODE_PERMISSION);
        }else {
            requestLocation();
        }
    }

    private void requestLocation(){
        mLocationClient.start();
    }

    @Override
    public void onClick(View view) {

        if(lastSelectId==view.getId()){
            return;
        }

        Intent intent;

        switch (view.getId()){
            //定位
            case R.id.address:
                intent=new Intent(activity, SelectAddressActivity.class);
                startActivity(intent);
                break;
            //扫码
            case R.id.scan:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }
                break;
            //导游
            case R.id.guide:
                break;
            //搜索跳转
            case R.id.search:
                break;
            //家教
            case R.id.home_teach:
                break;
            //保洁陪护
            case R.id.clean:
                break;
            //上门服务
            case R.id.maintain:
                intent=new Intent(activity, MaintainActivity.class);
                startActivity(intent);
                break;
            //全部分类
            case R.id.all:
                break;
            //发布
            case R.id.publish:
                publishView.setVisibility(View.VISIBLE);
                preferentialView.setVisibility(View.GONE);
                lastSelectId=R.id.publish;
                break;
            //活动特惠
            case R.id.preferential:
                publishView.setVisibility(View.GONE);
                preferentialView.setVisibility(View.VISIBLE);
                lastSelectId=R.id.preferential;
                break;
        }
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(activity, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    Toast.makeText(activity, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case SCAN_CODE_PERMISSION:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(activity,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(activity,"发送未知错误",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition=new StringBuilder();

            MyApplication.longitude=bdLocation.getLongitude();//保存经度
            MyApplication.latitude=bdLocation.getLatitude();//保存纬度

            currentPosition.append(bdLocation.getDistrict());
            if(currentPosition.length()>3){
                address.setText(currentPosition.substring(0,2)+"...");
            }else {
                address.setText(currentPosition);
            }

            MyApplication.location=currentPosition.toString();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if((MyApplication.location).length()>3){
            address.setText((MyApplication.location).substring(0,2)+"..");
        }else {
            address.setText((MyApplication.location));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case -1:
                        Toast.makeText(activity,data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT)+"",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            Toast.makeText(activity,"扫描失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;
        }
    }
}

package weifen.com.eclife.fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import weifen.com.common.adapter.MyFragmentPagerAdapter;
import weifen.com.common.base.BaseFragment;
import weifen.com.eclife.R;

/**
 * 附近界面
 * Created by Administrator on 2017/2/27.
 */
public class NeighbourFragment extends BaseFragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] tabTitles=new String[]{"家政","出游","维修","其他"};
    private NeighbourPagerAdapter neighbourPagerAdapter;
    private TextView selectedAddress;

    //地图实例
    public LocationClient mLocationClient;
    public LocationClientOption option;

    @Override
    protected int getLayout() {
        return R.layout.fragment_neighbour;
    }

    @Override
    protected void init(View view) {
        initView(view);
        initAdapter();

        mLocationClient=new LocationClient(activity);
        mLocationClient.registerLocationListener(new MyLocationListener());
        option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        //请求权限
        getLocationPermission();

    }

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
            ActivityCompat.requestPermissions(activity,permissions,1);
        }else {
            requestLocation();
        }
    }

    private void requestLocation(){
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition=new StringBuilder();

            currentPosition.append(bdLocation.getDistrict());
            if(currentPosition.length()>3){
                selectedAddress.setText(currentPosition.substring(0,2)+"...");
            }else {
                selectedAddress.setText(currentPosition);
            }

        }
    }

    private void initAdapter() {
        neighbourPagerAdapter=new NeighbourPagerAdapter(getFragmentManager(),tabTitles);
        viewPager.setAdapter(neighbourPagerAdapter);
    }

    private void initView(View view) {
        viewPager= (ViewPager) view.findViewById(R.id.nearViewPager);
        tabLayout= (TabLayout) view.findViewById(R.id.tab_layout_near);
        selectedAddress= (TextView) view.findViewById(R.id.selectedAddress);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class NeighbourPagerAdapter extends MyFragmentPagerAdapter {
        private String[] tabTitle;
        public NeighbourPagerAdapter(FragmentManager fm, String[] tabTitle){
            super(fm);
            this.tabTitle=tabTitle;
        }
        @Override
        public int getCount() {
            return tabTitle==null?0:tabTitle.length;
        }

        @Override
        public Fragment getItem(int position) {
            NeighbourTabFragment neighbourTabFragment=new NeighbourTabFragment();
            neighbourTabFragment.setType(position);
            return neighbourTabFragment;
        }

       @Override
       public CharSequence getPageTitle(int position) {
           return tabTitle[position];
       }
   }
}

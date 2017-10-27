package weifen.com.common.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by zhurencong on 2017/9/19.
 */
public class LocationUtil {

    //地图实例
    public LocationClient mLocationClient;
    public LocationClientOption option;
    private String address;

    public LocationUtil(Context context) {

        mLocationClient=new LocationClient(context);
        mLocationClient.registerLocationListener(new MyLocationListener());
        option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }

    public String getLocation(){
        mLocationClient.stop();
        return address;
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

            address=bdLocation.getDistrict();

        }
    }
}

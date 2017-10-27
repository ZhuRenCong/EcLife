package weifen.com.eclife.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import weifen.com.common.app.BaseApplication;
import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.eclife.R;
import weifen.com.eclife.app.MyApplication;
import weifen.com.eclife.domain.URLUtil;

/**
 * Created by zhurencong on 2017/9/20.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private LinearLayout personalData,accountSafe,infoAuthentication,contact_Us,agreement;
    private TextView exit;
    private CircleImageView image;

    @Override
    protected int setLayout() {
        return R.layout.setting_layout;
    }

    @Override
    protected void initView() {

        ActivityCollector.addActivity(this);

        back= (ImageView) findViewById(R.id.back);
        personalData= (LinearLayout) findViewById(R.id.personal_data);
        accountSafe= (LinearLayout) findViewById(R.id.account_safe);
        infoAuthentication= (LinearLayout) findViewById(R.id.info_authentication);
        contact_Us= (LinearLayout) findViewById(R.id.contact_us);
        agreement= (LinearLayout) findViewById(R.id.agreement);
        exit= (TextView) findViewById(R.id.exit);
        image= (CircleImageView) findViewById(R.id.image);

        back.setOnClickListener(this);
        personalData.setOnClickListener(this);
        accountSafe.setOnClickListener(this);
        infoAuthentication.setOnClickListener(this);
        contact_Us.setOnClickListener(this);
        agreement.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!"".equals(MyApplication.user.getTel()+"")){
            setUserInfo();
        }
    }

    public void setUserInfo(){

        Picasso.with(this).load(URLUtil.URL_PATH+ MyApplication.user.getImageUrl()).error(getResources().getDrawable(R.mipmap.person_iocn)).into(image);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            //返回
            case R.id.back:
                finish();
                break;
            //个人资料
            case R.id.personal_data:
                intent=new Intent(this,InfoActivity.class);
                startActivity(intent);
                break;
            //账户安全
            case R.id.account_safe:
                break;
            //信息认证
            case R.id.info_authentication:
                break;
            //联系我们
            case R.id.contact_us:
                intent=new Intent(this,ContactUsActivity.class);
                startActivity(intent);
                break;
            //协议
            case R.id.agreement:
                break;
            //退出登录
            case R.id.exit:
                ActivityCollector.finishAll();
                break;
        }
    }
}

package weifen.com.eclife.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.NumberIsValid;
import weifen.com.eclife.R;
import weifen.com.eclife.requestResult.RequestResult;

/**
 * Created by zhurencong on 2017/10/14.
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private EditText account,verificationCode;
    private Button verify,login;
    private TextView toLogin,aboutUs;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            verify.setClickable(false);
            if(msg.what==1){
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        verify.setText(millisUntilFinished / 1000 + "秒");
                    }

                    @Override
                    public void onFinish() {
                        verify.setClickable(true);
                        verify.setText("验证");
                    }
                }.start();
            }
        }
    };

    @Override
    protected int setLayout() {
        return R.layout.forget_password;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        account= (EditText) findViewById(R.id.account);
        verificationCode= (EditText) findViewById(R.id.verification_code);
        verify= (Button) findViewById(R.id.verify);
        login= (Button) findViewById(R.id.login );
        toLogin= (TextView) findViewById(R.id.to_login);
        aboutUs= (TextView) findViewById(R.id.about_us);

        verify.setOnClickListener(this);
        toLogin.setOnClickListener(this);
        login.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //获取验证码
            case R.id.verify:
                if(!NumberIsValid.verifyPhoneNum(account.getText()+"")){
                    Toast.makeText(this,"请输入11位有效数字！",Toast.LENGTH_LONG).show();
                }else {
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    RequestResult.getVerify(account.getText()+"",this,"http://60.205.204.56/forgetpsd.php");
                }
                break;
            //密码登录
            case R.id.to_login:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            //登录
            case R.id.login:
                if(!NumberIsValid.verifyPhoneNum(account.getText()+"")){
                    Toast.makeText(this,"请输入11位有效数字！",Toast.LENGTH_LONG).show();
                }else if((verificationCode.getText()).length()!=4){
                    Toast.makeText(this,"请输入4位有效验证码！",Toast.LENGTH_LONG).show();
                }else {
                    RequestResult.login(account.getText()+"",this,false,verificationCode.getText()+"");
                }
                break;
            //关于我们
            case R.id.about_us:
                break;
        }
    }
}

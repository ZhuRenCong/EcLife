package weifen.com.eclife.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.NumberIsValid;
import weifen.com.eclife.R;
import weifen.com.eclife.requestResult.RequestResult;
import weifen.com.request.utils.RequestUtil;

/**
 * Created by zhurencong on 2017/10/11.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText account,password,verificationCode;
    private ImageView showpassword;
    private Button verify,register;
    private TextView toLogin;
    //是否显示密码 0----不显示  1-----显示
    private int isShow=0;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                verify.setClickable(false);
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
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        account= (EditText) findViewById(R.id.account);
        password= (EditText) findViewById(R.id.password);
        verificationCode= (EditText) findViewById(R.id.verification_code);
        showpassword= (ImageView) findViewById(R.id.showpassword);
        verify= (Button) findViewById(R.id.verify);
        register= (Button) findViewById(R.id.register);
        toLogin= (TextView) findViewById(R.id.to_login);

        showpassword.setOnClickListener(this);
        verify.setOnClickListener(this);
        toLogin.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        String tel=account.getText()+"";
        String code=verificationCode.getText()+"";
        String pwd=password.getText()+"";

        switch (view.getId()){
            //显示密码
            case R.id.showpassword:
                if(isShow==0){
                    //显示密码
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.length());
                    showpassword.setImageResource(R.mipmap.eye);
                    isShow=1;
                }else {
                    //隐藏密码
                    showpassword.setImageResource(R.mipmap.not_eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.length());
                    isShow=0;
                }
                break;
            //验证
            case R.id.verify:
                if("".equals(tel)){
                    Toast.makeText(this,"手机号不能为空!",Toast.LENGTH_LONG).show();
                }else if(!NumberIsValid.verifyPhoneNum(tel)){
                    Toast.makeText(this,"请输入11位有效手机号!",Toast.LENGTH_LONG).show();
                }else {
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    RequestResult.getVerify(account.getText()+"",this,"http://60.205.204.56/register.php");
                }
                break;
            //返回登录
            case R.id.to_login:
                intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            //注册
            case R.id.register:
                if("".equals(tel)){
                    Toast.makeText(this,"手机号不能为空!",Toast.LENGTH_LONG).show();
                }else if(!NumberIsValid.verifyPhoneNum(tel)){
                    Toast.makeText(this,"请输入11位有效手机号!",Toast.LENGTH_LONG).show();
                }else if(code.length()!=4){
                    Toast.makeText(this,"请输入4位有效数字!",Toast.LENGTH_LONG).show();
                }else {
                    RequestResult.Verify(account.getText()+"",verificationCode.getText()+"",password.getText()+"",this);
                }
                break;
        }
    }
}

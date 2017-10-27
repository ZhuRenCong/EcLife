package weifen.com.eclife.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import weifen.com.common.base.BaseActivity;
import weifen.com.eclife.R;
import weifen.com.eclife.domain.URLUtil;
import weifen.com.eclife.requestResult.RequestResult;
import weifen.com.request.utils.RequestUtil;

/**
 * Created by zhurencong on 2017/10/18.
 */
public class ContactUsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back,mark;
    private LinearLayout contactNumber,youxiang,onlineContact,ideaToUs;
    private Button commit;
    private EditText ideaContent;
    private TextView qq,number;
    //上一个隐藏状态    0-----隐藏    1-----显示
    private int markNumber=0;

    @Override
    protected int setLayout() {
        return R.layout.contact_us;
    }

    @Override
    protected void initView() {

        back= (ImageView) findViewById(R.id.back);
        mark= (ImageView) findViewById(R.id.mark);
        contactNumber= (LinearLayout) findViewById(R.id.contact_number);
        youxiang= (LinearLayout) findViewById(R.id.youxiang);
        onlineContact= (LinearLayout) findViewById(R.id.online_contact);
        ideaToUs= (LinearLayout) findViewById(R.id.idea_to_us);
        commit= (Button) findViewById(R.id.commit);
        ideaContent= (EditText) findViewById(R.id.idea_content);
        qq= (TextView) findViewById(R.id.qq);
        number= (TextView) findViewById(R.id.number);

        back.setOnClickListener(this);
        contactNumber.setOnClickListener(this);
        youxiang.setOnClickListener(this);
        onlineContact.setOnClickListener(this);
        ideaToUs.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //返回
            case R.id.back:
                finish();
                break;
            //联系客服
            case R.id.contact_number:
                contactDialog(number.getText()+"");
                break;
            //复制邮箱号
            case R.id.youxiang:
                ClipboardManager copy = (ClipboardManager) this .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(qq.getText()+"");
                Toast.makeText(this,"邮箱已经被复制",Toast.LENGTH_SHORT).show();
                break;
            //在线联系
            case R.id.online_contact:
                RequestResult.changeService(this,null,null);
                break;
            //建议反馈
            case R.id.idea_to_us:
                if(markNumber==0){
                    ideaContent.setVisibility(View.VISIBLE);
                    commit.setVisibility(View.VISIBLE);
                    mark.setImageResource(R.mipmap.back_up);
                    markNumber=1;
                }else {
                    ideaContent.setVisibility(View.INVISIBLE);
                    commit.setVisibility(View.INVISIBLE);
                    mark.setImageResource(R.mipmap.back_down);
                    markNumber=0;
                }
                break;
            //提交建议
            case R.id.commit:
                if(!"".equals(ideaContent.getText()+"")){
                    RequestResult.commitSuggest(this, URLUtil.CONTACT_US_SUGGEST,ideaContent.getText()+"");
                }else {
                    Toast.makeText(this,"请输入建议内容！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void contactDialog(final String contactNumber){

        View view=getLayoutInflater().inflate(R.layout.contact_dialog_layout,null);

        final AlertDialog dialog=new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.show();
        TextView contact= (TextView) view.findViewById(R.id.contact);
        Button call= (Button) view.findViewById(R.id.call);
        Button cancel= (Button) view.findViewById(R.id.cancel);
        contact.setText(contactNumber);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactNumber));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}

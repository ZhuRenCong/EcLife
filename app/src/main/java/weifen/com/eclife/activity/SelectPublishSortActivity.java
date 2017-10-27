package weifen.com.eclife.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import weifen.com.common.base.BaseActivity;
import weifen.com.eclife.R;
import weifen.com.eclife.domain.Constant;

/**
 * Created by zhurencong on 2017/9/25.
 */
public class SelectPublishSortActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout guide,homeTeach,clean,maintain,all;
    private TextView cancel,sendAgrement,getAgrement;

    @Override
    protected int setLayout() {
        return R.layout.select_publish_sort;
    }

    @Override
    protected void initView() {

        guide= (LinearLayout) findViewById(R.id.guide);
        homeTeach= (LinearLayout) findViewById(R.id.home_teach);
        clean= (LinearLayout) findViewById(R.id.clean);
        maintain= (LinearLayout) findViewById(R.id.maintain);
        all= (LinearLayout) findViewById(R.id.all);
        cancel= (TextView) findViewById(R.id.cancel);
        sendAgrement= (TextView) findViewById(R.id.send_agrement);
        getAgrement= (TextView) findViewById(R.id.get_agrement);

        guide.setOnClickListener(this);
        homeTeach.setOnClickListener(this);
        clean.setOnClickListener(this);
        maintain.setOnClickListener(this);
        all.setOnClickListener(this);
        cancel.setOnClickListener(this);
        sendAgrement.setOnClickListener(this);
        getAgrement.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()){
            //向导
            case R.id.guide:
                intent=new Intent(SelectPublishSortActivity.this,PublishActivity.class);
                intent.putExtra(Constant.WHICHTYPE,Constant.GUIDE);
                startActivity(intent);
                finish();
                break;
            //家教
            case R.id.home_teach:
                intent=new Intent(SelectPublishSortActivity.this,PublishActivity.class);
                intent.putExtra(Constant.WHICHTYPE,Constant.HOMETEACH);
                startActivity(intent);
                finish();
                break;
            //保洁陪护
            case R.id.clean:
                intent=new Intent(SelectPublishSortActivity.this,PublishActivity.class);
                intent.putExtra(Constant.WHICHTYPE,Constant.CLEAN);
                startActivity(intent);
                finish();
                break;
            //上门维护
            case R.id.maintain:
                intent=new Intent(SelectPublishSortActivity.this,PublishActivity.class);
                intent.putExtra(Constant.WHICHTYPE,Constant.MAINTAIN);
                startActivity(intent);
                finish();
                break;
            //其他分类
            case R.id.all:
                intent=new Intent(SelectPublishSortActivity.this,PublishActivity.class);
                intent.putExtra(Constant.WHICHTYPE,Constant.ALL);
                startActivity(intent);
                finish();
                break;
            //取消
            case R.id.cancel:
                finish();
                break;
            //发单协议
            case R.id.send_agrement:
                break;
            //接单协议
            case R.id.get_agrement:
                break;
        }
    }
}

package weifen.com.eclife.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import io.rong.imkit.RongIM;
import weifen.com.common.base.BaseActivity;
import weifen.com.eclife.R;
import weifen.com.eclife.db.ShoppingCom;

/**
 * Created by zhurencong on 2017/9/27.
 */
public class ShopActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout chat;
    private ShoppingCom shopping;

    @Override
    protected int setLayout() {
        return R.layout.shop_layout;
    }

    @Override
    protected void initView() {

        chat= (LinearLayout) findViewById(R.id.chat);
        chat.setOnClickListener(this);
        shopping= (ShoppingCom) getIntent().getSerializableExtra("data");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.chat:
//                RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
//                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
//                RongIM.getInstance().startPrivateChat(this,shopping.getTel(),shopping.getTitle());
                break;
        }
    }
}

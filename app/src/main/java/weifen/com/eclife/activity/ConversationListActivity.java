package weifen.com.eclife.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import weifen.com.common.base.BaseActivity;
import weifen.com.eclife.R;

/**
 * 会话列表
 * Created by zhurencong on 2017/10/24.
 */
public class ConversationListActivity extends BaseActivity implements View.OnClickListener {

    private Fragment mConversationList = null;
    private ImageView back;

    @Override
    protected int setLayout() {
        return R.layout.conversationlist;
    }

    @Override
    protected void initView() {
        mConversationList = initConversationList();//融云会话列表的对象
        back= (ImageView) findViewById(R.id.back);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction =   fragmentManager.beginTransaction();
        beginTransaction.add(R.id.conversationlist,mConversationList);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();

        back.setOnClickListener(this);
    }

    //集成会话列表
    public Fragment initConversationList(){
        if(mConversationList == null){
            ConversationListFragment conversationListFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                    .build();
            conversationListFragment.setUri(uri);
            return conversationListFragment;
        }else{
            return mConversationList;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}

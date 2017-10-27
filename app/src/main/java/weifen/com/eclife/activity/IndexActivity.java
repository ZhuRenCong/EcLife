package weifen.com.eclife.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;

import java.util.List;
import java.util.Map;

import weifen.com.common.base.BaseActivity;
import weifen.com.common.utils.ActivityCollector;
import weifen.com.common.utils.FragmentUtil;
import weifen.com.eclife.R;
import weifen.com.eclife.fragment.CommunityFragment;
import weifen.com.eclife.fragment.MainFragment;
import weifen.com.eclife.fragment.MineFragment;
import weifen.com.eclife.fragment.NeighbourFragment;

/**
 * Created by Administrator on 2017/3/1.
 */
public class IndexActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "IndexActivity";
    //内容区
    FrameLayout contentLayout;
    private Fragment lastFrament;
    private List<Map<String,Fragment>> fragmentList;

    //上一次选中的Id
    int lastSelectId;
    //当前内容Fragment
    Fragment currentContentFragment;
    Fragment mainFragment,saleFragment,findFragment,mineFragment;

    @Override
    protected int setLayout() {
        return R.layout.layout_index;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        LinearLayout tabRoot = (LinearLayout) findViewById(R.id.tab_root);
        int childCount = tabRoot.getChildCount();
        for(int i=0;i<childCount;i++){
            tabRoot.getChildAt(i).setOnClickListener(this);
        }
        select(R.id.tab_main);
    }

    /**
     * 监听tab的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        select(v.getId());
    }

    private void select(int id){
        if(lastSelectId==id){
            return;
        }
        LinearLayout tab;
        ImageView imageView;

        //取消上一个选中的
        if(lastSelectId!=0){
            tab = (LinearLayout) findViewById(lastSelectId);
            imageView = (ImageView) tab.getChildAt(0);
            switch (lastSelectId){
                case R.id.tab_main:
                    imageView.setImageResource(R.mipmap.main_unselect);
                    break;
                case R.id.tab_neighbour:
                    imageView.setImageResource(R.mipmap.sale_unselect);
                    break;
                case R.id.tab_community:
                    imageView.setImageResource(R.mipmap.find_unselect);
                    break;
                case R.id.tab_mine:
                    imageView.setImageResource(R.mipmap.my_unselect);
                    break;
            }
        }

        tab = (LinearLayout) findViewById(id);
        imageView = (ImageView) tab.getChildAt(0);
        switch (id){
            case R.id.tab_main:
                imageView.setImageResource(R.mipmap.main_select);
                if(mainFragment==null){
                    mainFragment = new MainFragment();
                }
                //显示framgent
                showFramgent(mainFragment,"main");
                break;
            case R.id.tab_neighbour:
                imageView.setImageResource(R.mipmap.sale_select);
                if(saleFragment==null){
                    saleFragment = new NeighbourFragment();
                }
                showFramgent(saleFragment,"sale");
                break;
            case R.id.tab_community:
                imageView.setImageResource(R.mipmap.find_select);
                if(findFragment==null){
                    findFragment = new CommunityFragment();
                }
                showFramgent(findFragment,"find");
                break;
            case R.id.tab_mine:
                imageView.setImageResource(R.mipmap.my_select);
                if(mineFragment==null){
                    mineFragment = new MineFragment();
                }
                showFramgent(mineFragment,"mine");
                break;
        }
        lastSelectId=id;
    }


    private void showFramgent(Fragment content, String tag){
        Log.d(TAG, "showFramgent: ---------------"+content.hashCode());
        FragmentManager fragmentManager = getFragmentManager();
        //隐藏Framgent
        FragmentUtil.hideFrament(fragmentManager,lastFrament);
        //显示framgent
        FragmentUtil.showFrament(fragmentManager,R.id.fragment_content,content,tag);
        lastFrament = content;
    }
}


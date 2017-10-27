package weifen.com.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 上拉到一定程度显示渐变效果的ScrollView
 */
public class GradualScrollView extends ScrollView{

    View topView;
    public GradualScrollView(Context context) {
        this(context,null);
    }

    public GradualScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradualScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopView(View topView){
        this.topView=topView;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(topView==null){
            return;
        }
        if(t>0&&t<topView.getHeight()){
            //计算百分比
            if(topView.getVisibility()==View.INVISIBLE){
                topView.setVisibility(VISIBLE);
            }
            float fraction=t*1.0f/topView.getHeight();
            //执行topView的渐变动画
            showTopViewAnimation(fraction);
        }else if(t==0&&topView.getVisibility()==View.VISIBLE){
            ViewCompat.setAlpha(topView,1);
            topView.clearAnimation();
            topView.setVisibility(View.INVISIBLE);
        }else if(t>topView.getHeight()){
            ViewCompat.setAlpha(topView,1);
            topView.clearAnimation();
            if(topView.getVisibility()==View.INVISIBLE){
                topView.setVisibility(VISIBLE);
            }
        }
    }

    private void showTopViewAnimation( float fraction) {
        ViewCompat.setAlpha(topView,fraction);
    }
}

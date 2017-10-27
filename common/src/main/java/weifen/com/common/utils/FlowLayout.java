package weifen.com.common.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 袁磊 on 2017/2/17.
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        //调用两个参数的构造方法
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        //调用三个参数的构造方法
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);//Group宽度
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);//Group宽度测量模式
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);//Group高度
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);//Group高度测量模式

        //wrap_content情况下的宽高
        int width = 0;
        int height = 0;

        //记录每一行的总宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //得到内部元素的个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到子View的LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //换行时
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {//（当前第一行宽度）+（一个子View宽度）>（控件宽度）
                //对比得到最大的行宽度
                width = Math.max(width, lineWidth);
                //重置lineWidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {//不换行时
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一个子控件
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        Log.d("TAG", "sizeWidth=" + sizeWidth);
        Log.d("TAG", "sizeHeight=" + sizeHeight);

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    //存储所有的View（注：一行一行存储）
    private List<List<View>> mAllViews = new ArrayList<>();
    //每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin >
                    width - getPaddingLeft() - getPaddingRight()) {

                //记录LineHeight
                mLineHeight.add(lineHeight);
                //记录当前行所有的子View
                mAllViews.add(lineViews);

                //重置我们的行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //重置我们的集合
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }//for循环结束
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //设置子View的位置

        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            //当前行的所有的View(每行整体)
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //遍历每一行的所有View（每行里面的子View）
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                //判读child的状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                //child的left，right，top，bottom
                int lc = left + lp.leftMargin;
                int rc = lc + child.getMeasuredWidth();
                int tc = top + lp.topMargin;
                int bc = tc + child.getMeasuredHeight();

                //为子View进行布局
                child.layout(lc, tc, rc, bc);

                //在每一行中的子View，只需要改变left的位置，高度一样
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }


    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
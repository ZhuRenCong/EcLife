package weifen.com.eclife.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2017/10/21.
 */
public abstract class BasePopupwindow {
    Context context;
    PopupWindow popupWindow;
    PopCallBack popCallBack;
    public BasePopupwindow(Context context, PopCallBack popCallBack){
        this.context=context;
        this.popCallBack=popCallBack;
    }


    public void init(){
        popupWindow=new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(getPopView());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void open(View parent,int gravity,int x,int y){
        if (popupWindow!=null) {
            popupWindow.showAtLocation(parent,gravity,x,y);
        }
    }

    public void open(View anchor){
        if (popupWindow!=null) {
            popupWindow.showAsDropDown(anchor);
        }
    }

    public void close(){
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    /**
     *
     * @param authorView    呼出popupWindow的View
     * @return
     */
    public int[] calculatePopupWindow(View authorView){
        View contentView=getPopView();
        int[] authorLocation=new int[2];
        int[] windowLocation=new int[2];
        //获取view在屏幕上的坐标
        authorView.getLocationOnScreen(authorLocation);
        int authorHeight=authorView.getHeight();
        //获得屏幕的高度|宽度
        int screenWidth=context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight=context.getResources().getDisplayMetrics().heightPixels;

        //获取popupWindow的宽度和高度
        contentView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int windowWidth=contentView.getMeasuredWidth();
        int windowHeight=contentView.getMeasuredHeight();

        boolean isShowUpSign=(screenHeight-authorHeight-authorLocation[1])<windowHeight;
        if(isShowUpSign){//向上
            windowLocation[0]=0;
            windowLocation[1]=authorLocation[1]-windowHeight;
        }else{
            windowLocation[0]=0;
            windowLocation[1]=authorLocation[1]+authorHeight;
        }
        return windowLocation;
    }

    public abstract View getPopView();

    public interface PopCallBack{
        void setText(int type);//根据type显示排序的问题
    }
}

package weifen.com.eclife.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import weifen.com.eclife.R;

/**
 * Created by Administrator on 2017/10/21.
 */
public class SortPopupWindow extends BasePopupwindow implements View.OnClickListener {

    TextView allTV,priceTV,commentTV,saleTV,distanceTV;
    View leftTV;
    public SortPopupWindow(Context context, PopCallBack popCallBack) {
        super(context, popCallBack);
    }

    @Override
    public void init() {
        super.init();
        View view=popupWindow.getContentView();
        leftTV= (View) view.findViewById(R.id.left);
        allTV= (TextView) view.findViewById(R.id.all_sort);
        allTV.setTag(1);
        priceTV= (TextView) view.findViewById(R.id.price_sort);
        priceTV.setTag(2);
        commentTV= (TextView) view.findViewById(R.id.comment_sort);
        commentTV.setTag(3);
        saleTV= (TextView) view.findViewById(R.id.sale_sort);
        saleTV.setTag(4);
        distanceTV= (TextView) view.findViewById(R.id.distance_sort);
        distanceTV.setTag(5);

        allTV.setOnClickListener(this);
        priceTV.setOnClickListener(this);
        commentTV.setOnClickListener(this);
        saleTV.setOnClickListener(this);
        distanceTV.setOnClickListener(this);
        leftTV.setOnClickListener(this);
    }

    @Override
    public View getPopView() {
        return View.inflate(context, R.layout.layout_sort_pop,null);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.left){
            close();
            return;
        }
        popCallBack.setText((Integer) view.getTag());
        close();
    }
}

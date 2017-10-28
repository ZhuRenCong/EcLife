package weifen.com.eclife.pop;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import weifen.com.eclife.R;

/**
 * Created by WJC on 2017/10/28.
 */
public class ScreenPopupWindow extends BasePopupwindow implements View.OnClickListener {

    TextView cancelTV, sureTV;
    TextView selectPriceTV, selectDistanceTV, selectKeywordTV;
    LinearLayout priceRoot, keywordRoot;
    EditText minET, maxET, keyWordEt;
    int CURRENT_TYPE = 1;
    public static final int PRICE_SELECT_TYPE = 1;
    public static final int DISTANCE_SELECT_TYPE = 2;
    public static final int KEYWORD_TYPE = 3;

    public ScreenPopupWindow(Context context, PopCallBack popCallBack) {
        super(context, popCallBack);
    }

    @Override
    public void init() {
        super.init();
        //初始化获取控件
        View view = popupWindow.getContentView();

        cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        sureTV = (TextView) view.findViewById(R.id.tv_sure);

        selectPriceTV = (TextView) view.findViewById(R.id.tv_select_price);
        selectPriceTV.setTag(6);
        selectDistanceTV = (TextView) view.findViewById(R.id.tv_select_distance);
        selectDistanceTV.setTag(7);
        selectKeywordTV = (TextView) view.findViewById(R.id.tv_select_keyword);
        selectKeywordTV.setTag(8);
        //筛选选项的listView
        priceRoot = (LinearLayout) view.findViewById(R.id.price_screen_content);
        keywordRoot = (LinearLayout) view.findViewById(R.id.keyword_root);

        minET = (EditText) view.findViewById(R.id.et_min);
        maxET = (EditText) view.findViewById(R.id.et_max);
        keyWordEt = (EditText) view.findViewById(R.id.et_keyword);

        cancelTV.setOnClickListener(this);
        sureTV.setOnClickListener(this);
        selectPriceTV.setOnClickListener(this);
        selectDistanceTV.setOnClickListener(this);
        selectKeywordTV.setOnClickListener(this);

        setTVBG(true, false, false);
    }

    @Override
    public View getPopView() {
        return View.inflate(context, R.layout.layout_pop_screen, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_sure:
                popCallBack.sureSelect(CURRENT_TYPE, minET.getText().toString(), maxET.getText().toString(),keyWordEt.getText().toString() );
                close();
                break;
            case R.id.tv_cancel:
                close();
                break;
            case R.id.tv_select_price://点击价格
                setTVBG(true, false, false);
                //显示价格
                priceRoot.setVisibility(View.VISIBLE);
                keywordRoot.setVisibility(View.GONE);
                popCallBack.setText((Integer) view.getTag());
                CURRENT_TYPE = PRICE_SELECT_TYPE;
                break;
            case R.id.tv_select_distance://点击距离
                setTVBG(false, true, false);
                priceRoot.setVisibility(View.VISIBLE);
                keywordRoot.setVisibility(View.GONE);
                popCallBack.setText((Integer) view.getTag());
                CURRENT_TYPE = DISTANCE_SELECT_TYPE;
                break;
            case R.id.tv_select_keyword://点击关键字
                setTVBG(false, false, true);
                keywordRoot.setVisibility(View.VISIBLE);
                priceRoot.setVisibility(View.GONE);
                popCallBack.setText((Integer) view.getTag());
                CURRENT_TYPE = KEYWORD_TYPE;
                break;
        }
    }

    public void setTVBG(boolean sign1, boolean sign2, boolean sign3) {
        selectPriceTV.setSelected(sign1);
        selectDistanceTV.setSelected(sign2);
        selectKeywordTV.setSelected(sign3);
    }
}

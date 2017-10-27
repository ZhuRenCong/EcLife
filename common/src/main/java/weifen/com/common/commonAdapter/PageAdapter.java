package weifen.com.common.commonAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhurencong on 2017/9/19.
 */
public class PageAdapter extends PagerAdapter {

    List<ImageView> advImages;

    public PageAdapter(List<ImageView> advImages) {
        this.advImages=advImages;
    }

    @Override
    public int getCount() {
        return advImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = advImages.get(position);
        container.addView(imageView);
        return imageView;
    }
}

package tanglie.mypulltorefresh.my;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.w3c.dom.ProcessingInstruction;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class PullToRefreshListView extends ListView {

    private View headerView;

    public PullToRefreshListView(Context context) {
        super(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

        System.out.println("deltaX: " + deltaX);
        System.out.println("deltaY: " + deltaY);
        System.out.println("scrollX: " + scrollX);
        System.out.println("scrollY: " + scrollY);
        System.out.println("scrollRangeX: " + scrollRangeX);
        System.out.println("scrollRangeY: " + scrollRangeY);
        System.out.println("maxOverScrollX: " + maxOverScrollX);
        System.out.println("maxOverScrollY: " + maxOverScrollY);
        System.out.println("isTouchEvent: " + isTouchEvent);
        System.out.println("----------分割线----------");

        headerView.setVisibility(View.VISIBLE);
//        headerView.scrollBy(0, scrollY);


        return returnValue;
    }

    @Override
    public void addHeaderView(View view){
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

        FrameLayout frame = new FrameLayout(getContext());
        view.setVisibility(View.GONE);
        frame.addView(view, lp);

        this.headerView = view;
        super.addHeaderView(frame);

    }
}

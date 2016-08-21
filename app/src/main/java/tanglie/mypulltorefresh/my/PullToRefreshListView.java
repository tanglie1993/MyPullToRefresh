package tanglie.mypulltorefresh.my;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class PullToRefreshListView extends LinearLayout implements OverscrollListView.OverScrollListener {

    private View headerView;
    private OverscrollListView listView;

    private boolean isScrolling = false;
    private float lastEventY;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        listView = new OverscrollListView(context);
        listView.setOverScrollListener(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        addView(listView, 0, params);

        post(new Runnable() {
            @Override
            public void run() {
                headerView.setVisibility(View.VISIBLE);
                scrollTo(0, headerView.getLayoutParams().height);
            }
        });
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
        return returnValue;
    }

    public void addHeaderView(View view){
        headerView = view;
        int height = view.getLayoutParams().height;
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        addView(view, 0, lp);
        view.setVisibility(GONE);
        invalidate();
    }

    public void setAdapter(ArrayAdapter<String> stringArrayAdapter) {
        listView.setAdapter(stringArrayAdapter);
        invalidate();
    }

    @Override
    public void onOverScroll(int deltaY) {
        if(getScrollY() + deltaY > 0){
            scrollBy(0, deltaY);
        }
    }

    @Override
     public boolean onInterceptTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            return listView.getScrollY() == 0;
        }else{
            return isScrolling;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isScrolling = true;
            lastEventY = event.getY();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float deltaY = event.getY() - lastEventY;
            lastEventY = event.getY();
            onOverScroll(-(int) deltaY);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            lastEventY = 0;
            isScrolling = false;
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            lastEventY = 0;
            isScrolling = false;
        }
        return true;
    }
}

package tanglie.mypulltorefresh.my;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class PullToRefreshListView extends LinearLayout {

    private View headerView;
    private ListView listView;

    private boolean isScrolling = false;
    private float currentDragStartY;

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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(listView, 0, params);

        post(new Runnable() {
            @Override
            public void run() {
                headerView.setVisibility(View.VISIBLE);
                onOverScroll(0);
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        invalidate();
    }

    public void setAdapter(ArrayAdapter<String> stringArrayAdapter) {
        listView.setAdapter(stringArrayAdapter);
        invalidate();
    }

    public void onOverScroll(float scrollY) {
        ViewGroup.LayoutParams params = headerView.getLayoutParams();
        params.height = (int) scrollY;
        headerView.setLayoutParams(params);
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
            currentDragStartY = event.getY();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float deltaY = event.getY() - currentDragStartY;
            onOverScroll(deltaY);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            currentDragStartY = 0;
            isScrolling = false;
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            currentDragStartY = 0;
            isScrolling = false;
        }
        return true;
    }
}

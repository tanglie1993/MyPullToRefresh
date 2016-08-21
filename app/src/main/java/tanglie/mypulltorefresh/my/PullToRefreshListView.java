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

    private int headerMaxHeight;

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
        listView = new ListView(context);
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
        headerMaxHeight = view.getLayoutParams().height;
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(view, 0, lp);
        view.setVisibility(GONE);
        invalidate();
    }

    public void setAdapter(ArrayAdapter<String> stringArrayAdapter) {
        listView.setAdapter(stringArrayAdapter);
        invalidate();
    }

    public void onOverScroll(float scrollY) {
        if(scrollY < headerMaxHeight){
            ViewGroup.LayoutParams params = headerView.getLayoutParams();
            params.height = (int) scrollY;
            headerView.setLayoutParams(params);
        }
    }

    @Override
     public boolean onInterceptTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            return listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0;
        }else{
            return isScrolling;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isScrolling = true;
            currentDragStartY = event.getY();
            listView.onTouchEvent(event);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(event.getY() > currentDragStartY){
                float deltaY = event.getY() - currentDragStartY;
                onOverScroll(deltaY);
            }else{
                listView.onTouchEvent(event);
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            currentDragStartY = 0;
            isScrolling = false;
            listView.onTouchEvent(event);
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            currentDragStartY = 0;
            isScrolling = false;
            listView.onTouchEvent(event);
        }
        return true;
    }
}

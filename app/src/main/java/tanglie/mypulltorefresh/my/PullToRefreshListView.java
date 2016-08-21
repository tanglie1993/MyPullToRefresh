package tanglie.mypulltorefresh.my;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tanglie.mypulltorefresh.R;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class PullToRefreshListView extends LinearLayout {

    private static final int RELEASE_TO_REFRESH_THRESHOLD = 300;

    private View headerView;
    private ListView listView;

    private float currentDragStartY;
    private State currentState = State.NO_OVERSCROLL;

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
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            currentState = State.DRAGGING;
            currentDragStartY = event.getY();
            listView.onTouchEvent(event);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(event.getY() > currentDragStartY){
                float deltaY = event.getY() - currentDragStartY;
                if(deltaY > RELEASE_TO_REFRESH_THRESHOLD){
                    currentState = State.RELEASE_TO_REFRESH;
                    TextView headerTextView= (TextView) headerView.findViewById(R.id.headerTextView);
                    headerTextView.setText("Release To Refresh");
                }
                onOverScroll(deltaY);
            }else {
                listView.onTouchEvent(event);
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            currentDragStartY = 0;
            listView.onTouchEvent(event);
            if(currentState == State.RELEASE_TO_REFRESH){
                reset();
            }else if(currentState == State.DRAGGING){
                reset();
            }
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            currentDragStartY = 0;
            listView.onTouchEvent(event);
            if(currentState == State.RELEASE_TO_REFRESH){
                reset();
            }else if(currentState == State.DRAGGING){
                reset();
            }
        }
        return true;
    }

    private void reset() {
        currentState = State.RESETTING;
        final ValueAnimator animator = ValueAnimator.ofFloat((float) headerView.getLayoutParams().height, (float) 0);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float height = (Float) valueAnimator.getAnimatedValue();
                onOverScroll(height);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                currentState = State.NO_OVERSCROLL;
                TextView headerTextView = (TextView) headerView.findViewById(R.id.headerTextView);
                headerTextView.setText("Pull To Refresh");
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
}

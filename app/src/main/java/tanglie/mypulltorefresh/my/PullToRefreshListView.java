package tanglie.mypulltorefresh.my;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
    private float currentMotionSeriesStartY;
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
    }

    public void init(View headerView, Context context){
        if(this.headerView != null){
            return;
        }
        this.headerView = headerView;
        headerMaxHeight = headerView.getLayoutParams().height;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(context) + headerMaxHeight);
        addView(listView, 0, params);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, headerView.getLayoutParams().height);
        addView(headerView, 0, lp);
        headerView.setVisibility(GONE);

        invalidate();
    }

    public void setAdapter(ArrayAdapter<String> stringArrayAdapter) {
        listView.setAdapter(stringArrayAdapter);
        invalidate();
    }

    public void setOverScrollHeight(float scrollY) {
        if(scrollY < headerMaxHeight){
            if(headerView.getVisibility() != View.VISIBLE){
                headerView.setVisibility(View.VISIBLE);
            }
            scrollTo(0, headerMaxHeight - (int) scrollY);
        }
    }

    @Override
     public boolean onInterceptTouchEvent(MotionEvent event){
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            currentMotionSeriesStartY = event.getY();
            listView.onTouchEvent(event);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(currentState == State.LOADING || currentState == State.RESETTING){
                return true;
            }
            if(event.getY() > currentMotionSeriesStartY
                    && listView.getFirstVisiblePosition() == 0
                    && listView.getChildAt(0).getY() == 0){
                currentState = State.DRAGGING;
                if(currentDragStartY == 0){
                    currentDragStartY = event.getY();
                }
                float deltaY = event.getY() - currentDragStartY;
                if(deltaY > RELEASE_TO_REFRESH_THRESHOLD){
                    currentState = State.RELEASE_TO_REFRESH;
                    TextView headerTextView= (TextView) headerView.findViewById(R.id.headerTextView);
                    headerTextView.setText("Release To Refresh");
                }else{
                    currentState = State.DRAGGING;
                }
                setOverScrollHeight(deltaY);
            }else {
                currentState = State.NO_OVERSCROLL;
                listView.onTouchEvent(event);
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            listView.onTouchEvent(event);
            onMotionSeriesFinish(event);
        }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            listView.onTouchEvent(event);
            onMotionSeriesFinish(event);
        }
        return true;
    }

    private void onMotionSeriesFinish(MotionEvent event) {
        int deltaY = (int) (event.getY() - currentDragStartY);
        currentDragStartY = 0;
        currentMotionSeriesStartY = 0;
        if(currentState == State.RELEASE_TO_REFRESH){
            reset(deltaY);
        }else if(currentState == State.DRAGGING){
            reset(deltaY);
        }
    }

    private void reset(int deltaY) {
        currentState = State.RESETTING;
        final ValueAnimator animator = ValueAnimator.ofInt(deltaY, 0);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float height = (Integer) valueAnimator.getAnimatedValue();
                setOverScrollHeight(height);
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
//                headerView.setVisibility(View.GONE);
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

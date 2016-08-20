package tanglie.mypulltorefresh.my;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/20 0020.
 */
public class OverscrollListView extends ListView{
    public OverscrollListView(Context context) {
        super(context);
    }

    public OverscrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverscrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OverscrollListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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


        return returnValue;
    }
}

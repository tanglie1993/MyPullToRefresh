package tanglie.mypulltorefresh.my;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class ScreenUtils {
    public static final int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
}

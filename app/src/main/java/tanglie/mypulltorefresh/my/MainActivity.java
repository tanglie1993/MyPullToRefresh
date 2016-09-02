package tanglie.mypulltorefresh.my;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import tanglie.mypulltorefresh.R;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] strs = new String[20];
        for(int i = 0; i < strs.length; i++){
            strs[i] = ""+i;
        }
        PullToRefreshListView lv = (PullToRefreshListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strs));
        View view = getLayoutInflater().inflate(R.layout.header_view, (ViewGroup) findViewById(R.id.rootView), false);
//        measureView(view);
        lv.init(view, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void measureView(View child) {
//        ViewGroup.LayoutParams lp = child.getLayoutParams();
//        if(lp == null){
//            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        //headerView的宽度信息
//        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
//        int childMeasureHeight;
//        if(lp.height > 0){
//            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
//            //最后一个参数表示：适合、匹配
//        } else {
//            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
//        }
////System.out.println("childViewWidth"+childMeasureWidth);
////System.out.println("childViewHeight"+childMeasureHeight);
//        //将宽和高设置给child
//        child.measure(childMeasureWidth, childMeasureHeight);
//    }
}

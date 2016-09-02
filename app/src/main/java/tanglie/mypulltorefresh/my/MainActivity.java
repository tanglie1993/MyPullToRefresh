package tanglie.mypulltorefresh.my;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
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
        final PullToRefreshListView lv = (PullToRefreshListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strs));
        lv.setLoadingStartListener(new PullToRefreshListView.LoadingStartListener() {
            @Override
            public void onLoadingStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv.onLoadingFinish();
                    }
                }, 2000);
            }
        });
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
}

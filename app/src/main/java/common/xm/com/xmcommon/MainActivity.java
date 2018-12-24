package common.xm.com.xmcommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import common.xm.com.xmcommon.media.ActMedia;
import common.xm.com.xmcommon.tabar.ActTabbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActMedia.class));
            }
        });

        findViewById(R.id.btn_tabbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActTabbar.class));
            }
        });

//        ListView listView = findViewById(R.id.list);
//        List<? extends Map<String, ?>> data = new ArrayList();
//
//        HashMap<String, Object> map = new HashMap<String, Object>();
//
//        String[] from = new String[]{"cover", "integral", "date", "pb"};
//        int[] to = new int[]{R.id.iv_cover, R.id.tv_integral_num, R.id.tv_date, R.id.progress};
//        ListAdapter adapter = new SimpleAdapter(this, data, R.layout.signin_item, from, to);
//        listView.setAdapter(adapter);
//        listView.setSelection(0);
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
}

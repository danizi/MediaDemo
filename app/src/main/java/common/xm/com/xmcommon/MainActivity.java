package common.xm.com.xmcommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xm.lib.component.XmTabbarComponent;
import com.xm.lib.core.OneCore;
import com.xm.lib.core.TwoCore;
import com.xm.lib.ijkplayer.IJKPlayer;
import com.xm.lib.media.enum_.EnumViewType;
import com.xm.lib.media.XmMediaComponent;

import common.xm.com.xmcommon.media.ActMedia;
import common.xm.com.xmcommon.media.mediaview.MediaControlView;
import common.xm.com.xmcommon.media.mediaview.MediaLoading;
import common.xm.com.xmcommon.media.mediaview.MediaPreView;
import common.xm.com.xmcommon.tabar.ActTabbar;
import common.xm.com.xmcommon.tabar.MyFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    }
}

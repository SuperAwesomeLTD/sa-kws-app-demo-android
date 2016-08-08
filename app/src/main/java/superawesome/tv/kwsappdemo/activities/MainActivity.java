package superawesome.tv.kwsappdemo.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.adapters.ViewPagerAdapter;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.fragments.DocumentationFragment;
import superawesome.tv.kwsappdemo.fragments.FeatureFragment;
import superawesome.tv.kwsappdemo.fragments.PlatformFragment;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAApplication;


public class MainActivity extends AppCompatActivity {

    // fragment array
    private List<Fragment> fragments = Arrays.asList(new PlatformFragment(), new FeatureFragment(), new DocumentationFragment());
    private List<String> tabs = Arrays.asList("Platform", "Feature", "More");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set singleton context
        SAApplication.setSAApplicationContext(getApplicationContext());
        KWSSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (short i = 0; i < fragments.size(); i++){
            adapter.addFragment(fragments.get(i), tabs.get(i));
        }
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // setup filters
        IntentFilter filter = new IntentFilter("superawesome.tv.RECEIVED_BROADCAST");
        registerReceiver(new NotificationReceiver (this), filter);
    }

    private class NotificationReceiver extends BroadcastReceiver {

        private Context context = null;

        public NotificationReceiver  (Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String title = arg1.getExtras().getString("TITLE");
            String message = arg1.getExtras().getString("MESSAGE");
            SAAlert.getInstance().show(context, title, message, "Got it!", null, false, 0, null);
        }
    }
}

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

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.adapters.ViewPagerAdapter;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.fragments.DocumentationFragment;
import superawesome.tv.kwsappdemo.fragments.FeatureFragment;
import superawesome.tv.kwsappdemo.fragments.PlatformFragment;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAApplication;


public class MainActivity extends AppCompatActivity {

    // constants
    private final String KWS_API = "https://kwsapi.demo.superawesome.tv/v1/";

    // private vars
    private List<Fragment> fragments = Arrays.asList(new PlatformFragment(), new FeatureFragment(), new DocumentationFragment());
    private List<String> tabs = Arrays.asList("Platform", "Features", "More");
    private KWSModel localModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup SDK
        SAApplication.setSAApplicationContext(getApplicationContext());
        localModel = KWSSingleton.getInstance().getModel();
        if (localModel != null) {
            KWS.sdk.setup(this, localModel.token, KWS_API);
        }

        // UI stuff
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
        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_BROADCAST");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter3 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        registerReceiver(new NotificationReceiver (this), filter1);
        registerReceiver(new SignUpReceiver(), filter2);
        registerReceiver(new LogoutReceiver(), filter3);
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

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update local model
            localModel = KWSSingleton.getInstance().getModel();

            // setup SDK w/ new token
            KWS.sdk.setup(MainActivity.this, localModel.token, KWS_API);
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update local model (to null)
            localModel = KWSSingleton.getInstance().getModel();

            // de-setup SDK
            // KWS.sdk.desetup();
        }
    }
}

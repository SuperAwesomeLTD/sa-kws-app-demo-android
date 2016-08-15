package superawesome.tv.kwsappdemo.activities.main;

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
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.activities.main.documentation.DocumentationFragment;
import superawesome.tv.kwsappdemo.activities.main.features.FeatureFragment;
import superawesome.tv.kwsappdemo.activities.main.platform.PlatformFragment;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import superawesome.tv.kwsappdemo.aux.SmartReceiver;
import superawesome.tv.kwsappdemo.aux.SmartReceiverInterface;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAApplication;


public class MainActivity extends AppCompatActivity {

    // constants
    private final String KWS_API = "https://kwsapi.demo.superawesome.tv/v1/";

    // private vars
    private List<Fragment> fragments = Arrays.asList(new PlatformFragment(), new FeatureFragment(), new DocumentationFragment());
    private List<String> tabs = Arrays.asList("Platform", "Features", "More");
    private KWSModel localModel = null;
    private IntentFilter notifFilter = null;
    private IntentFilter signupFilter = null;
    private IntentFilter logoutFilter = null;

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
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        for (short i = 0; i < fragments.size(); i++){
            adapter.addFragment(fragments.get(i), tabs.get(i));
        }
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // setup filters
        notifFilter = new IntentFilter("superawesome.tv.RECEIVED_BROADCAST");
        registerReceiver(new SmartReceiver(this, (context, intent) -> {
            String title = intent.getExtras().getString("TITLE");
            String message = intent.getExtras().getString("MESSAGE");
            SAAlert.getInstance().show(context, title, message, "Got it!", null, false, 0, null);
        }), notifFilter);

        signupFilter = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        registerReceiver(new SmartReceiver(this, (context, intent) -> {
            localModel = KWSSingleton.getInstance().getModel();
            KWS.sdk.setup(MainActivity.this, localModel.token, KWS_API);
        }), signupFilter);

        logoutFilter = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        registerReceiver(new SmartReceiver(this, new SmartReceiverInterface() {
            @Override
            public void execute(Context context, Intent intent) {
                localModel = KWSSingleton.getInstance().getModel();
            }
        }), logoutFilter);
    }
}

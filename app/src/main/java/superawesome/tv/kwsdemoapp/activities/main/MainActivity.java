package superawesome.tv.kwsdemoapp.activities.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.activities.main.documentation.DocumentationFragment;
import superawesome.tv.kwsdemoapp.activities.main.features.FeaturesFragment;
import superawesome.tv.kwsdemoapp.activities.main.platform.PlatformFragment;

public class MainActivity extends BaseActivity {

    // private vars
    private List<Fragment> fragments = Arrays.asList(new PlatformFragment(), new FeaturesFragment(), new DocumentationFragment());
    private List<String> tabs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = Arrays.asList(
                getString(R.string.page_platform_title),
                getString(R.string.page_features_title),
                getString(R.string.page_documentation_title));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        for (short i = 0; i < fragments.size(); i++){
            adapter.addFragment(fragments.get(i), tabs.get(i));
        }
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

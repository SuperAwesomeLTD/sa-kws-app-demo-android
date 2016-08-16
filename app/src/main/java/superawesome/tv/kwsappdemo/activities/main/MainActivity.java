package superawesome.tv.kwsappdemo.activities.main;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.main.documentation.DocumentationFragment;
import superawesome.tv.kwsappdemo.activities.main.features.FeaturesFragment;
import superawesome.tv.kwsappdemo.activities.main.platform.PlatformFragment;

public class MainActivity extends AppCompatActivity {

    // private vars
    private List<Fragment> fragments = Arrays.asList(new PlatformFragment(), new FeaturesFragment(), new DocumentationFragment());
    private List<String> tabs = Arrays.asList("Platform", "Features", "More");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}

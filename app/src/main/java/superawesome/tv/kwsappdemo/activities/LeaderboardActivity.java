package superawesome.tv.kwsappdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.adapters.LeaderboardAdapter;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardActivity extends AppCompatActivity {

    // toolbar
    private android.support.v7.widget.Toolbar toolbar = null;
    private ListView leadersListView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Bundle bundle = getIntent().getExtras();
        List<KWSLeader> leaders = bundle.getParcelableArrayList("leaders");

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.leaderboardToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        leadersListView = (ListView) findViewById(R.id.leadersListView);
        LeaderboardAdapter adapter = new LeaderboardAdapter(this, R.layout.listitem_leaderboard, leaders);
        leadersListView.setAdapter(adapter);
    }
}

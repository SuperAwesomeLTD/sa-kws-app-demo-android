package superawesome.tv.kwsappdemo.activities.leader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import rx.functions.Action0;
import rx.functions.Action1;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.ViewModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

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

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.leaderboardToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context c = this;
        leadersListView = (ListView) findViewById(R.id.leadersListView);
        LeaderboardAdapter adapter = new LeaderboardAdapter(this, R.layout.listitem_leader_row);

        adapter.update(() -> {
            SAProgressDialog.getInstance().showProgress(c);
        }, () -> {
            SAProgressDialog.getInstance().hideProgress();
            leadersListView.setAdapter(adapter);
        }, () -> {
            SAProgressDialog.getInstance().hideProgress();
            SAAlert.getInstance().show(c, "Hey!", "There are no leaders to display right now!", "Got i1!", null, false, 0, null);
        });
    }
}

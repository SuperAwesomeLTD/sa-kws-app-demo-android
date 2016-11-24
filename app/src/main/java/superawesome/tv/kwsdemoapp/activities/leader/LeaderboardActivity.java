package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import rx.functions.Func1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.leaderboardToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context c = this;

        LeaderboardAdapter adapter = new LeaderboardAdapter(c);

        ListView leadersListView = (ListView) findViewById(R.id.leadersListView);
        leadersListView.setAdapter(adapter);

        LeaderboardSource source = new LeaderboardSource();
        source.getLeaderboard(LeaderboardActivity.this).
                doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(c)).
                map((Func1<KWSLeader, GenericViewModelInterface>) kwsLeader -> new LeaderRowViewModel(kwsLeader.rank, kwsLeader.score, kwsLeader.user)).
                toList().
                doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                subscribe(adapter::updateData, this::errorAlert);

    }

    private void errorAlert (Throwable err) {
        SAAlert.getInstance().show(this,
                getString(R.string.leader_popup_error_title),
                getString(R.string.leader_popup_error_message),
                getString(R.string.leader_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }
}

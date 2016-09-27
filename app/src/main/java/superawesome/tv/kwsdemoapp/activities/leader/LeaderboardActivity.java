package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.ViewModel;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context c = this;

        LeaderboardAdapter adapter = new LeaderboardAdapter(this);

        ListView leadersListView = (ListView) findViewById(R.id.leadersListView);
        leadersListView.setAdapter(adapter);

        LeaderboardSource source = new LeaderboardSource();
        source.getLeaderboard().
                doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(c)).
                map((Func1<KWSLeader, ViewModel>) kwsLeader -> new LeaderRowViewModel(kwsLeader.rank, kwsLeader.score, kwsLeader.user)).
                toList().
                doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                subscribe(viewModels -> {
                    adapter.updateData(viewModels);
                }, throwable -> {
                    SAAlert.getInstance().show(c,
                            getString(R.string.leader_popup_error_title),
                            getString(R.string.leader_popup_error_message),
                            getString(R.string.leader_popup_dismiss_button),
                            null,
                            false,
                            0,
                            null);
                }, () -> {
                    // do nothing
                });

    }
}
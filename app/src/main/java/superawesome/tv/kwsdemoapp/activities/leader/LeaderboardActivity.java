package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class LeaderboardActivity extends BaseActivity {

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
        ListView leadersListView = (ListView) findViewById(R.id.leadersListView);
        SAProgressDialog dialog = SAProgressDialog.getInstance();

        RxKWS.getLeaderBoard(c)
                .map(kwsLeader -> (GenericViewModel) new LeaderRowViewModel(kwsLeader.rank, kwsLeader.score, kwsLeader.user))
                .doOnSubscribe(() -> dialog.showProgress(c))
                .doOnError(throwable -> dialog.hideProgress())
                .doOnCompleted(dialog::hideProgress)
                .toList()
                .subscribe(leaderRowViewModels -> {

                    // form final list
                    List<GenericViewModel> list = new ArrayList<>();
                    list.add(new LeaderHeaderViewModel());
                    list.addAll(leaderRowViewModels);

                    RxDataSource.from(c, list)
                            .bindTo(leadersListView)
                            .customiseRow(R.layout.listitem_leader_header, LeaderHeaderViewModel.class, (genericViewModel, view) -> {

                                TextView rankTextView = (TextView) view.findViewById(R.id.leaderRank);
                                rankTextView.setText(c.getString(R.string.page_leader_header_rank));

                                TextView usernameTextView = (TextView) view.findViewById(R.id.leaderUsername);
                                usernameTextView.setText(c.getString(R.string.page_leader_header_username));

                                TextView scoreTextView = (TextView) view.findViewById(R.id.leaderPoints);
                                scoreTextView.setText(c.getString(R.string.page_leader_header_score));
                            })
                            .customiseRow(R.layout.listitem_leader_row, LeaderRowViewModel.class, (genericViewModel, view) -> {

                                LeaderRowViewModel row = (LeaderRowViewModel) genericViewModel;

                                TextView rankTextView = (TextView) view.findViewById(R.id.leaderRank);
                                rankTextView.setText(row.getRankTxt() != null ? row.getRankTxt() : c.getString(R.string.page_leader_row_rank_default));

                                TextView usernameTextView = (TextView) view.findViewById(R.id.leaderUsername);
                                usernameTextView.setText(row.getUsernameTxt() != null ? row.getUsernameTxt() : c.getString(R.string.page_leader_row_username_default));

                                TextView scoreTextView = (TextView) view.findViewById(R.id.leaderPoints);
                                scoreTextView.setText(row.getScoreTxt() != null ? row.getScoreTxt() : c.getString(R.string.page_leader_row_score_default));

                            })
                            .update();

                }, throwable -> {
                    // do nothing
                });
    }
}

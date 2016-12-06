package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

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
        ListView leadersListView = (ListView) findViewById(R.id.leadersListView);
        SAProgressDialog dialog = SAProgressDialog.getInstance();

        RxKWS.getLeaderboard(c)
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
                                rankTextView.setText(c.getString(R.string.leader_col_1_title));

                                TextView usernameTextView = (TextView) view.findViewById(R.id.leaderUsername);
                                usernameTextView.setText(c.getString(R.string.leader_col_2_title));

                                TextView scoreTextView = (TextView) view.findViewById(R.id.leaderPoints);
                                scoreTextView.setText(c.getString(R.string.leader_col_3_title));
                            })
                            .customiseRow(R.layout.listitem_leader_row, LeaderRowViewModel.class, (genericViewModel, view) -> {

                                LeaderRowViewModel row = (LeaderRowViewModel) genericViewModel;

                                TextView rankTextView = (TextView) view.findViewById(R.id.leaderRank);
                                rankTextView.setText(row.getRankTxt() != null ? row.getRankTxt() : c.getString(R.string.leader_col_1_default));

                                TextView usernameTextView = (TextView) view.findViewById(R.id.leaderUsername);
                                usernameTextView.setText(row.getUsernameTxt() != null ? row.getUsernameTxt() : c.getString(R.string.leader_col_2_default));

                                TextView scoreTextView = (TextView) view.findViewById(R.id.leaderPoints);
                                scoreTextView.setText(row.getScoreTxt() != null ? row.getScoreTxt() : c.getString(R.string.leader_col_3_default));

                            })
                            .update();

                }, throwable -> {
                    errorAlert();
                });
    }

    private void errorAlert () {
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

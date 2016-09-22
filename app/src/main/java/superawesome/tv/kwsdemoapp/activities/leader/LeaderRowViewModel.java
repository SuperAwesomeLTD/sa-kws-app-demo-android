package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class LeaderRowViewModel implements ViewModel {

    private String rankTxt = null;
    private String scoreTxt = null;
    private String usernameTxt = null;

    public LeaderRowViewModel(int rank, int score, String username) {
        rankTxt = Integer.toString(rank);
        scoreTxt = Integer.toString(score);
        usernameTxt = username;
    }

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_leader_row, null);
        }

        TextView rankTextView = (TextView) v.findViewById(R.id.leaderRank);
        TextView usernameTextView = (TextView) v.findViewById(R.id.leaderUsername);
        TextView scoreTextView = (TextView) v.findViewById(R.id.leaderPoints);

        if (rankTextView != null) {
            rankTextView.setText(rankTxt != null ? rankTxt : context.getString(R.string.leader_col_1_default));
        }
        if (usernameTextView != null) {
            usernameTextView.setText(usernameTxt != null ? usernameTxt : context.getString(R.string.leader_col_2_default));
        }
        if (scoreTextView != null) {
            scoreTextView.setText(scoreTxt != null ? scoreTxt : context.getString(R.string.leader_col_3_default));
        }

        return v;
    }
}

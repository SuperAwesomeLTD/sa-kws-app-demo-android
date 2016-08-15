package superawesome.tv.kwsappdemo.activities.leader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class LeaderRowViewModel implements ViewModel {

    private String rankTxt = "0";
    private String scoreTxt = "0";
    private String usernameTxt = "unknown";

    public LeaderRowViewModel(int rank, int score, String username) {
        rankTxt = Integer.toString(rank);
        scoreTxt = Integer.toString(score);
        if (username != null) {
            usernameTxt = username;
        }
    }

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_leaderrow, null);
        }

        TextView rankTextView = (TextView) v.findViewById(R.id.leaderRank);
        TextView usernameTextView = (TextView) v.findViewById(R.id.leaderUsername);
        TextView scoreTextView = (TextView) v.findViewById(R.id.leaderPoints);

        if (rankTextView != null) {
            rankTextView.setText(rankTxt);
        }
        if (usernameTextView != null) {
            usernameTextView.setText(usernameTxt);
        }
        if (scoreTextView != null) {
            scoreTextView.setText(scoreTxt);
        }

        return v;
    }
}

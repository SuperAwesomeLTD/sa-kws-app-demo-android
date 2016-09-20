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
public class LeaderHeaderViewModel implements ViewModel {
    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_leader_header, null);
        }

        TextView rankTextView = (TextView) v.findViewById(R.id.leaderRank);
        TextView usernameTextView = (TextView) v.findViewById(R.id.leaderUsername);
        TextView scoreTextView = (TextView) v.findViewById(R.id.leaderPoints);

        if (rankTextView != null) {
            rankTextView.setText(context.getString(R.string.leader_col_1_title));
        }
        if (usernameTextView != null) {
            usernameTextView.setText(context.getString(R.string.leader_col_2_title));
        }
        if (scoreTextView != null) {
            scoreTextView.setText(context.getString(R.string.leader_col_3_title));
        }

        return v;
    }
}

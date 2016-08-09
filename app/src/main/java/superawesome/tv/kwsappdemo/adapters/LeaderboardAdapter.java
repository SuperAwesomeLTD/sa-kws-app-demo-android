package superawesome.tv.kwsappdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import superawesome.tv.kwsappdemo.R;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardAdapter extends ArrayAdapter<KWSLeader> {

    public LeaderboardAdapter (Context context, int resource, List<KWSLeader> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = getContext();
        KWSLeader item = getItem(position);

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_leaderboard, null);
        }

        if (item != null) {
            TextView rankTextView = (TextView) v.findViewById(R.id.leaderRank);
            TextView usernameTextView = (TextView) v.findViewById(R.id.leaderUsername);
            TextView pointsTextView = (TextView) v.findViewById(R.id.leaderPoints);

            if (rankTextView != null) {
                rankTextView.setText(item.rank + "");
            }
            if (usernameTextView != null) {
                usernameTextView.setText(item.user + "");
            }
            if (pointsTextView != null) {
                pointsTextView.setText(item.score + "");
            }
        }

        return v;
    }
}

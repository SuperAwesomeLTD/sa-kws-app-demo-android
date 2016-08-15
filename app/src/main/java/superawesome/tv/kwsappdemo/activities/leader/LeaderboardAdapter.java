package superawesome.tv.kwsappdemo.activities.leader;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetLeaderboardInterface;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.DataSource;
import superawesome.tv.kwsappdemo.aux.DataSourceInterface;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardAdapter extends ArrayAdapter<KWSLeader> implements DataSource {

    // internal list
    private List<ViewModel> leaders = null;

    public LeaderboardAdapter (Context context, int resource) {
        super(context, resource);
    }

    @Override public void update(DataSourceInterface start, DataSourceInterface success, DataSourceInterface error) {
        if (start != null) start.event();

        KWS.sdk.getLeaderBoard(list -> {
            leaders = new ArrayList<>();
            leaders.add(new LeaderHeaderViewModel());
            for (KWSLeader leader : list) {
                leaders.add(new LeaderRowViewModel(leader.rank, leader.score, leader.user));
            }
            if (leaders.size() > 1) {
                if (success != null) success.event();
            } else {
                if (error != null) error.event();
            }
        });
    }

    @Override public int getItemViewType(int position) { return position == 0 ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

    @Override public int getCount() {
        return leaders.size();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        return leaders.get(position).representationAsRow(getContext(), convertView);
    }
}

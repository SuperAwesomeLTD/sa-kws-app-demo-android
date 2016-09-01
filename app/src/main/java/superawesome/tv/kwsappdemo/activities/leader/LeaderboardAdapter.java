package superawesome.tv.kwsappdemo.activities.leader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsappdemo.aux.GBAdapter;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardAdapter extends ArrayAdapter<ViewModel> implements GBAdapter {

    // internal list
    private List<ViewModel> leaders = new ArrayList<>();

    public LeaderboardAdapter (Context context) {
        super(context, 0);
    }

    @Override
    public void updateData (List<ViewModel> newLeaders) {
        leaders = new ArrayList<>();
        leaders.add(new LeaderHeaderViewModel());
        leaders.addAll(newLeaders);
        notifyDataSetChanged();
    }

    @Override public int getItemViewType(int position) { return position == 0 ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

    @Override public int getCount() { return leaders.size(); }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        return leaders.get(position).representationAsRow(getContext(), convertView);
    }
}

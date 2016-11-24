package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsdemoapp.aux.GenericAdapter;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardAdapter extends GenericAdapter {

    public LeaderboardAdapter (Context context) {
        super(context);
    }

    @Override public void updateData (List<GenericViewModelInterface> newLeaders) {
        data = new ArrayList<>();
        data.add(new LeaderHeaderViewModel());
        data.addAll(newLeaders);
        notifyDataSetChanged();
    }

    @Override public int getItemViewType(int position) { return position == 0 ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

}

package superawesome.tv.kwsappdemo.activities.user;

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
public class UserAdapter extends ArrayAdapter<ViewModel> implements GBAdapter {

    private List<ViewModel> items = new ArrayList<>();

    public UserAdapter (Context context) {
        super(context, 0);
    }

    @Override
    public void updateData (List<ViewModel> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    @Override public int getCount() { return items.size(); }

    @Override public int getItemViewType(int position) { return items.get(position) instanceof UserHeaderViewModel ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).representationAsRow(getContext(), convertView);
    }
}

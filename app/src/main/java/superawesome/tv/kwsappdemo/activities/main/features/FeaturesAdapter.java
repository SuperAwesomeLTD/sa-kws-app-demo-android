package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsappdemo.aux.GBAdapter;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesAdapter extends ArrayAdapter<ViewModel> implements GBAdapter {

    private List<ViewModel> rows = new ArrayList<>();

    public FeaturesAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public void updateData(List<ViewModel> newData) {
        rows = newData;
        notifyDataSetChanged();
    }

    @Override public int getViewTypeCount() { return 6; }

    @Override public int getItemViewType(int position) { return position; }

    @Override public int getCount() { return rows.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rows.get(position).representationAsRow(getContext(), convertView);
    }
}

package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsappdemo.aux.DataSource;
import superawesome.tv.kwsappdemo.aux.DataSourceInterface;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesAdapter extends ArrayAdapter<ViewModel> implements DataSource {

    private List<ViewModel> rows = null;

    public FeaturesAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void update(DataSourceInterface start, DataSourceInterface success, DataSourceInterface error) {
        start.event();

        rows = new ArrayList<>();
        rows.add(new FeaturesAuthRowViewModel());
        rows.add(new FeaturesNotifRowViewModel());
        rows.add(new FeaturesPermRowViewModel());
        rows.add(new FeaturesEventsRowViewModel());

        success.event();
    }

    @Override public int getViewTypeCount() { return 4; }

    @Override public int getItemViewType(int position) { return position; }

    @Override public int getCount() { return rows.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rows.get(position).representationAsRow(getContext(), convertView);
    }
}

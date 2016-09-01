package superawesome.tv.kwsappdemo.activities.appdata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsappdemo.aux.GBAdapter;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class AppDataAdapter extends ArrayAdapter<ViewModel> implements GBAdapter {

    private List<ViewModel> items = new ArrayList<>();

    public AppDataAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public void updateData (List<ViewModel> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).representationAsRow(getContext(), convertView);
    }
}

package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import superawesome.tv.kwsdemoapp.aux.GBAdapter;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class GetAppDataAdapter extends ArrayAdapter<ViewModel> implements GBAdapter {

    private List<ViewModel> items = new ArrayList<>();

    public GetAppDataAdapter(Context context) {
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

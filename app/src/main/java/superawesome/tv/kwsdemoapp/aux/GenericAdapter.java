package superawesome.tv.kwsdemoapp.aux;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapter extends ArrayAdapter<GenericViewModelInterface> implements GenericAdapterInterface {

    protected List<GenericViewModelInterface> data = new ArrayList<>();

    public GenericAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public void updateData(List<GenericViewModelInterface> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewModelInterface getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return data.get(position).representationAsRow(getContext(), convertView, parent);
    }
}

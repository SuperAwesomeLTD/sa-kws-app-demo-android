package superawesome.tv.kwsappdemo.activities.appdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class AppDataRowViewModel implements ViewModel {

    private String name;
    private String value;

    public AppDataRowViewModel(String name, int value) {
        this.name = name != null ? name : "Unknown";
        this.value = "" + value;
    }

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_appdata_row, null);
        }

        TextView nameTextView = (TextView) v.findViewById(R.id.appDataItemName);
        TextView valueTextView = (TextView) v.findViewById(R.id.appDataItemValue);

        if (nameTextView != null) {
            nameTextView.setText(name);
        }
        if (valueTextView != null) {
            valueTextView.setText(value);
        }

        return v;
    }
}

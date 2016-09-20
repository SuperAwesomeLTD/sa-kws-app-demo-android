package superawesome.tv.kwsappdemo.activities.getappdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class GetAppDataRowViewModel implements ViewModel {

    private String name;
    private String value;

    public GetAppDataRowViewModel(String name, int value) {
        this.name = name;
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
            nameTextView.setText(name != null ? name : context.getString(R.string.get_app_data_col_unknown_username));
        }
        if (valueTextView != null) {
            valueTextView.setText(value);
        }

        return v;
    }
}

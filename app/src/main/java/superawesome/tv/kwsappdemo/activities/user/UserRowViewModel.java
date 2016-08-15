package superawesome.tv.kwsappdemo.activities.user;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class UserRowViewModel implements ViewModel {

    private String itemTxt = null;
    private String valueTxt = null;
    private int valueColor = Color.BLACK;

    public UserRowViewModel (String item, Object value) {
        itemTxt = item;
        if (value != null) {
            valueTxt = "" + value;
            if (value instanceof  Integer && !item.equals("ID")) {
                if ((Integer)value > 0) {
                    valueColor = Color.rgb(57, 97, 4);
                } else {
                    valueColor = Color.rgb(97,4,4);
                }
            }
            if (valueTxt.equals("")) {
                valueTxt = "undefined";
                valueColor = Color.LTGRAY;
            }
        } else {
            valueTxt = "undefined";
            valueColor = Color.LTGRAY;
        }
    }

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_userrow, null);
        }

        TextView itemTitleTextView = (TextView) v.findViewById(R.id.UserItemTitle);
        TextView itemValueTextView = (TextView) v.findViewById(R.id.UserItemValue);

        if (itemTitleTextView != null) {
            itemTitleTextView.setText(itemTxt);
        }
        if (itemValueTextView != null) {
            itemValueTextView.setText(valueTxt);
            itemValueTextView.setTextColor(valueColor);
        }

        return v;
    }
}

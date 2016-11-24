package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class UserRowViewModel implements GenericViewModelInterface {

    private String itemTxt = null;
    private Object value = null;
    private String valueTxt = null;
    private int valueColor = Color.BLACK;

    public UserRowViewModel (String item, Object value) {
        itemTxt = item;
        this.value = value;
    }

    private void processValue(Context c, String item, Object value) {
        if (value != null) {
            valueTxt = "" + value;
            if (value instanceof Integer && !item.equals("ID")) {
                if ((Integer)value > 0) {
                    valueColor = Color.rgb(57, 97, 4);
                } else {
                    valueColor = Color.rgb(97,4,4);
                }
            }
            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    valueColor = Color.rgb(57, 97, 4);
                    valueTxt = c.getString(R.string.user_row_value_true);
                } else {
                    valueColor = Color.rgb(97, 4, 4);
                    valueTxt = c.getString(R.string.user_row_value_false);
                }
            }
            if (valueTxt.equals("")) {
                valueTxt = c.getString(R.string.user_row_value_undefined);
                valueColor = Color.LTGRAY;
            }
        } else {
            valueTxt = c.getString(R.string.user_row_value_undefined);
            valueColor = Color.LTGRAY;
        }
    }

    @Override
    public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_user_row, parent, false);
        }

        TextView itemTitleTextView = (TextView) v.findViewById(R.id.UserItemTitle);
        TextView itemValueTextView = (TextView) v.findViewById(R.id.UserItemValue);

        if (itemTitleTextView != null) {
            itemTitleTextView.setText(itemTxt);
        }
        if (itemValueTextView != null) {
            processValue(context, itemTxt, value);
            itemValueTextView.setText(valueTxt);
            itemValueTextView.setTextColor(valueColor);
        }

        return v;
    }
}

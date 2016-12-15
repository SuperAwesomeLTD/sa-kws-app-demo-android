package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;
import android.graphics.Color;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;

class UserRowViewModel extends GenericViewModel {

    private String itemTxt = null;
    private Object value = null;
    private String valueTxt = null;
    private int valueColor = Color.BLACK;

    private UserRowViewModel(String item, Object value) {
        itemTxt = item;
        this.value = value;
    }

    static UserRowViewModel create (Context context, int id, Object value) {
        String item = context.getString(id);
        UserRowViewModel viewModel = new UserRowViewModel(item, value);
        viewModel.processValue(context);
        return viewModel;
    }

    void processValue(Context c) {
        if (value != null) {
            valueTxt = "" + value;
            if (value instanceof Integer && !itemTxt.equals("ID")) {
                if ((Integer)value > 0) {
                    valueColor = Color.rgb(57, 97, 4);
                } else {
                    valueColor = Color.rgb(97,4,4);
                }
            }
            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    valueColor = Color.rgb(57, 97, 4);
                    valueTxt = c.getString(R.string.page_user_row_true_value);
                } else {
                    valueColor = Color.rgb(97, 4, 4);
                    valueTxt = c.getString(R.string.page_user_row_false_value);
                }
            }
            if (valueTxt.equals("")) {
                valueTxt = c.getString(R.string.page_user_row_undefined_value);
                valueColor = Color.LTGRAY;
            }
        } else {
            valueTxt = c.getString(R.string.page_user_row_undefined_value);
            valueColor = Color.LTGRAY;
        }
    }

    String getItemTxt() {
        return itemTxt;
    }

    public Object getValue() {
        return value;
    }

    String getValueTxt() {
        return valueTxt;
    }

    int getValueColor() {
        return valueColor;
    }
}

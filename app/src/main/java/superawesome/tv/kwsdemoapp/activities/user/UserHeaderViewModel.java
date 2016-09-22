package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class UserHeaderViewModel implements ViewModel {

    private String headerTxt = null;

    public UserHeaderViewModel (String header) {
        this.headerTxt = header;
    }

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_user_header, null);
        }

        TextView headerTextView = (TextView) v.findViewById(R.id.UserHeader);
        if (headerTextView != null) {
            headerTextView.setText(headerTxt);
        }

        return v;
    }
}

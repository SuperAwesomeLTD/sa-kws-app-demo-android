package superawesome.tv.kwsappdemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.models.UserDetailModelType;
import superawesome.tv.kwsappdemo.models.UserDetailsModel;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserAdapter extends ArrayAdapter<UserDetailsModel> {

    private List<UserDetailsModel> items = null;

    public UserAdapter (Context context, int resource, List<UserDetailsModel> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type.ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = getContext();
        UserDetailsModel item = getItem(position);

        if (v == null) {
            if (item.type == UserDetailModelType.HEADER)
                v = LayoutInflater.from(context).inflate(R.layout.listitem_user_header, null);
            else
                v = LayoutInflater.from(context).inflate(R.layout.listitem_user_detail, null);
        }

        if (item != null) {
            TextView headerTextView = (TextView) v.findViewById(R.id.UserHeader);
            TextView itemTitleTextView = (TextView) v.findViewById(R.id.UserItemTitle);
            TextView itemValueTextView = (TextView) v.findViewById(R.id.UserItemValue);

            if (headerTextView != null) {
                headerTextView.setText(item.headerText);
            }
            if (itemTitleTextView != null) {
                itemTitleTextView.setText(item.itemTitle);
            }
            if (itemValueTextView != null) {
                itemValueTextView.setText(item.itemValue);
            }
        }

        return v;
    }
}

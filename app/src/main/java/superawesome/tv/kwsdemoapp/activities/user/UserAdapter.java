package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;

import superawesome.tv.kwsdemoapp.aux.GenericAdapter;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserAdapter extends GenericAdapter {

    public UserAdapter (Context context) {
        super(context);
    }

    @Override public int getItemViewType(int position) { return data.get(position) instanceof UserHeaderViewModel ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }
}

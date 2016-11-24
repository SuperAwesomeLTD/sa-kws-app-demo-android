package superawesome.tv.kwsdemoapp.activities.main.features;

import android.content.Context;

import superawesome.tv.kwsdemoapp.aux.GenericAdapter;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesAdapter extends GenericAdapter {

    public FeaturesAdapter(Context context) {
        super(context);
    }

    @Override public int getViewTypeCount() { return 6; }

    @Override public int getItemViewType(int position) { return position; }
}

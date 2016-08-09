package superawesome.tv.kwsappdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsappdemo.R;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FeatureFragment extends Fragment {

    // constructor
    public FeatureFragment () {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KWS.sdk.setApplicationContext(getContext().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get views
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        return view;
    }
}

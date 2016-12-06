package superawesome.tv.kwsdemoapp.activities.main.platform;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseFragment;

public class PlatformFragment extends BaseFragment {

    // constructor
    public PlatformFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_platform, container, false);
        Button openKWS = (Button) view.findViewById(R.id.openKWSWebsite);
        RxView.clicks(openKWS).subscribe(aVoid -> onClick());
        return view;
    }

    private void onClick () {
        String KWSURL = "http://www.superawesome.tv/en/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KWSURL));
        getContext().startActivity(browserIntent);
    }
}

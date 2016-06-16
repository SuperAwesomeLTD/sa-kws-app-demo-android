package superawesome.tv.kwsappdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class PlatformFragment extends Fragment {

    // private
    private Button openKWS;
    private final String KWSURL = "http://www.superawesome.tv/en/";

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
        openKWS = (Button) view.findViewById(R.id.openKWSWebsite);
        openKWS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KWSURL));
                getContext().startActivity(browserIntent);
            }
        });
        return view;
    }
}
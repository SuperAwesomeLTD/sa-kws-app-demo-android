package superawesome.tv.kwsdemoapp.activities.main.documentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseFragment;

public class DocumentationFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentation, container, false);
        WebView documentation = (WebView) view.findViewById(R.id.DocumentationWebView);
        documentation.loadUrl("https://developers.superawesome.tv/");
        documentation.getSettings().setJavaScriptEnabled(true);
        documentation.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        return view;
    }
}

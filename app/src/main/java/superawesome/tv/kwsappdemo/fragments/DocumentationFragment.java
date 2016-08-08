package superawesome.tv.kwsappdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import superawesome.tv.kwsappdemo.R;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class DocumentationFragment extends Fragment {

    // webview
    private WebView documentation;

    // constructor
    public DocumentationFragment () {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentation, container, false);

        documentation = (WebView) view.findViewById(R.id.DocumentationWebView);
        documentation.loadUrl("https://developers.superawesome.tv/");
        documentation.getSettings().setJavaScriptEnabled(true);
        documentation.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        return view;
    }
}

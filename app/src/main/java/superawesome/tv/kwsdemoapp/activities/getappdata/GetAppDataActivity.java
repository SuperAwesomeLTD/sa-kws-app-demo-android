package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.activities.setappdata.SetAppDataActivity;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SALoadScreen;

public class GetAppDataActivity extends BaseActivity {

    // private constants
    private static final int SET_REQ_CODE = 111;

    private Observable <List<GetAppDataRowViewModel>> observable = null;
    private RxDataSource <GetAppDataRowViewModel> source = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdata_get);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.appDataGetToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Button addButton = (Button) findViewById(R.id.appDataAdd);
        RxView.clicks(addButton).subscribe(aVoid -> startActivity());

        Context c = this;
        ListView appDataListView = (ListView) findViewById(R.id.appDataListView);
        SALoadScreen dialog = SALoadScreen.getInstance();

        observable = RxKWS.getAppData(c)
                .map(kwsAppData -> new GetAppDataRowViewModel(kwsAppData.name, kwsAppData.value))
                .doOnSubscribe(() -> dialog.show(c))
                .doOnError(throwable -> dialog.hide())
                .doOnCompleted(dialog::hide)
                .toList()
                .share();

        observable.subscribe(getAppDataRowViewModels -> {

            source = RxDataSource.create(c);
            source
                    .bindTo(appDataListView)
                    .customiseRow(R.layout.listitem_appdata_row, GetAppDataRowViewModel.class, (viewModel, view) -> {

                        TextView nameTextView = (TextView) view.findViewById(R.id.appDataItemName);
                        nameTextView.setText(viewModel.getName() != null ? viewModel.getName() : "");

                        TextView valueTextView = (TextView) view.findViewById(R.id.appDataItemValue);
                        valueTextView.setText(viewModel.getValue());

                    })
                    .update(getAppDataRowViewModels);

        }, throwable -> {
            errorAlert();
        });

        setOnActivityResult((requestCode, resultCode, intent) -> {
            observable.subscribe(getAppDataRowViewModels -> {
                source.update(getAppDataRowViewModels);
            });
        });
    }

    private void startActivity () {
        Intent setAppData = new Intent(GetAppDataActivity.this, SetAppDataActivity.class);
        startActivityForResult(setAppData, SET_REQ_CODE);
    }

    private void errorAlert () {
        SAAlert.getInstance().show(GetAppDataActivity.this,
                getString(R.string.page_getappdata_popup_error_network_title),
                getString(R.string.page_getappdata_popup_error_network_message),
                getString(R.string.page_getappdata_popup_error_network_ok_button),
                null,
                false,
                0,
                null);
    }
}

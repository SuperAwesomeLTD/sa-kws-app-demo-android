package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import gabrielcoman.com.rxdatasource.RxDataSource;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.setappdata.SetAppDataActivity;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class GetAppDataActivity extends AppCompatActivity {

    // private constants
    private static final int SET_REQ_CODE = 111;

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


        reloadData();
    }

    private void reloadData () {

        Context c = this;
        ListView appDataListView = (ListView) findViewById(R.id.appDataListView);
        SAProgressDialog dialog = SAProgressDialog.getInstance();

        RxKWS.getAppData(c)
                .map(kwsAppData -> new GetAppDataRowViewModel(kwsAppData.name, kwsAppData.value))
                .doOnSubscribe(() -> dialog.showProgress(c))
                .doOnError(throwable -> dialog.hideProgress())
                .doOnCompleted(dialog::hideProgress)
                .toList()
                .subscribe(getAppDataRowViewModels -> {

                    RxDataSource.from(c, getAppDataRowViewModels)
                            .bindTo(appDataListView)
                            .customiseRow(R.layout.listitem_appdata_row, GetAppDataRowViewModel.class, (viewModel, view) -> {

                                TextView nameTextView = (TextView) view.findViewById(R.id.appDataItemName);
                                nameTextView.setText(viewModel.getName() != null ? viewModel.getName() : c.getString(R.string.get_app_data_col_unknown_username));

                                TextView valueTextView = (TextView) view.findViewById(R.id.appDataItemValue);
                                valueTextView.setText(viewModel.getValue());

                            })
                            .update();

                }, throwable -> {
                    errorAlert();
                });
    }

    private void startActivity () {
        Intent setAppData = new Intent(GetAppDataActivity.this, SetAppDataActivity.class);
        startActivityForResult(setAppData, SET_REQ_CODE);
    }

    private void errorAlert () {
        SAAlert.getInstance().show(GetAppDataActivity.this,
                getString(R.string.get_app_data_popup_error_title),
                getString(R.string.get_app_data_popup_error_message),
                getString(R.string.get_app_data_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SET_REQ_CODE && resultCode == RESULT_OK) {
            reloadData();
        }
    }
}

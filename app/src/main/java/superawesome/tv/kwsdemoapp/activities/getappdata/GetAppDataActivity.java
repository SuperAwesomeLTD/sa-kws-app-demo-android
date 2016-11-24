package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import rx.Observable;
import rx.functions.Func1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.setappdata.SetAppDataActivity;
import superawesome.tv.kwsdemoapp.aux.GenericAdapter;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class GetAppDataActivity extends AppCompatActivity {

    // private constants
    private static final int SET_REQ_CODE = 111;

    // private vars
    Observable<List<GenericViewModelInterface>> appDataObservable = null;
    GenericAdapter adapter = null;
    GetAppDataSource source = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdata_get);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.appDataGetToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Button addButton = (Button) findViewById(R.id.appDataAdd);
        ListView appDataListView = (ListView) findViewById(R.id.appDataListView);

        adapter = new GenericAdapter(this);
        appDataListView.setAdapter(adapter);

        source = new GetAppDataSource();
        appDataObservable = source.getAppData(GetAppDataActivity.this).
                doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(GetAppDataActivity.this)).
                map((Func1<KWSAppData, GenericViewModelInterface>) kwsAppData -> new GetAppDataRowViewModel(kwsAppData.name, kwsAppData.value)).
                toList().
                doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress());

        appDataObservable.subscribe(rows -> adapter.updateData(rows), throwable -> errorAlert());

        RxView.clicks(addButton).subscribe(aVoid -> startActivity());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SET_REQ_CODE && resultCode == RESULT_OK) {
            appDataObservable.subscribe(rows -> adapter.updateData(rows), throwable -> errorAlert());
        }
    }

    private void startActivity () {
        Intent setappdata = new Intent(GetAppDataActivity.this, SetAppDataActivity.class);
        startActivityForResult(setappdata, SET_REQ_CODE);
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
}

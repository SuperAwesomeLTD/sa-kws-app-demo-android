package superawesome.tv.kwsdemoapp.activities.setappdata;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SALoadScreen;

public class SetAppDataActivity extends BaseActivity {

    private SetAppDataModel currentModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdata_set);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.appDataSetToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context context = SetAppDataActivity.this;
        SALoadScreen dialog = SALoadScreen.getInstance();

        TextView nameTextView = (TextView) findViewById(R.id.appDataName);
        TextView valueTextView = (TextView) findViewById(R.id.appDataValue);
        Button submitButton = (Button) findViewById(R.id.appDataSubmit);

        Observable<String> rxName = RxTextView.textChanges(nameTextView).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxValue = RxTextView.textChanges(valueTextView).
                map(charSequence -> charSequence.toString().trim());

        Observable.combineLatest(rxName, rxValue, SetAppDataModel::new).
                doOnNext(setAppDataModel -> currentModel = setAppDataModel).
                map(SetAppDataModel::isValid).
                subscribe(submitButton::setEnabled);

        RxView.clicks(submitButton).subscribe(aVoid -> {
            RxKWS.submitData(context, currentModel.getName(), currentModel.getValue()).
                    doOnSubscribe(() -> dialog.show(context)).
                    doOnError((throwable)-> dialog.hide()).
                    doOnCompleted(dialog::hide).
                    subscribe(this::finishOK, this::errorAlert);
        });
    }

    private void finishOK (Boolean finish) {
        setResult(RESULT_OK);
        finish();
    }

    private void errorAlert (Throwable error) {
        SAAlert.getInstance().show(SetAppDataActivity.this,
                getString(R.string.page_setappdata_popup_error_network_title),
                getString(R.string.page_setappdata_popup_error_network_message),
                getString(R.string.page_setappdata_popup_error_network_ok_button),
                null,
                false,
                0,
                null);
    }
}

package superawesome.tv.kwsdemoapp.activities.setappdata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import superawesome.tv.kwsdemoapp.R;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class SetAppDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdata_set);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.appDataSetToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView nameTextView = (TextView) findViewById(R.id.appDataName);
        TextView valueTextView = (TextView) findViewById(R.id.appDataValue);
        valueTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button submitButton = (Button) findViewById(R.id.appDataSubmit);

        Observable<String> rxName = RxTextView.textChanges(nameTextView).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxValue = RxTextView.textChanges(valueTextView).
                map(charSequence -> charSequence.toString().trim());

        Observable<SetAppDataModel> rxModel = Observable.combineLatest(rxName, rxValue, SetAppDataModel::new);

        Observable<Void> rxButton = RxView.clicks(submitButton);

        rxModel.map(SetAppDataModel::isValid).
                subscribe(submitButton::setEnabled);

        Observable.combineLatest(rxModel, rxButton, (setAppDataModel, aVoid) -> setAppDataModel).
                subscribe(model -> {
                    submitData(model.getName(), model.getValue()).
                            doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(SetAppDataActivity.this)).
                            doOnError((throwable)-> SAProgressDialog.getInstance().hideProgress()).
                            doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                            subscribe(aBoolean -> {
                                setResult(RESULT_OK);
                                finish();
                            }, throwable -> {
                                SAAlert.getInstance().show(SetAppDataActivity.this,
                                        getString(R.string.add_app_data_popup_error_title),
                                        getString(R.string.add_app_data_error_message),
                                        getString(R.string.add_app_data_popup_dismiss_button),
                                        null,
                                        false,
                                        0,
                                        null);
                            });
                });
    }

    private Observable<Boolean> submitData(final String name, final Integer value) {
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            KWS.sdk.setAppData(name, value, b -> {
                if (b) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception());
                }
            });
        });
    }
}

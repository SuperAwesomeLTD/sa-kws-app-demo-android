package superawesome.tv.kwsappdemo.activities.appdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.services.kws.KWSSetAppDataInterface;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import superawesome.tv.kwsappdemo.R;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class SetAppDataActivity extends AppCompatActivity {

    private String name = null;
    private Integer value = 0;

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


        RxTextView.textChanges(nameTextView).
                map(charSequence -> charSequence.toString()).
                map(s -> s.equals("") ? null : s).
                subscribe(s -> {
                    name = s;
                });
        RxTextView.textChanges(valueTextView).
                map(charSequence -> charSequence.toString()).
                map(s -> s == null  || s.equals("") ? -Integer.MAX_VALUE : Integer.parseInt(s)).
                subscribe(integer -> {
                    value = integer;
                });

        RxView.clicks(submitButton).
                subscribe(aVoid -> {
                    if (name != null && value != -Integer.MAX_VALUE) {

                        submitData(name, value).
                                doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(SetAppDataActivity.this)).
                                doOnError((throwable)-> SAProgressDialog.getInstance().hideProgress()).
                                doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                                subscribe(aBoolean -> {
                                    // go back and send an OK result
                                    setResult(RESULT_OK);
                                    finish();
                                }, throwable -> {
                                    SAAlert.getInstance().show(SetAppDataActivity.this, "Hey!", "Could not save name | value pair.", "Ok!", null, false, 0, null);
                                });

                    } else {
                        SAAlert.getInstance().show(SetAppDataActivity.this, "Hey!", "Your key must not be empty", "Got it!", null, false, 0, null);
                    }
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

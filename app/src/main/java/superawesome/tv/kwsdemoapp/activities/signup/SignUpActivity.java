package superawesome.tv.kwsdemoapp.activities.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.KWSSingleton;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class SignUpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // set toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.signinToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // get edits
        EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        EditText password1Edit = (EditText) findViewById(R.id.password1Edit);
        EditText password2Edit = (EditText) findViewById(R.id.password2Edit);
        EditText yearEdit = (EditText) findViewById(R.id.yearEdit);
        EditText monthEdit = (EditText) findViewById(R.id.monthEdit);
        EditText dayEdit = (EditText) findViewById(R.id.dayEdit);
        Button submit = (Button) findViewById(R.id.RegisterUserButton);

        Observable<String> rxUsername = RxTextView.textChanges(usernameEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxPassword1 = RxTextView.textChanges(password1Edit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxPassword2 = RxTextView.textChanges(password2Edit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxYear = RxTextView.textChanges(yearEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxMonth = RxTextView.textChanges(monthEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxDay = RxTextView.textChanges(dayEdit).
                map(charSequence -> charSequence.toString().trim());

        Observable<SignUpModel> rxModel = Observable.combineLatest(rxUsername, rxPassword1, rxPassword2, rxYear, rxMonth, rxDay, SignUpModel::new);

        Observable<Void> rxButton = RxView.clicks(submit);

        rxModel.map(SignUpModel::isValid).
                subscribe(submit::setEnabled);

        rxButton.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });

        Observable.combineLatest(rxModel, rxButton, (signUpModel, aVoid) -> signUpModel).
                subscribe(model -> {
                    KWSSingleton.getInstance().loginUser(model.getUsername(), model.getPassword(), model.getDate()).
                            doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(SignUpActivity.this)).
                            doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                            doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                            subscribe(aBoolean -> {
                                // do nothing
                            }, throwable -> {
                                SAAlert.getInstance().show(SignUpActivity.this,
                                        getString(R.string.sign_up_popup_error_title),
                                        getString(R.string.sign_up_popup_error_message),
                                        getString(R.string.sign_up_popup_dismiss_button),
                                        null,
                                        false,
                                        0,
                                        null);
                            }, () -> {
                                UniversalNotifier.postNotification("RECEIVED_SIGNUP");
                                onBackPressed();
                            });
                });
    }
}

package superawesome.tv.kwsdemoapp.activities.signup;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func7;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class SignUpActivity extends AppCompatActivity {

    private SignUpModel currentModel;

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
        EditText parentEmailEdit = (EditText) findViewById(R.id.parentEmail);
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
        Observable<String> rxParentEmail = RxTextView.textChanges(parentEmailEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxYear = RxTextView.textChanges(yearEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxMonth = RxTextView.textChanges(monthEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxDay = RxTextView.textChanges(dayEdit).
                map(charSequence -> charSequence.toString().trim());

        Observable.combineLatest(rxUsername, rxPassword1, rxPassword2, rxParentEmail, rxYear, rxMonth, rxDay, SignUpModel::new).
                doOnNext(signUpModel -> currentModel = signUpModel).
                map(SignUpModel::isValid).
                subscribe(submit::setEnabled);

        RxView.focusChanges(usernameEdit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                usernameEdit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isUserOK()) {
                    usernameEdit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    usernameEdit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(password1Edit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                password1Edit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isPassword1OK()) {
                    password1Edit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    password1Edit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(password2Edit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                password2Edit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isPassword2OK() || !currentModel.arePasswordsSame()) {
                    password2Edit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    password2Edit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(parentEmailEdit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                parentEmailEdit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isParentEmailOK()) {
                    parentEmailEdit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    parentEmailEdit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(yearEdit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                yearEdit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isYearOK()) {
                    yearEdit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    yearEdit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(monthEdit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                monthEdit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isMonthOK()) {
                    monthEdit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    monthEdit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        RxView.focusChanges(dayEdit).skip(1).subscribe(aBoolean -> {
            if (aBoolean) {
                dayEdit.getBackground().mutate().clearColorFilter();
            } else {
                if (!currentModel.isDayOK()) {
                    dayEdit.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    dayEdit.getBackground().mutate().clearColorFilter();
                }
            }
        });

        SignUpSource source = new SignUpSource();

        RxView.clicks(submit).subscribe(aVoid -> {
            source.signUp(SignUpActivity.this, currentModel.getUsername(), currentModel.getPassword(), currentModel.getDate(), currentModel.getParentEmail()).
                    doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(SignUpActivity.this)).
                    doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                    doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                    subscribe(aBoolean -> {
                        // if aBoolean is true then all is OK
                        if(aBoolean) {
                            setResult(RESULT_OK);
                            finish();
                        }
                        // if aBoolean is false then it's a duplicate username
                        else {
                            SAAlert.getInstance().show(SignUpActivity.this,
                                    getString(R.string.sign_up_popup_warning_duplicate_title),
                                    getString(R.string.sign_up_popup_warning_duplicate_message),
                                    getString(R.string.sign_up_popup_dismiss_button),
                                    null,
                                    false,
                                    0,
                                    null);
                        }
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
                        // do nothing
                    });
        });
    }
}

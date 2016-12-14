package superawesome.tv.kwsdemoapp.activities.signup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.functions.Func1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class SignUpActivity extends BaseActivity {

    private SignUpModel currentModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // set toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.signinToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context context = this;
        SAProgressDialog dialog = SAProgressDialog.getInstance();

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

        RxView.focusChanges(usernameEdit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isUserOK()).
                subscribe(aBoolean -> setFieldColor(usernameEdit, aBoolean));

        RxView.focusChanges(password1Edit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isPassword1OK()).
                subscribe(aBoolean -> setFieldColor(password1Edit, aBoolean));

        RxView.focusChanges(password2Edit).skip(1).
                map(aBoolean -> aBoolean || (currentModel.isPassword2OK() && currentModel.arePasswordsSame())).
                subscribe(aBoolean -> setFieldColor(password2Edit, aBoolean));

        RxView.focusChanges(parentEmailEdit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isParentEmailOK()).
                subscribe(aBoolean -> setFieldColor(parentEmailEdit, aBoolean));

        RxView.focusChanges(yearEdit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isYearOK()).
                subscribe(aBoolean -> setFieldColor(yearEdit, aBoolean));

        RxView.focusChanges(monthEdit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isMonthOK()).
                subscribe(aBoolean -> setFieldColor(monthEdit, aBoolean));

        RxView.focusChanges(dayEdit).skip(1).
                map(aBoolean -> aBoolean || currentModel.isDayOK()).
                subscribe(aBoolean -> setFieldColor(dayEdit, aBoolean));

        RxView.clicks(submit).subscribe(aVoid -> {
            RxKWS.signUp(context, currentModel.getUsername(), currentModel.getPassword(), currentModel.getDate(), currentModel.getParentEmail()).
                    doOnSubscribe(() -> dialog.showProgress(context)).
                    doOnError(throwable -> dialog.hideProgress()).
                    doOnCompleted(dialog::hideProgress).
                    subscribe(this::clickAction, this::errorAlert);
        });
    }

    private void setFieldColor (EditText editText, boolean valid) {
        if (valid) {
            editText.getBackground().mutate().clearColorFilter();
        } else {
            editText.getBackground().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void clickAction (boolean success) {
        if (success) {
            setResult(RESULT_OK);
            finish();
        }
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
    }

    private void errorAlert (Throwable err) {
        SAAlert.getInstance().show(SignUpActivity.this,
                getString(R.string.sign_up_popup_error_title),
                getString(R.string.sign_up_popup_error_message),
                getString(R.string.sign_up_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }
}

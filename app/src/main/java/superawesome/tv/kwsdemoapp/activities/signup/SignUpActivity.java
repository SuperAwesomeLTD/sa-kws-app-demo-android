package superawesome.tv.kwsdemoapp.activities.signup;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.json.JSONObject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.activities.country.CountryActivity;
import superawesome.tv.kwsdemoapp.activities.country.CountryRowViewModel;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class SignUpActivity extends BaseActivity {

    // private constants
    private static final int COUNTRY_CODE = 113;

    private SignUpModel currentModel;
    private PublishSubject<String> countrySubject = null;

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

        // create the country subject
        countrySubject = PublishSubject.create();

        Context context = this;
        SAProgressDialog dialog = SAProgressDialog.getInstance();

        EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        EditText password1Edit = (EditText) findViewById(R.id.password1Edit);
        EditText password2Edit = (EditText) findViewById(R.id.password2Edit);
        EditText parentEmailEdit = (EditText) findViewById(R.id.parentEmail);
        EditText yearEdit = (EditText) findViewById(R.id.yearEdit);
        EditText monthEdit = (EditText) findViewById(R.id.monthEdit);
        EditText dayEdit = (EditText) findViewById(R.id.dayEdit);
        Button selectCountry = (Button) findViewById(R.id.SelectCountry);
        Button submit = (Button) findViewById(R.id.RegisterUserButton);
        ImageButton reload = (ImageButton) findViewById(R.id.ReloadRandomUsername);
        ImageView flag = (ImageView) findViewById(R.id.CountryFlag);

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

        Observable.combineLatest(rxUsername, rxPassword1, rxPassword2, rxParentEmail, rxYear, rxMonth, rxDay, countrySubject.asObservable().startWith(""), SignUpModel::new).
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
            RxKWS.signUp(context, currentModel.getUsername(), currentModel.getPassword(), currentModel.getDate(), currentModel.getParentEmail(), currentModel.getISOCode()).
                    doOnSubscribe(() -> dialog.showProgress(context)).
                    doOnError(throwable -> dialog.hideProgress()).
                    doOnCompleted(dialog::hideProgress).
                    subscribe(this::clickAction, this::errorAlert);
        });

        RxView.clicks(selectCountry).subscribe(aVoid -> {
            Intent createIntent = new Intent(SignUpActivity.this, CountryActivity.class);
            startActivityForResult(createIntent, COUNTRY_CODE);
        });

        // for the name reload
        RxView.clicks(reload)
                .map(aVoid -> "")
                .startWith("")
                .flatMap(aVoid -> RxKWS.getRandomName(SignUpActivity.this))
                .subscribe(usernameEdit::setText);

        // when coming back from the country selection
        setOnActivityResult((requestCode, resultCode, data) -> {

            if (requestCode == COUNTRY_CODE && resultCode == RESULT_OK && data != null) {

                // get the data
                String countryString = data.getStringExtra("k_COUNTRY_DATA");
                JSONObject countryJson = SAJsonParser.newObject(countryString);
                CountryRowViewModel tmp = new CountryRowViewModel(countryJson);

                // update UI

                Resources resources = SignUpActivity.this.getResources();
                String packageName = SignUpActivity.this.getPackageName();

                selectCountry.setText(tmp.getCountryName());
                selectCountry.setTextColor(resources.getColor(R.color.countryBtnGray));

                try {
                    int flagId = resources.getIdentifier(tmp.getCountryFlagString(), "drawable", packageName);
                    flag.setImageDrawable(resources.getDrawable(flagId));
                } catch (Exception e){
                    int flagId = resources.getIdentifier(tmp.getDefaultCountryFlagString(), "drawable", packageName);
                    flag.setImageDrawable(resources.getDrawable(flagId));
                }

                // update country subject
                countrySubject.onNext(tmp.getCountryISOCode());
            }
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
                    getString(R.string.page_signup_popup_error_create_title),
                    getString(R.string.page_signup_popup_error_create_message),
                    getString(R.string.page_signup_popup_error_create_ok_button),
                    null,
                    false,
                    0,
                    null);
        }
    }

    private void errorAlert (Throwable err) {
        SAAlert.getInstance().show(SignUpActivity.this,
                getString(R.string.page_signup_popup_error_network_title),
                getString(R.string.page_signup_popup_error_network_message),
                getString(R.string.page_signup_popup_error_network_ok_button),
                null,
                false,
                0,
                null);
    }
}

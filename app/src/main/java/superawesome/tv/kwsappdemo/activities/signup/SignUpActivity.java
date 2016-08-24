package superawesome.tv.kwsappdemo.activities.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.UniversalNotifier;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class SignUpActivity extends AppCompatActivity {

    // toolbar
    android.support.v7.widget.Toolbar toolbar = null;

    // text fields
    EditText usernameEdit = null;
    EditText password1Edit = null;
    EditText password2Edit = null;
    EditText yearEdit = null;
    EditText monthEdit = null;
    EditText dayEdit = null;
    Button submit = null;

    // private vars
    private String username = null;
    private String password = null;
    private int year = 0, month = 0, day = 0;
    private String date = null;

    private Boolean usernameOK = false;
    private Boolean passwordOK = false;
    private Boolean dateOK = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.signinToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // get edits
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        password1Edit = (EditText) findViewById(R.id.password1Edit);
        password2Edit = (EditText) findViewById(R.id.password2Edit);
        yearEdit = (EditText) findViewById(R.id.yearEdit);
        monthEdit = (EditText) findViewById(R.id.monthEdit);
        dayEdit = (EditText) findViewById(R.id.dayEdit);
        submit = (Button) findViewById(R.id.RegisterUserButton);


        // process the username
        RxTextView.textChanges(usernameEdit).
                map(charSequence -> charSequence.toString().trim()).
                map(s -> s != null && !s.isEmpty()).
                doOnNext(usernameValid -> username = usernameValid ? usernameEdit.getText().toString() : null).
                subscribe(aBoolean -> usernameOK = aBoolean );

        // process passwords
        rx.Observable<String> pass1Obs = RxTextView.textChanges(password1Edit).
                map(charSequence -> charSequence.toString().trim());
        rx.Observable<Boolean> pass1ValidObs = pass1Obs.
                map(s -> !s.isEmpty() && s.length() >= 8);
        rx.Observable<String> pass2Obs = RxTextView.textChanges(password2Edit).
                map(charSequence -> charSequence.toString().trim());
        rx.Observable<Boolean> pass2ValidObs = pass2Obs.
                map(s -> !s.isEmpty() && s.length() >= 8);
        rx.Observable.combineLatest(pass1Obs, pass2Obs, pass1ValidObs, pass2ValidObs, (pass1, pass2, pass1Ok, pass2Ok) -> pass1.equals(pass2) && pass1Ok && pass2Ok).
                doOnNext(passwordsValid -> password = passwordsValid ? password1Edit.getText().toString() : null).
                subscribe(aBoolean -> passwordOK = aBoolean );

        // process year
        rx.Observable<Integer> yearObs = RxTextView.textChanges(yearEdit).
                map(charSequence -> charSequence.toString()).
                map(s -> s != null && !s.isEmpty() ? Integer.parseInt(s) : 0).
                doOnNext(integer -> year = integer);
        rx.Observable<Integer> monthObs = RxTextView.textChanges(monthEdit).
                map(charSequence -> charSequence.toString()).
                map(s -> s != null && !s.isEmpty() ? Integer.parseInt(s) : 0).
                doOnNext(integer -> month = integer);
        rx.Observable<Integer> dayObs = RxTextView.textChanges(dayEdit).
                map(charSequence -> charSequence.toString()).
                map(s -> s != null && !s.isEmpty() ? Integer.parseInt(s) : 0).
                doOnNext(integer -> day = integer);

        rx.Observable.combineLatest(yearObs, monthObs, dayObs, (year1, month1, day1) -> year1 > 1900 && (month1 >= 1 && month1 <= 12) && (day1 >= 1 && day1 <= 30)).
                doOnNext(dateValid -> {
                    date = dateValid ? (year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day)) : null;
                }).subscribe(aBoolean -> dateOK = aBoolean );

        RxView.clicks(submit).subscribe(aVoid -> {
            if (usernameOK && dateOK && passwordOK) {
                // start progress dialog
                SAProgressDialog.getInstance().showProgress(SignUpActivity.this);

                // perform singup
                KWSSingleton.getInstance().loginUser(username, password, date).
                        subscribe(aBoolean -> {
                            // user logged OK
                        }, throwable -> {
                            // error case
                            SAProgressDialog.getInstance().hideProgress();
                            SAAlert.getInstance().show(SignUpActivity.this, "Hey!", "Error trying to create user. Please try again!", "Got it!", null, false, 0, null);
                        }, () -> {
                            // completed case
                            SAProgressDialog.getInstance().hideProgress();
                            UniversalNotifier.getInstance().postNotification("RECEIVED_SIGNUP");
                            onBackPressed();
                        });
            } else {
                SAAlert.getInstance().show(SignUpActivity.this, "Hey!", "Please specify a valid username, valid, matching passwords and a valid date of birth!", "Got it!", null, false, 0, null);
            }
        });
    }
}

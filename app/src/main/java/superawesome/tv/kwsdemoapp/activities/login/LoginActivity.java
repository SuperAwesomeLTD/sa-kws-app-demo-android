package superawesome.tv.kwsdemoapp.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseActivity;
import superawesome.tv.kwsdemoapp.activities.signup.SignUpActivity;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SALoadScreen;

public class LoginActivity extends BaseActivity {

    // private constants
    private static final int AUTH_REQ_CODE = 113;

    private LoginModel currentModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context context = LoginActivity.this;
        SALoadScreen dialog = SALoadScreen.getInstance();

        EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        Button login = (Button) findViewById(R.id.LoginUserButton);
        Button create = (Button) findViewById(R.id.CreateUserButton);

        RxView.clicks(create).subscribe(this::create);

        Observable<String> rxUsername = RxTextView.textChanges(usernameEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxPassword = RxTextView.textChanges(passwordEdit).
                map(charSequence -> charSequence.toString().trim());

        Observable.combineLatest(rxUsername, rxPassword, LoginModel::new)
                .doOnNext(model -> currentModel = model)
                .map(LoginModel::isValid)
                .subscribe(login::setEnabled);

        RxView.clicks(login).
                subscribe(aVoid -> {
                    RxKWS.login(context, currentModel.getUsername(), currentModel.getPassword())
                            .doOnSubscribe(() -> dialog.show(context))
                            .doOnError(throwable -> dialog.hide())
                            .doOnCompleted(dialog::hide)
                            .subscribe(this::finishOK, this::errorAlert);
                });

        setOnActivityResult((requestCode, resultCode, data) -> {
            if (requestCode == AUTH_REQ_CODE && resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void finishOK (boolean status) {
        if (status) {
            setResult(RESULT_OK);
            finish();
        } else {
            SAAlert.getInstance().show(LoginActivity.this,
                    getString(R.string.page_login_popup_error_auth_title),
                    getString(R.string.page_login_popup_error_auth_message),
                    getString(R.string.page_login_popup_error_auth_ok_button),
                    null,
                    false,
                    0,
                    null);
        }
    }

    private void errorAlert (Throwable err) {
        SAAlert.getInstance().show(LoginActivity.this,
                getString(R.string.page_login_popup_error_network_title),
                getString(R.string.page_login_popup_error_network_message),
                getString(R.string.page_login_popup_error_network_ok_button),
                null,
                false,
                0,
                null);
    }

    private void create (Void v) {
        Intent createIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivityForResult(createIntent, AUTH_REQ_CODE);
    }
}

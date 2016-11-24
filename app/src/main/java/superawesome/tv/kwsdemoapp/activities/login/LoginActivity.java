package superawesome.tv.kwsdemoapp.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.signup.SignUpActivity;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 11/11/16.
 */
public class LoginActivity extends AppCompatActivity {

    // private constants
    private static final int SET_REQ_CODE = 111;

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

        EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        Button login = (Button) findViewById(R.id.LoginUserButton);
        Button create = (Button) findViewById(R.id.CreateUserButton);

        RxView.clicks(create).subscribe(aVoid -> create());

        Observable<String> rxUsername = RxTextView.textChanges(usernameEdit).
                map(charSequence -> charSequence.toString().trim());
        Observable<String> rxPassword = RxTextView.textChanges(passwordEdit).
                map(charSequence -> charSequence.toString().trim());

        Observable.combineLatest(rxUsername, rxPassword, LoginModel::new).
                doOnNext(model -> currentModel = model).
                map(LoginModel::isValid).
                subscribe(login::setEnabled);

        LoginSource source = new LoginSource();

        RxView.clicks(login).
                subscribe(aVoid -> {
                    source.login(LoginActivity.this, currentModel.getUsername(), currentModel.getPassword()).
                            doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(LoginActivity.this)).
                            doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                            doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                            subscribe(this::finishOK, this::errorAlert);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_REQ_CODE && resultCode == RESULT_OK) {
            UniversalNotifier.postNotification("RECEIVED_SIGNUP");
            onBackPressed();
        }
    }

    private void finishOK (boolean status) {
        if (status) {
            UniversalNotifier.postNotification("RECEIVED_SIGNUP");
            onBackPressed();
        } else {
            SAAlert.getInstance().show(LoginActivity.this,
                    getString(R.string.login_popup_error_title),
                    getString(R.string.login_popup_error_message),
                    getString(R.string.login_popup_dismiss_button),
                    null,
                    false,
                    0,
                    null);
        }
    }

    private void errorAlert (Throwable err) {
        SAAlert.getInstance().show(LoginActivity.this,
                getString(R.string.login_popup_error_network_title),
                getString(R.string.login_popup_error_network_message),
                getString(R.string.login_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }

    private void create () {
        Intent createIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivityForResult(createIntent, SET_REQ_CODE);
    }
}

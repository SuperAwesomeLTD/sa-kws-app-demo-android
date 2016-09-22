package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import rx.functions.Action0;
import rx.functions.Action1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.KWSSingleton;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.ViewModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ListView userDetailsListView = (ListView) findViewById(R.id.userDetailsListView);
        UserAdapter adapter = new UserAdapter(UserActivity.this);
        userDetailsListView.setAdapter(adapter);

        UserSource source = new UserSource();
        source.getUser(UserActivity.this).
                doOnSubscribe(() -> SAProgressDialog.getInstance().showProgress(UserActivity.this)).
                toList().
                doOnError(throwable -> SAProgressDialog.getInstance().hideProgress()).
                doOnCompleted(() -> SAProgressDialog.getInstance().hideProgress()).
                subscribe(viewModels -> {
                    adapter.updateData(viewModels);
                }, throwable -> {
                    SAAlert.getInstance().show(UserActivity.this,
                            getString(R.string.user_popup_error_title),
                            getString(R.string.user_popup_error_message),
                            getString(R.string.user_popup_dismiss_button),
                            null,
                            false,
                            0,
                            null);
                });

        RxView.clicks(logoutButton).
                subscribe(aVoid -> {
                    KWSSingleton.getInstance().logoutUser();
                    UniversalNotifier.postNotification("RECEIVED_LOGOUT");
                    onBackPressed();
                });

    }
}

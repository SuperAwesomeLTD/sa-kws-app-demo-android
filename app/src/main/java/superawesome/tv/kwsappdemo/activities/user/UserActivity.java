package superawesome.tv.kwsappdemo.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserActivity extends AppCompatActivity {

    // toolbar
    android.support.v7.widget.Toolbar toolbar = null;
    private ListView userDetailsListView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Context context = this;

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        userDetailsListView = (ListView) findViewById(R.id.userDetailsListView);
        UserAdapter adapter = new UserAdapter(UserActivity.this, R.layout.listitem_leaderrow);
        adapter.update(() -> {
            SAProgressDialog.getInstance().showProgress(context);
        }, () -> {
            SAProgressDialog.getInstance().hideProgress();
            userDetailsListView.setAdapter(adapter);
        }, () -> {
            SAProgressDialog.getInstance().hideProgress();
            SAAlert.getInstance().show(UserActivity.this, "Hey!", "An error occurred retrieving user data, please try again.", "Got it!", null, false, 0, null);
        });
    }

    public void onLogout(View v) {
        // perform logout
        FirebaseInstanceId.getInstance().getToken();
        KWSSingleton.getInstance().setModel(null);

        Intent intent = new Intent();
        intent.setAction("superawesome.tv.RECEIVED_LOGOUT");
        intent.putExtra("STATUS", 1);
        sendBroadcast(intent);

        onBackPressed();
    }
}

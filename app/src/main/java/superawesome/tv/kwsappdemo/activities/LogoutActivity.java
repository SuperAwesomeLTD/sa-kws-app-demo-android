package superawesome.tv.kwsappdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class LogoutActivity extends AppCompatActivity {

    // toolbar
    android.support.v7.widget.Toolbar toolbar = null;

    TextView username = null;
    TextView token = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.logoutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        username = (TextView) findViewById(R.id.usernameValue);
        username.setText(KWSSingleton.getInstance().getModel().username);
        token = (TextView) findViewById(R.id.tokenValue);
        token.setText(KWSSingleton.getInstance().getModel().token);
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

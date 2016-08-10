package superawesome.tv.kwsappdemo.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.LogoutActivity;
import superawesome.tv.kwsappdemo.activities.SignUpActivity;
import superawesome.tv.kwsappdemo.activities.UserActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeatureAuthView extends LinearLayout {

    // constants
    private final String AUTHURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // layout
    private Button authAction;
    private Button authDocs;
    private KWSModel localModel = null;

    public FeatureAuthView(Context context) {
        this(context, null, 0);
    }

    public FeatureAuthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeatureAuthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_feature_auth, this, true);

        authAction = (Button) findViewById(R.id.authAction);
        authDocs = (Button) findViewById(R.id.authDocs);

        localModel = KWSSingleton.getInstance().getModel();
        updateInterface();

        authAction.setOnClickListener(v -> {
            Intent authIntent = new Intent(getContext(), localModel == null ? SignUpActivity.class : UserActivity.class);
            context.startActivity(authIntent);
        });

        authDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTHURL));
            context.startActivity(browserIntent);
        });

        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SignUpReceiver(), filter1);
        context.registerReceiver(new LogoutReceiver(), filter2);
    }

    private void updateInterface () {
        boolean status = localModel != null;
        authAction.setText(status ? "Logged in as " + localModel.username : "AUTHENTICATE USER");
    }

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureAuthView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureAuthView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }
    }
}

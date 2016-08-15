package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.signup.SignUpActivity;
import superawesome.tv.kwsappdemo.activities.user.UserActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import superawesome.tv.kwsappdemo.aux.SmartReceiver;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeatureAuthView extends LinearLayout {

    // constants
    private final String AUTHURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // vars
    private Button authAction;
    private Button authDocs;
    private KWSModel localModel = null;
    private IntentFilter signupFilter = null;
    private IntentFilter logoutFilter = null;

    // constructors
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

        signupFilter = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeatureAuthView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), signupFilter);

        logoutFilter = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeatureAuthView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), logoutFilter);
    }

    private void updateInterface () {
        boolean status = localModel != null;
        authAction.setText(status ? "Logged in as " + localModel.username : "AUTHENTICATE USER");
    }
}

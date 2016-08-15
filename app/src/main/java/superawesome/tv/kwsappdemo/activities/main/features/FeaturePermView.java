package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import superawesome.tv.kwsappdemo.aux.SmartReceiver;
import superawesome.tv.kwsappdemo.aux.SmartReceiverInterface;
import tv.superawesome.lib.sautils.SAAlert;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeaturePermView extends LinearLayout {

    // constants
    private final String PERMURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // private vars
    private Button permissionAdd;
    private Button permissionDocs;
    private CharSequence currentTitle = null;
    private KWSModel localModel = null;
    private KWSPermissionType[] requestedType = null;
    private IntentFilter signupFilter = null;
    private IntentFilter logoutFilter = null;

    // constructor
    public FeaturePermView(Context context) {
        this(context, null, 0);
    }

    public FeaturePermView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeaturePermView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_feature_perm, this, true);

        permissionAdd = (Button) findViewById(R.id.permissionAddButton);
        permissionDocs = (Button) findViewById(R.id.permissionDocs);

        localModel = KWSSingleton.getInstance().getModel();
        updateInterface();

        permissionAdd.setOnClickListener(v -> {
            Context c = getContext();
            KWSPermissionType types[] = new KWSPermissionType[] {
                    KWSPermissionType.accessEmail,
                    KWSPermissionType.accessAddress,
                    KWSPermissionType.accessFirstName,
                    KWSPermissionType.accessLastName,
                    KWSPermissionType.accessPhoneNumber,
                    KWSPermissionType.sendNewsletter
            };
            CharSequence titles[] = new CharSequence[] {
                    "Access email",
                    "Access address",
                    "Access first name",
                    "Access last name",
                    "Access phone number",
                    "Send newslatter"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Ask for a permission");
            builder.setItems(titles, (dialog, which) -> {
                currentTitle = titles[which];
                requestedType = new KWSPermissionType[] { types [which] };
                KWS.sdk.requestPermission(requestedType, this::permissionCallback);
            });
            builder.show();
        });

        permissionDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PERMURL));
            getContext().startActivity(browserIntent);
        });

        signupFilter = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeaturePermView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), signupFilter);

        logoutFilter = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeaturePermView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), logoutFilter);
    }

    private void permissionCallback (boolean success, boolean requested) {
        if (!success) {
            SAAlert.getInstance().show(getContext(), "Hey!", "Something happened while asking for permissions", "Got it!", null, false, 0, null);
        } else {
            if (requested) {
                SAAlert.getInstance().show(getContext(), "Great!", "You've successfully asked for permission for " + currentTitle, "Got it!", null, false, 0 , null);
            } else {
                KWS.sdk.submitParentEmailWithPopup(b -> {
                    if (b) {
                        KWS.sdk.requestPermission(requestedType, this::permissionCallback);
                    }
                });
            }
        }
    }

    private void updateInterface () {
        boolean status = localModel != null;
        permissionAdd.setEnabled(status);
    }
}

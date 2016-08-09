package superawesome.tv.kwsappdemo.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.process.KWSErrorType;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeatureNotifView extends LinearLayout {

    // constants
    private final String NOTIFURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // private vars
    private Button notifEnableDisable;
    private Button notifDocs;
    private KWSModel localModel = null;

    public FeatureNotifView(Context context) {
        this(context, null, 0);
    }

    public FeatureNotifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeatureNotifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_feature_notif, this, true);

        notifEnableDisable = (Button) findViewById(R.id.notifEnableDisable);
        notifDocs = (Button) findViewById(R.id.notifDocs);

        localModel = KWSSingleton.getInstance().getModel();
        updateInterface();

        if (localModel != null) {
            KWS.sdk.isRegistered(success -> {
                if (success) {
                    setupAsRegistered();
                } else {
                    setupAsUnregistered();
                }
            });
        } else {
            setupAsUnregistered();
        }

        notifDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFURL));
            getContext().startActivity(browserIntent);
        });

        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SignUpReceiver(), filter1);
        context.registerReceiver(new LogoutReceiver(), filter2);
    }

    public void setupAsUnregistered () {
        final Context context = getContext();
        notifEnableDisable.setText("ENABLE PUSH NOTIFICATIONS");
        notifEnableDisable.setOnClickListener(v -> {
            SAProgressDialog.getInstance().showProgress(context);
            KWS.sdk.registerWithPopup(this::registerCallback);
        });
    }

    public void setupAsRegistered () {
        final Context context = getContext();
        notifEnableDisable.setText("DISABLE PUSH NOTIFICATIONS");
        notifEnableDisable.setOnClickListener(v -> {
            SAProgressDialog.getInstance().showProgress(context);
            KWS.sdk.unregister(this::unregisterCallback);
        });
    }

    public void registerCallback (boolean success, KWSErrorType kwsErrorType) {
        // get context
        final Context context = getContext();

        // dismiss progress dialog
        SAProgressDialog.getInstance().hideProgress();

        // some kind of error clause
        if (!success) {
            switch (kwsErrorType) {
                case UserHasNoParentEmail: {
                    KWS.sdk.submitParentEmailWithPopup(success1 -> {
                        if (success1) {
                            SAProgressDialog.getInstance().showProgress(context);
                            KWS.sdk.register(FeatureNotifView.this::registerCallback);
                        }
                    });
                    break;
                }
                case ParentHasDisabledRemoteNotifications:
                case UserHasDisabledRemoteNotifications:
                case FirebaseNotSetup:
                case FirebaseCouldNotGetToken:
                case FailedToCheckIfUserHasNotificationsEnabledInKWS:
                case FailedToRequestNotificationsPermissionInKWS:
                case FailedToSubmitParentEmail:
                case FailedToSubscribeTokenToKWS: {
                    SAAlert.getInstance().show(context, "Hey!", "An error occurred: " + kwsErrorType.toString(), "Got it!", null, false, 0, null);
                    break;
                }
            }
        }
        // success clause
        else {
            setupAsRegistered();
            SAAlert.getInstance().show(context, "Great news!", "This user has been successfully registered for Remote Notifications in KWS.", "Got it!", null, false, 0, null);
        }
    }

    public void unregisterCallback (boolean success) {
        // hide progress dialog
        SAProgressDialog.getInstance().hideProgress();

        // could not unregister
        if (!success) {
            SAAlert.getInstance().show(getContext(), "Ups!", "Failed to unsubscribe Firebase token from KWS because of network error.", "Got it!", null, false, 0, null);
        }
        // could unregister OK
        else {
            setupAsUnregistered();
            SAAlert.getInstance().show(getContext(), "Hey!", "This user has been de-registered for Remote Notifications", "Got it!", null, false, 0, null);
        }
    }

    private void updateInterface () {
        boolean status = localModel != null;
        notifEnableDisable.setEnabled(status);
    }

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureNotifView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface ();
            setupAsUnregistered();
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureNotifView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface ();

            // unregister & desetup (has to be here)
            SAProgressDialog.getInstance().showProgress(getContext());
            KWS.sdk.unregister(FeatureNotifView.this::unregisterCallback);
            KWS.sdk.desetup();
        }
    }
}

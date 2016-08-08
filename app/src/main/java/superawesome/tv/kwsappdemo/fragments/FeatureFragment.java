package superawesome.tv.kwsappdemo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.process.KWSErrorType;
import kws.superawesome.tv.kwssdk.process.RegisterInterface;
import superawesome.tv.kwsappdemo.activities.LogoutActivity;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.SignUpActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FeatureFragment extends Fragment {

    // constants
    private final String AUTHURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";
    private final String NOTIFURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";
    private final String KWS_API = "https://kwsapi.demo.superawesome.tv/v1/";

    // buttons
    private Button authAction;
    private Button authDocs;
    private Button notifEnableDisable;
    private Button notifDocs;

    // private vars
    private KWSModel localModel = null;

    // constructor
    public FeatureFragment () {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KWS.sdk.setApplicationContext(getContext().getApplicationContext());

        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        getActivity().registerReceiver(new SignUpReceiver(), filter1);
        getActivity().registerReceiver(new LogoutReceiver(), filter2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get views
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        authAction = (Button) view.findViewById(R.id.authAction);
        notifEnableDisable = (Button) view.findViewById(R.id.notifEnableDisable);

        // get local model
        localModel = KWSSingleton.getInstance().getModel();

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Login
        ////////////////////////////////////////////////////////////////////////////////////////////

        authAction.setText(localModel == null ? "AUTHENTICATE USER" : "Logged in as " + localModel.username);
        authAction.setOnClickListener(v -> {
            Intent authIntent = new Intent(getContext(), localModel == null ? SignUpActivity.class : LogoutActivity.class);
            startActivity(authIntent);
        });

        authDocs = (Button) view.findViewById(R.id.authDocs);
        authDocs.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTHURL));
                getContext().startActivity(browserIntent);
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Notifications
        ////////////////////////////////////////////////////////////////////////////////////////////

        // get the notification button

        if (localModel != null) {
            KWS.sdk.setup(getContext(), localModel.token, KWS_API);
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

        // notifications
        notifDocs = (Button) view.findViewById(R.id.notifDocs);
        notifDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFURL));
            getContext().startActivity(browserIntent);
        });

        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Setup functions
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setupAsUnregistered () {

        // get needed vars
        final Context context = getContext();
        localModel = KWSSingleton.getInstance().getModel();

        notifEnableDisable.setText("ENABLE PUSH NOTIFICATIONS");
        notifEnableDisable.setOnClickListener(v -> {
            if (localModel != null) {
                SAAlert.getInstance().show(context, "Hey!", "Do you want to enable Push Notifications for this user?", "Yes", "No", false, 0, (button, message) -> {
                    if (button == SAAlert.OK_BUTTON) {
                        SAProgressDialog.getInstance().showProgress(context);
                        KWS.sdk.setup(context, localModel.token, KWS_API);
                        KWS.sdk.register(this::registerCallback);
                    }
                });
            }
            else {
                SAAlert.getInstance().show(getContext(), "Hey!", "Before enabling Push Notifications you must authenticate with KWS.", "Got it!", null, false, 0, null);
            }
        });
    }

    public void setupAsRegistered () {
        notifEnableDisable.setText("DISABLE PUSH NOTIFICATIONS");
        notifEnableDisable.setOnClickListener(v -> {
            SAProgressDialog.getInstance().showProgress(getContext());
            KWS.sdk.unregister(this::unregisterCallback);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Common callback functions
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
                            KWS.sdk.register(FeatureFragment.this::registerCallback);
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
                    SAAlert.getInstance().show(context, "Hey!", "An error occured: " + kwsErrorType.toString(), "Got it!", null, false, 0, null);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Receivers
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureFragment.this.authAction.setText("Logged in as " + KWSSingleton.getInstance().getModel().username);
            setupAsUnregistered();
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureFragment.this.authAction.setText("AUTHENTICATE USER");
            SAProgressDialog.getInstance().showProgress(getContext());
            KWS.sdk.unregister(FeatureFragment.this::unregisterCallback);
        }
    }
}

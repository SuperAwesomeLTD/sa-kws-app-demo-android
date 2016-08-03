package superawesome.tv.kwsappdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.KWSCheckInterface;
import kws.superawesome.tv.kwssdk.KWSErrorType;
import kws.superawesome.tv.kwssdk.KWSRegisterInterface;
import kws.superawesome.tv.kwssdk.KWSUnregisterInterface;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FeatureFragment extends Fragment implements KWSRegisterInterface, KWSUnregisterInterface, KWSCheckInterface {

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
        // LOGIN
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
        // Notif
        ////////////////////////////////////////////////////////////////////////////////////////////

        // get the notification button

        if (localModel != null) {
            KWS.sdk.setup(getContext(), localModel.token, KWS_API, false);
            KWS.sdk.userIsRegistered(this);
        } else {
            setupAsUnregistered();
        }

        // notif
        notifDocs = (Button) view.findViewById(R.id.notifDocs);
        notifDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFURL));
            getContext().startActivity(browserIntent);
        });

        return view;
    }

    public void setupAsUnregistered () {

        // update local model
        localModel = KWSSingleton.getInstance().getModel();

        notifEnableDisable.setText("ENABLE PUSH NOTIFICATIONS");
        notifEnableDisable.setOnClickListener(v -> {
            Log.d("SuperAwesome", "Done deal!");
            if (localModel != null) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Hey!");
                alert.setCancelable(false);
                alert.setMessage("Do you want to enable Push Notifications for this user?");
                final AlertDialog.Builder ok = alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        SAProgressDialog.getInstance().showProgress(getContext());
                        KWS.sdk.setup(getContext(), KWSSingleton.getInstance().getModel().token, KWS_API, false);
                        KWS.sdk.registerForRemoteNotifications(FeatureFragment.this);
                    }
                });
                final AlertDialog.Builder cancel = alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
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
            KWS.sdk.unregisterForRemoteNotifications(FeatureFragment.this);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Registration callbacks
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void kwsSDKDidRegisterUserForRemoteNotifications() {
        setupAsRegistered();
        SAProgressDialog.getInstance().hideProgress();
        SAAlert.getInstance().show(getContext(), "Great news!", "This user has been successfully registered for Remote Notifications in KWS.", "Got it!", null, false, 0, null);
    }

    @Override
    public void kwsSDKDidFailToRegisterUserForRemoteNotificationsWithError(KWSErrorType kwsErrorType) {

        switch (kwsErrorType) {
            case ParentHasDisabledRemoteNotifications: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Hey!", "This user could not be registered for Remote Notifications because a parent in KWS has disabled this functionality.", "Got it!", null, false, 0, null);
                break;
            }
            case UserHasDisabledRemoteNotifications: {
                SAProgressDialog.getInstance().hideProgress();
                // not happening
                break;
            }
            case UserHasNoParentEmail: {
                KWS.sdk.showParentEmailPopup();
                break;
            }
            case ParentEmailInvalid: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "You must input a valid parent email!", "Got it!", null, false, 0, null);
                break;
            }
            case FirebaseNotSetup: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Could not continue process since Firebase is not properly setup!", "Got it!", null, false, 0, null);
                break;
            }
            case FirebaseCouldNotGetToken: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Could not continue process since Firebase could not obtain a valid token.", "Got it!", null, false, 0, null);
                break;
            }
            case FailedToCheckIfUserHasNotificationsEnabledInKWS: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Failed to beck if user has Notifications enabled in KWS because of network error.", "Got it!", null, false, 0, null);
                break;
            }
            case FailedToRequestNotificationsPermissionInKWS: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Failed to request Notification permissions in KWS because of network error.", "Got it!", null, false, 0, null);
                break;
            }
            case FailedToSubmitParentEmail: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Failed to submit parent email to KWS because of network error", "Got it!", null, false, 0, null);
                break;
            }
            case FailedToSubscribeTokenToKWS: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Failed to subscribe Firebase token to KWS because of network error.", "Got it!", null, false, 0, null);
                break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Unregistration callbacks
    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void kwsSDKDidUnregisterUserForRemoteNotifications() {
        setupAsUnregistered();
        SAProgressDialog.getInstance().hideProgress();
        SAAlert.getInstance().show(getContext(), "Hey!", "This user has been de-registered for Remote Notifications", "Got it!", null, false, 0, null);
    }

    @Override
    public void kwsSDKDidFailToUnregisterUserForRemoteNotifications() {
        SAProgressDialog.getInstance().hideProgress();
        SAAlert.getInstance().show(getContext(), "Ups!", "Failed to unsubscribe Firebase token from KWS because of network error.", "Got it!", null, false, 0, null);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Is Registered callbacks
    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void kwsSDKUserIsRegistered() {
        setupAsRegistered();
    }

    @Override
    public void kwsSDKUserIsNotRegistered() {
        setupAsUnregistered();
    }

    @Override
    public void kwsSDKDidFailToCheckIfUserIsRegistered() {
        setupAsUnregistered();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RECEIVERS
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
            KWS.sdk.unregisterForRemoteNotifications(FeatureFragment.this);
        }
    }
}

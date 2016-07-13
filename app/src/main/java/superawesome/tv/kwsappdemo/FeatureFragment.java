package superawesome.tv.kwsappdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.BoringLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.PortUnreachableException;
import java.util.PriorityQueue;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.KWSErrorType;
import kws.superawesome.tv.kwssdk.KWSInterface;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FeatureFragment extends Fragment implements KWSInterface {

    // constants
    private final String AUTHURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";
    private final String NOTIFURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";
    private final String KWS_API = "https://kwsapi.demo.superawesome.tv/v1/";

    // buttons
    private Button authAction;
    private Button authDocs;
    private Button notifEnable;
    private Button notifDisable;
    private Button notifDocs;

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
        View view = inflater.inflate(R.layout.fragment_feature, container, false);

        authAction = (Button) view.findViewById(R.id.authAction);

        if (KWSSingleton.getInstance().getModel() == null) {
            authAction.setText("AUTHENTICATE USER");
        } else {
            authAction.setText("Logged in as " + KWSSingleton.getInstance().getModel().username);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // LOGIN
        ////////////////////////////////////////////////////////////////////////////////////////////
        authAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KWSSingleton.getInstance().getModel() == null) {
                    Intent signin = new Intent(getContext(), SignUpActivity.class);
                    startActivity(signin);
                }
                else {
                    Intent logout = new Intent(getContext(), LogoutActivity.class);
                    startActivity(logout);
                }
            }
        });

        authDocs = (Button) view.findViewById(R.id.authDocs);
        authDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTHURL));
                    getContext().startActivity(browserIntent);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Notif
        ////////////////////////////////////////////////////////////////////////////////////////////
        notifEnable = (Button) view.findViewById(R.id.notifEnable);
        notifEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KWSSingleton.getInstance().getModel() != null) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Hey!");
                    alert.setCancelable(false);
                    alert.setMessage("Do you want to enable Push Notifications for this user?");
                    final AlertDialog.Builder ok = alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            SAProgressDialog.getInstance().showProgress(getContext());

                            KWS.sdk.setup(getContext(), KWSSingleton.getInstance().getModel().token, KWS_API, false, FeatureFragment.this);
                            KWS.sdk.registerForRemoteNotifications();
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
            }
        });

        notifDisable = (Button) view.findViewById(R.id.notifDisable);
        notifDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SAProgressDialog.getInstance().showProgress(getContext());
                KWS.sdk.unregisterForRemoteNotifications();
            }
        });

        notifDocs = (Button) view.findViewById(R.id.notifDocs);
        notifDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFURL));
                getContext().startActivity(browserIntent);
            }
        });


        return view;
    }

    @Override
    public void kwsSDKDidRegisterUserForRemoteNotifications() {
        SAProgressDialog.getInstance().hideProgress();
        SAAlert.getInstance().show(getContext(), "Great news!", "This user has been successfully registered for Remote Notifications in KWS.", "Got it!", null, false, 0, null);
    }

    @Override
    public void kwsSDKDidUnregisterUserForRemoteNotifications() {
        SAProgressDialog.getInstance().hideProgress();
        SAAlert.getInstance().show(getContext(), "Hey!", "This user has been de-registered for Remote Notifications", "Got it!", null, false, 0, null);
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
            case FailedToUbsubscribeTokenToKWS: {
                SAProgressDialog.getInstance().hideProgress();
                SAAlert.getInstance().show(getContext(), "Ups!", "Failed to unsubscribe Firebase token from KWS because of network error.", "Got it!", null, false, 0, null);
                break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RECEIVERS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureFragment.this.authAction.setText("Logged in as " + KWSSingleton.getInstance().getModel().username);
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureFragment.this.authAction.setText("AUTHENTICATE USER");
        }
    }
}

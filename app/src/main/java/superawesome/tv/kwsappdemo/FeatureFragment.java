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

import kws.superawesome.tv.KWS;
import kws.superawesome.tv.KWSErrorType;
import kws.superawesome.tv.KWSInterface;

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

    // progress
    ProgressDialog progress;

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

        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
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

                            progress.show();

                            KWS.sdk.setup(getContext(), KWSSingleton.getInstance().getModel().token, KWS_API, false, FeatureFragment.this);
                            KWS.sdk.checkIfNotificationsAreAllowed();
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
                    KWSSimpleAlert.getInstance().show(getContext(), "Hey!", "Before enabling Push Notifications you must authenticate with KWS.", "Got it!");
                }
            }
        });

        notifDisable = (Button) view.findViewById(R.id.notifDisable);
        notifDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
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
    public void kwsSDKDoesAllowUserToRegisterForRemoteNotifications() {
        KWS.sdk.registerForRemoteNotifications();
    }

    @Override
    public void kwsSDKDidRegisterUserForRemoteNotifications() {
        progress.dismiss();
        KWSSimpleAlert.getInstance().show(getContext(), "Great news!", "This user has been successfully registered for Remote Notifications in KWS.", "Got it!");
    }

    @Override
    public void kwsSDKDidUnregisterUserForRemoteNotifications() {
        progress.dismiss();
        KWSSimpleAlert.getInstance().show(getContext(), "Hey!", "This user has been de-registered for Remote Notifications", "Got it!");
    }

    @Override
    public void kwsSDKDidFailToRegisterUserForRemoteNotificationsWithError(KWSErrorType kwsErrorType) {
        switch (kwsErrorType) {
            case NoKWSPermission: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Hey!", "This user could not be registered for Remote Notifications because a parent in KWS has disabled this functionality.", "Got it!");
                break;
            }
            case NoSystemPermission:
                progress.dismiss();
                // not happening
                break;
            case ParentEmailNotFound: {
                progress.dismiss();
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Hey!");
                alert.setCancelable(false);
                alert.setMessage("To enable Push Notifications in KWS you'll need to provide a parent's email.");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);

                final AlertDialog.Builder ok = alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString() != null && !input.getText().toString().equals("")) {
                            progress.show();
                            KWS.sdk.submitParentEmail(input.getText().toString());
                        } else {
                            dialog.dismiss();
                            KWSSimpleAlert.getInstance().show(getContext(), "Hey!", "You must input a valid parent email!", "Got it!");
                        }
                    }
                });
                final AlertDialog.Builder cancel = alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
            }
            case ParentEmailInvalid: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Ups!", "You must input a valid parent email!", "Got it!");
                break;
            }
            case FirebaseNotSetup: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Ups!", "Could not continue process since Firebase is not properly setup!", "Got it!");
                break;
            }
            case FirebaseCouldNotGetToken: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Ups!", "Could not continue process since Firebase could not obtain a valid token.", "Got it!");
                break;
            }
            case NetworkError: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Ups!", "An un-identified error occured, and this user could not be registered for Remote Notifications in KWS.", "Got it!");
                break;
            }
            case CouldNotUnsubscribeInKWS: {
                progress.dismiss();
                KWSSimpleAlert.getInstance().show(getContext(), "Ups!", "Could not ubsubscribe user from Remote Notifications. Probably already unsubscribed!", "Got it!");
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

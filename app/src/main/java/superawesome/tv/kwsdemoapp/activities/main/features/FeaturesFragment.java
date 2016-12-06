package superawesome.tv.kwsdemoapp.activities.main.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.oauth.KWSLoggedUser;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.getappdata.GetAppDataActivity;
import superawesome.tv.kwsdemoapp.activities.leader.LeaderboardActivity;
import superawesome.tv.kwsdemoapp.activities.login.LoginActivity;
import superawesome.tv.kwsdemoapp.activities.user.UserActivity;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class FeaturesFragment extends Fragment {

    // private constants
    private static final int LOGOUT_REQ_CODE = 112;
    private static final int AUTH_REQ_CODE = 113;

    private static final String CLIENT = "sa-mobile-app-sdk-client-0";
    private static final String SECRET = "_apikey_5cofe4ppp9xav2t9";
    private static final String API = "https://kwsapi.demo.superawesome.tv/";

    private RxDataSource <GenericViewModel> dataSource = null;

    // constructor
    public FeaturesFragment() {
        // do nothing
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KWS.sdk.startSession(getContext(), CLIENT, SECRET, API);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get views
        View view = inflater.inflate(R.layout.fragment_features, container, false);

        ListView featureListView = (ListView) view.findViewById(R.id.FeaturesListView);

        Context context = getContext();

        getFeatures()
                .toList()
                .subscribe(genericViewModels -> {

                    dataSource = RxDataSource.from(context, genericViewModels);
                    dataSource
                            .bindTo(featureListView)
                            .customiseRow(R.layout.listitem_features_auth, FeaturesAuthRowViewModel.class, (viewModel, view1) -> {

                                Button authButton = (Button) view1.findViewById(R.id.authAction);
                                Button docButton = (Button) view1.findViewById(R.id.authDocs);

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;
                                KWSLoggedUser local = KWS.sdk.getLoggedUser();

                                authButton.setText(isLogged ?
                                        context.getString(R.string.feature_cell_auth_button_1_loggedin) + " " + local.username :
                                        context.getString(R.string.feature_cell_auth_button_1_loggedout));

                                RxView.clicks(authButton).subscribe(this::authFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);

                            })
                            .customiseRow(R.layout.listitem_features_notif, FeaturesNotifRowViewModel.class, (viewModel, view12) -> {

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;
                                boolean isRegistered = KWS.sdk.getLoggedUser() != null && KWS.sdk.getLoggedUser().isRegisteredForNotifications();

                                Button docButton = (Button) view12.findViewById(R.id.notifDocs);
                                Button subButton = (Button) view12.findViewById(R.id.notifEnableDisable);

                                subButton.setEnabled(isLogged);
                                subButton.setText(isRegistered ?
                                        context.getString(R.string.feature_cell_notif_button_1_disable) :
                                        context.getString(R.string.feature_cell_notif_button_1_enable));

                                RxView.clicks(subButton).subscribe(this::notificationFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);

                            })
                            .customiseRow(R.layout.listitem_features_perm, FeaturesPermRowViewModel.class, (viewModel, view13) -> {

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;

                                Button addPermission = (Button) view13.findViewById(R.id.permissionAddButton);
                                Button docButton = (Button) view13.findViewById(R.id.permissionDocs);

                                addPermission.setEnabled(isLogged);

                                RxView.clicks(addPermission).subscribe(this::permissionFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);
                            })
                            .customiseRow(R.layout.listitem_features_events, FeaturesEventsRowViewModel.class, (viewModel, view14) -> {

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;

                                Button add20Points = (Button) view14.findViewById(R.id.pointsAdd20);
                                Button sub10Points = (Button) view14.findViewById(R.id.pointsSub10);
                                Button getScore = (Button) view14.findViewById(R.id.getScore);
                                Button seeLeaders = (Button) view14.findViewById(R.id.pointsLeader);
                                Button docButton = (Button) view14.findViewById(R.id.pointsDocs);

                                add20Points.setEnabled(isLogged);
                                sub10Points.setEnabled(isLogged);
                                seeLeaders.setEnabled(isLogged);
                                getScore.setEnabled(isLogged);

                                RxView.clicks(add20Points).subscribe(this::add20PointsFunc);
                                RxView.clicks(sub10Points).subscribe(this::sub10PointsFunc);
                                RxView.clicks(getScore).subscribe(this::getScoreFunc);
                                RxView.clicks(seeLeaders).subscribe(this::leaderboardFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);
                            })
                            .customiseRow(R.layout.listitem_features_invite, FeaturesInviteRowViewModel.class, (viewModel, view15) -> {

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;

                                Button docButton = (Button) view15.findViewById(R.id.inviteDocs);
                                Button addButton = (Button) view15.findViewById(R.id.inviteAddUser);

                                addButton.setEnabled(isLogged);

                                RxView.clicks(addButton).subscribe(this::addUserFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);
                            })
                            .customiseRow(R.layout.listitem_features_appdata, FeaturesAppDataRowViewModel.class, (viewModel, view16) -> {

                                boolean isLogged = KWS.sdk.getLoggedUser() != null;

                                Button docButton = (Button) view16.findViewById(R.id.appdataDocs);
                                Button seeButton = (Button) view16.findViewById(R.id.appdataSee);

                                seeButton.setEnabled(isLogged);

                                RxView.clicks(seeButton).subscribe(this::appDataFunc);
                                RxView.clicks(docButton).subscribe(this::documentationFunc);
                            })
                            .update();

                });

        // signal change
        KWS.sdk.isRegistered(getContext(), b -> dataSource.update());

        return view;
    }

    private void documentationFunc (Void v) {
        String url = "http://doc.superawesome.tv/sa-kws-android-sdk/latest/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void authFunc (Void v) {
        boolean isLogged = KWS.sdk.getLoggedUser() != null;
        Intent authIntent = new Intent(getActivity(), !isLogged ? LoginActivity.class : UserActivity.class);
        startActivityForResult(authIntent, !isLogged ? AUTH_REQ_CODE : LOGOUT_REQ_CODE);
    }

    private void add20PointsFunc (Void v) {
        KWS.sdk.triggerEvent(getContext(), "GabrielAdd20ForAwesomeApp", 20, b -> {
            if (b) {
                alert(getString(R.string.feature_event_add20_popup_success_title),
                        getString(R.string.feature_event_add20_popup_success_message));
            } else {
                // failure
            }
        });
    }

    private void sub10PointsFunc (Void v) {
        KWS.sdk.triggerEvent(getContext(), "GabrielSub10ForAwesomeApp", -10, b -> {
            if (b) {
                alert(getString(R.string.feature_event_sub10_popup_success_title),
                        getString(R.string.feature_event_sub10_popup_success_message));
            } else {
                // failure
            }
        });
    }

    private void getScoreFunc (Void v) {
        KWS.sdk.getScore(getContext(), kwsScore -> {
            if (kwsScore != null) {
                alert(getString(R.string.feature_event_getscore_success_title),
                        getString(R.string.feature_event_getscore_success_message, kwsScore.rank, kwsScore.score));
            } else {
                // failure
            }
        });
    }

    private void leaderboardFunc (Void v) {
        Intent leaderBoard = new Intent(getActivity(), LeaderboardActivity.class);
        startActivity(leaderBoard);
    }

    private void addUserFunc (Void v) {
        SAAlert.getInstance().show(getActivity(),
                getString(R.string.feature_friend_email_popup_title),
                getString(R.string.feature_friend_email_popup_message),
                getString(R.string.feature_friend_email_popup_submit),
                getString(R.string.feature_friend_email_popup_cancel),
                true,
                32,
                (button, email) -> {
                    if (button == 0) {

                        SAProgressDialog.getInstance().showProgress(getActivity());

                        KWS.sdk.inviteUser(getContext(), email, b -> {
                            SAProgressDialog.getInstance().hideProgress();

                            if (b) {
                                alert(getString(R.string.feature_friend_email_popup_success_title),
                                        getString(R.string.feature_friend_email_popup_success_message, email));
                            } else {
                                alert(getString(R.string.feature_friend_email_popup_error_title),
                                        getString(R.string.feature_friend_email_popup_error_message, email));
                            }
                        });
                    }
                });
    }

    private void appDataFunc (Void v) {
        Intent getAppData = new Intent(getActivity(), GetAppDataActivity.class);
        startActivity(getAppData);
    }

    private void permissionFunc (Void v) {
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
                "Send newsletter"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(getString(R.string.feature_perm_alert_title));
        builder.setItems(titles, (dialog, which) -> {
            KWSPermissionType[] requestedType = new KWSPermissionType[] { types[which] };
            SAProgressDialog.getInstance().showProgress(getActivity());

            KWS.sdk.requestPermission(getContext(), requestedType, kwsPermissionStatus -> {

                SAProgressDialog.getInstance().hideProgress();

                switch (kwsPermissionStatus) {
                    case Success: {
                        alert(getString(R.string.feature_perm_popup_success_title),
                                getString(R.string.feature_perm_popup_success_message));
                        break;
                    }
                    case NoParentEmail: {
                        // this should not happen anymore, I think
                        break;
                    }
                    case NeworkError: {
                        alert(getString(R.string.feature_perm_popup_error_title),
                                getString(R.string.feature_perm_popup_error_message));
                        break;
                    }
                }
            });
        });
        builder.show();
    }

    private void notificationFunc (Void v) {

        boolean isRegistered = KWS.sdk.getLoggedUser() != null && KWS.sdk.getLoggedUser().isRegisteredForNotifications();

        if (isRegistered) {
            SAProgressDialog.getInstance().showProgress(getActivity());
            KWS.sdk.unregister(getContext(), b -> {
                SAProgressDialog.getInstance().hideProgress();
                if (b) {
                    dataSource.update();
                    alert(getString(R.string.feature_notif_unreg_popup_success_title),
                            getString(R.string.feature_notif_unreg_popup_success_message));
                } else {
                    alert(getString(R.string.feature_notif_unreg_popup_error_title),
                            getString(R.string.feature_notif_unreg_popup_error_message));
                }
            });
        } else {
            SAProgressDialog.getInstance().showProgress(getActivity());
            KWS.sdk.register(getContext(), kwsNotificationStatus -> {
                SAProgressDialog.getInstance().hideProgress();
                switch (kwsNotificationStatus) {
                    case ParentDisabledNotifications:
                    case UserDisabledNotifications:
                    case NoParentEmail:
                    case FirebaseNotSetup:
                    case FirebaseCouldNotGetToken: {
                        break;
                    }
                    case NetworkError: {
                        alert(getString(R.string.feature_notif_reg_popup_error_title),
                                getString(R.string.feature_notif_reg_popup_error_message));
                        break;
                    }
                    case Success: {
                        dataSource.update();
                        alert(getString(R.string.feature_notif_reg_popup_success_title),
                                getString(R.string.feature_notif_reg_popup_success_message));
                        break;
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // no matter if requestCode == LOGOUT_REQ_CODE or AUTH_REQ_CODE
            // just do an update
            dataSource.update();
        }
    }

    private void alert(String title, String message) {
        SAAlert.getInstance().show(
                getActivity(),
                title,
                message,
                getString(R.string.feature_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }

    private Observable<GenericViewModel> getFeatures () {
        return Observable.create(subscriber -> {

            // add data
            List<GenericViewModel> rows = new ArrayList<>();
            rows.add(new FeaturesAuthRowViewModel());
            rows.add(new FeaturesNotifRowViewModel());
            rows.add(new FeaturesPermRowViewModel());
            rows.add(new FeaturesEventsRowViewModel());
            rows.add(new FeaturesInviteRowViewModel());
            rows.add(new FeaturesAppDataRowViewModel());

            // emmit it
            for (GenericViewModel row : rows) {
                subscriber.onNext(row);
            }
            subscriber.onCompleted();

        });
    }
}

package superawesome.tv.kwsdemoapp.activities.main.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Arrays;

import gabrielcoman.com.rxdatasource.RxDataSource;
import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.oauth.KWSLoggedUser;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.base.BaseFragment;
import superawesome.tv.kwsdemoapp.activities.getappdata.GetAppDataActivity;
import superawesome.tv.kwsdemoapp.activities.leader.LeaderboardActivity;
import superawesome.tv.kwsdemoapp.activities.login.LoginActivity;
import superawesome.tv.kwsdemoapp.activities.user.UserActivity;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;

public class FeaturesFragment extends BaseFragment {

    // private constants
    private static final int LOGOUT_REQ_CODE = 112;
    private static final int AUTH_REQ_CODE = 113;

    private static final String CLIENT = "sa-mobile-app-sdk-client-0";
    private static final String SECRET = "_apikey_5cofe4ppp9xav2t9";
    private static final String API = "https://kwsapi.demo.superawesome.tv/";

    private RxDataSource <GenericViewModel> dataSource = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get views
        View view = inflater.inflate(R.layout.fragment_features, container, false);

        KWS.sdk.startSession(getContext(), CLIENT, SECRET, API);

        ListView featureListView = (ListView) view.findViewById(R.id.FeaturesListView);

        Context context = getContext();

        Observable.from(Arrays.asList(
                new FeaturesAuthRowViewModel(),
                new FeaturesNotifRowViewModel(),
                new FeaturesPermRowViewModel(),
                new FeaturesEventsRowViewModel(),
                new FeaturesInviteRowViewModel(),
                new FeaturesAppDataRowViewModel()
        )).toList().subscribe(genericViewModels -> {

            dataSource = RxDataSource.from(context, genericViewModels);
            dataSource
                    .bindTo(featureListView)
                    //
                    // the Login & Logout Row
                    .customiseRow(R.layout.listitem_features_auth, FeaturesAuthRowViewModel.class, (viewModel, v) -> {

                        Button authButton = (Button) v.findViewById(R.id.authAction);
                        Button docButton = (Button) v.findViewById(R.id.authDocs);

                        final boolean isLogged = KWS.sdk.getLoggedUser() != null;
                        KWSLoggedUser local = KWS.sdk.getLoggedUser();

                        authButton.setText(isLogged ?
                                context.getString(R.string.feature_cell_auth_button_1_loggedin) + " " + local.username :
                                context.getString(R.string.feature_cell_auth_button_1_loggedout));

                        RxView.clicks(authButton).subscribe(aVoid -> {
                            Intent authIntent = new Intent(getActivity(), !isLogged ? LoginActivity.class : UserActivity.class);
                            startActivityForResult(authIntent, !isLogged ? AUTH_REQ_CODE : LOGOUT_REQ_CODE);
                        });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    //
                    // the Notifications Row
                    .customiseRow(R.layout.listitem_features_notif, FeaturesNotifRowViewModel.class, (viewModel, v) -> {

                        final boolean isLogged = KWS.sdk.getLoggedUser() != null;
                        final boolean isRegistered = KWS.sdk.getLoggedUser() != null && KWS.sdk.getLoggedUser().isRegisteredForNotifications();

                        Button docButton = (Button) v.findViewById(R.id.notifDocs);
                        Button subButton = (Button) v.findViewById(R.id.notifEnableDisable);

                        subButton.setEnabled(isLogged);
                        subButton.setText(isRegistered ?
                                context.getString(R.string.feature_cell_notif_button_1_disable) :
                                context.getString(R.string.feature_cell_notif_button_1_enable));

                        Observable <Void> subButtonRx = RxView.clicks(subButton).share();

                        subButtonRx.filter(aVoid -> isRegistered)
                                .flatMap(aVoid -> RxKWS.disableNotifications(context))
                                .subscribe(aBoolean -> {

                                    dataSource.update();
                                    alert(getString(R.string.feature_notif_unreg_popup_success_title),
                                            getString(R.string.feature_notif_unreg_popup_success_message));
                                });

                        subButtonRx.filter(aVoid -> !isRegistered)
                                .flatMap(aVoid -> RxKWS.enableNotifications(context))
                                .filter(aBoolean -> aBoolean)
                                .subscribe(aBoolean -> {
                                    dataSource.update();
                                    alert(getString(R.string.feature_notif_reg_popup_success_title),
                                            getString(R.string.feature_notif_reg_popup_success_message));
                                });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    //
                    // The Permissions Row
                    .customiseRow(R.layout.listitem_features_perm, FeaturesPermRowViewModel.class, (viewModel, v) -> {

                        boolean isLogged = KWS.sdk.getLoggedUser() != null;

                        Button addPermission = (Button) v.findViewById(R.id.permissionAddButton);
                        Button docButton = (Button) v.findViewById(R.id.permissionDocs);

                        addPermission.setEnabled(isLogged);

                        RxView.clicks(addPermission)
                                .flatMap(aVoid -> RxKWS.requestPermissionPopup(context))
                                .flatMap(kwsPermissionTypes -> RxKWS.requestPermission(context, kwsPermissionTypes))
                                .filter(aBoolean -> aBoolean)
                                .subscribe(aBoolean -> {
                                    alert(getString(R.string.feature_perm_popup_success_title),
                                            getString(R.string.feature_perm_popup_success_message));
                                });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    //
                    // The Events Row
                    .customiseRow(R.layout.listitem_features_events, FeaturesEventsRowViewModel.class, (viewModel, v) -> {

                        boolean isLogged = KWS.sdk.getLoggedUser() != null;

                        Button add20Points = (Button) v.findViewById(R.id.pointsAdd20);
                        Button sub10Points = (Button) v.findViewById(R.id.pointsSub10);
                        Button getScore = (Button) v.findViewById(R.id.getScore);
                        Button seeLeaders = (Button) v.findViewById(R.id.pointsLeader);
                        Button docButton = (Button) v.findViewById(R.id.pointsDocs);

                        add20Points.setEnabled(isLogged);
                        sub10Points.setEnabled(isLogged);
                        seeLeaders.setEnabled(isLogged);
                        getScore.setEnabled(isLogged);

                        RxView.clicks(add20Points)
                                .flatMap(aVoid -> RxKWS.triggerEvent(context, "GabrielAdd20ForAwesomeApp"))
                                .filter(aBoolean -> aBoolean)
                                .subscribe(aBoolean -> {
                                    alert(getString(R.string.feature_event_add20_popup_success_title),
                                            getString(R.string.feature_event_add20_popup_success_message));
                                });

                        RxView.clicks(sub10Points)
                                .flatMap(aVoid -> RxKWS.triggerEvent(context, "GabrielSub10ForAwesomeApp"))
                                .filter(aBoolean -> aBoolean)
                                .subscribe(aBoolean -> {
                                    alert(getString(R.string.feature_event_sub10_popup_success_title),
                                            getString(R.string.feature_event_sub10_popup_success_message));
                                });

                        RxView.clicks(getScore)
                                .flatMap(aVoid -> RxKWS.getScore(context))
                                .filter(kwsScore -> kwsScore != null)
                                .subscribe(kwsScore -> {
                                    alert(getString(R.string.feature_event_getscore_success_title),
                                            getString(R.string.feature_event_getscore_success_message, kwsScore.rank, kwsScore.score));
                                });


                        RxView.clicks(seeLeaders).subscribe(aVoid -> {
                            Intent leaderBoard = new Intent(getActivity(), LeaderboardActivity.class);
                            startActivity(leaderBoard);
                        });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    //
                    // the Invite Row
                    .customiseRow(R.layout.listitem_features_invite, FeaturesInviteRowViewModel.class, (viewModel, v) -> {

                        boolean isLogged = KWS.sdk.getLoggedUser() != null;

                        Button docButton = (Button) v.findViewById(R.id.inviteDocs);
                        Button addButton = (Button) v.findViewById(R.id.inviteAddUser);

                        addButton.setEnabled(isLogged);

                        RxView.clicks(addButton)
                                .flatMap(aVoid -> RxKWS.inviteFriendPopup(context))
                                .flatMap(s -> RxKWS.inviteFriend(context, s))
                                .filter(aBoolean -> aBoolean)
                                .subscribe(aBoolean -> {
                                    alert(getString(R.string.feature_friend_email_popup_success_title),
                                            getString(R.string.feature_friend_email_popup_success_message));
                                });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    //
                    // The App Data Row
                    .customiseRow(R.layout.listitem_features_appdata, FeaturesAppDataRowViewModel.class, (viewModel, v) -> {

                        boolean isLogged = KWS.sdk.getLoggedUser() != null;

                        Button docButton = (Button) v.findViewById(R.id.appdataDocs);
                        Button seeButton = (Button) v.findViewById(R.id.appdataSee);

                        seeButton.setEnabled(isLogged);

                        RxView.clicks(seeButton).subscribe(aVoid -> {
                            Intent getAppData = new Intent(getActivity(), GetAppDataActivity.class);
                            startActivity(getAppData);
                        });

                        RxView.clicks(docButton).subscribe(this::documentationFunc);

                    })
                    .update();
        });

        KWS.sdk.isRegistered(getContext(), b -> dataSource.update());

        setOnActivityResult((requestCode, resultCode) -> {
            if (resultCode == Activity.RESULT_OK) {
                dataSource.update();
            }
        });

        return view;
    }

    private void documentationFunc (Void v) {
        String url = "http://doc.superawesome.tv/sa-kws-android-sdk/latest/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
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
}

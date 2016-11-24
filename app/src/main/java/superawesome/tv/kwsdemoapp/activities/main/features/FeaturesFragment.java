package superawesome.tv.kwsdemoapp.activities.main.features;

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
import android.widget.ListView;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import rx.functions.Action1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.activities.getappdata.GetAppDataActivity;
import superawesome.tv.kwsdemoapp.activities.leader.LeaderboardActivity;
import superawesome.tv.kwsdemoapp.activities.login.LoginActivity;
import superawesome.tv.kwsdemoapp.activities.user.UserActivity;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class FeaturesFragment extends Fragment {

    private static final String CLIENT = "sa-mobile-app-sdk-client-0";
    private static final String SECRET = "_apikey_5cofe4ppp9xav2t9";
    private static final String API = "https://kwsapi.demo.superawesome.tv/";

    private final String DOCSURL = "http://doc.superawesome.tv/sa-kws-android-sdk/latest/";

    private FeaturesAdapter adapter = null;

    // constructor
    public FeaturesFragment() {
        // do nothing
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KWS.sdk.startSession(getContext(), CLIENT, SECRET, API);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // get views
        View view = inflater.inflate(R.layout.fragment_features, container, false);

        // create adapter
        adapter = new FeaturesAdapter(getContext());

        // set adapter
        ListView featureListView = (ListView) view.findViewById(R.id.FeaturesListView);
        featureListView.setAdapter(adapter);

        // get data source
        FeaturesSource source = new FeaturesSource();
        source.getFeatures().
                toList().
                subscribe(viewModels -> {
                    adapter.updateData(viewModels);
                }, throwable -> {
                    // do nothing
                }, () -> {
                    // do nothing
                });

        KWS.sdk.isRegistered(getContext(), b -> adapter.notifyDataSetChanged());

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("DOCS_NOTIFICATION")).
                subscribe(notif -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DOCSURL));
                    getActivity().startActivity(browserIntent);
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("AUTH_NOTIFICATION")).
                subscribe(notif -> {
                    boolean isLogged = KWS.sdk.getLoggedUser() != null;
                    Intent authIntent = new Intent(getActivity(), !isLogged ? LoginActivity.class : UserActivity.class);
                    getActivity().startActivity(authIntent);
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("RECEIVED_SIGNUP")).
                subscribe(s -> {
                    adapter.notifyDataSetChanged();
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("RECEIVED_LOGOUT")).
                subscribe(s -> {
                    adapter.notifyDataSetChanged();
                });

        UniversalNotifier.getObservable().
                filter(nofif -> nofif.equals("ADD_20_POINTS")).
                subscribe(s -> {
                    KWS.sdk.triggerEvent(getContext(), "GabrielAdd20ForAwesomeApp", 20, b -> {
                        if (b) {
                            alert(getString(R.string.feature_event_add20_popup_success_title),
                                    getString(R.string.feature_event_add20_popup_success_message));
                        } else {
                            // failure
                        }
                    });
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SUB_10_POINTS")).
                subscribe(s -> {
                    KWS.sdk.triggerEvent(getContext(), "GabrielSub10ForAwesomeApp", -10, b -> {
                        if (b) {
                        alert(getString(R.string.feature_event_sub10_popup_success_title),
                                getString(R.string.feature_event_sub10_popup_success_message));
                        } else {
                            // failure
                        }
                    });
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("GET_SCORE")).
                subscribe(s -> {
                    KWS.sdk.getScore(getContext(), kwsScore -> {
                        if (kwsScore != null) {
                            alert(getString(R.string.feature_event_getscore_success_title),
                                    getString(R.string.feature_event_getscore_success_message, kwsScore.rank, kwsScore.score));
                        } else {
                            // failure
                        }
                    });
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SEE_LEADERBOARD")).
                subscribe(s -> {
                    Intent leaderboard = new Intent(getActivity(), LeaderboardActivity.class);
                    getActivity().startActivity(leaderboard);
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("REQUEST_PERMISSION")).
                subscribe(s -> {
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
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SUBSCRIBE_NOTIFICATION")).
                subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        boolean isRegistered = KWS.sdk.getLoggedUser() != null && KWS.sdk.getLoggedUser().isRegisteredForNotifications();

                        if (isRegistered) {
                            SAProgressDialog.getInstance().showProgress(getActivity());
                            KWS.sdk.unregister(getContext(), b -> {
                                SAProgressDialog.getInstance().hideProgress();
                                if (b) {
                                    adapter.notifyDataSetChanged();
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
                                        adapter.notifyDataSetChanged();
                                        alert(getString(R.string.feature_notif_reg_popup_success_title),
                                                getString(R.string.feature_notif_reg_popup_success_message));
                                        break;
                                    }
                                }
                            });
                        }
                    }
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("ADD_USER_NOTIFICATION")).
                subscribe(s -> {
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
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SEE_APP_DATA_NOTIFICATION")).
                subscribe(s -> {
                    Intent getappdata = new Intent(getActivity(), GetAppDataActivity.class);
                    getActivity().startActivity(getappdata);
                });


        return view;
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

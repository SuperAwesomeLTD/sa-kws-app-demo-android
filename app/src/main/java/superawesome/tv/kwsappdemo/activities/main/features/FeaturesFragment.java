package superawesome.tv.kwsappdemo.activities.main.features;

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

import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.process.KWSErrorType;
import kws.superawesome.tv.kwssdk.services.kws.KWSInviteUserInterface;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.appdata.GetAppDataActivity;
import superawesome.tv.kwsappdemo.activities.leader.LeaderboardActivity;
import superawesome.tv.kwsappdemo.activities.signup.SignUpActivity;
import superawesome.tv.kwsappdemo.activities.user.UserActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.UniversalNotifier;
import superawesome.tv.kwsappdemo.aux.ViewModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FeaturesFragment extends Fragment {

    private final String DOCSURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    private FeaturesAdapter adapter = null;

    // for permissions
    private CharSequence currentTitle = null;
    private KWSPermissionType[] requestedType = null;

    // constructor
    public FeaturesFragment() {
        // do nothing
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KWS.sdk.setApplicationContext(getContext().getApplicationContext());
        KWSSingleton.getInstance().start();
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


        // set registered or not
        KWSSingleton.getInstance().getIsRegistered().
                subscribe(aBoolean -> {
                    KWSSingleton.getInstance().markUserRegistrationStatus(aBoolean);
                    adapter.notifyDataSetChanged();
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("DOCS_NOTIFICATION")).
                subscribe(notif -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DOCSURL));
                    getActivity().startActivity(browserIntent);
                });
        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("AUTH_NOTIFICATION")).
                subscribe(notif -> {
                    boolean isLogged = KWSSingleton.getInstance().isUserLogged();
                    Intent authIntent = new Intent(getActivity(), !isLogged ? SignUpActivity.class : UserActivity.class);
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
                    KWS.sdk.triggerEvent("GabrielAdd20ForAwesomeApp", 20, "You just earned 20 points!", b -> {
                        if (b) {
                            SAAlert.getInstance().show(getContext(), "Congrats!", "You just earned 20 points!", "Great!", null, false, 0, null);
                        }
                    });
                });
        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SUB_10_POINTS")).
                subscribe(s -> {
                    KWS.sdk.triggerEvent("GabrielSub10ForAwesomeApp", -10, "You just lost 10 points!", b -> {
                        if (b) {
                            SAAlert.getInstance().show(getContext(), "Hey!", "You just lost 10 points!", "Ok!", null, false, 0, null);
                        }
                    });
                });
        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("GET_SCORE")).
                subscribe(s -> {
                    KWS.sdk.getScore(kwsScore -> {
                        if (kwsScore != null) {
                            SAAlert.getInstance().show(getContext(), "Hey!", "You're ranked " + kwsScore.rank + " with " + kwsScore.score + " points!", "Great!", null, false, 0, null);
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
                    builder.setTitle("Ask for a permission");
                    builder.setItems(titles, (dialog, which) -> {
                        currentTitle = titles[which];
                        requestedType = new KWSPermissionType[] { types[which] };
                        SAProgressDialog.getInstance().showProgress(getActivity());
                        KWS.sdk.requestPermission(requestedType, this::permissionCallback);
                    });
                    builder.show();
                });

        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("SUBSCRIBE_NOTIFICATION")).
                subscribe(s -> {
                    boolean isRegisterd = KWSSingleton.getInstance().isUserMarkedAsRegistered();
                    if (isRegisterd) {
                        SAProgressDialog.getInstance().showProgress(getActivity());
                        KWS.sdk.unregister(b -> {
                            SAProgressDialog.getInstance().hideProgress();
                            if (b) {
                                adapter.notifyDataSetChanged();
                                KWSSingleton.getInstance().markUserAsUnregistered();
                                SAAlert.getInstance().show(getActivity(), "Hey!", "You successfully unregistered for remote notifications!", "Got it!", null, false, 0, null);
                            } else {
                                SAAlert.getInstance().show(getActivity(), "Hey!", "There was an error trying to unregister for remote notifications. Try again later!", "Got it!", null, false, 0, null);
                            }
                        });
                    } else {
                        SAProgressDialog.getInstance().showProgress(getActivity());
                        KWS.sdk.register(FeaturesFragment.this::registerCallback);
                    }
                });
        UniversalNotifier.getObservable().
                filter(notif -> notif.equals("ADD_USER_NOTIFICATION")).
                subscribe(s -> {
                    SAAlert.getInstance().show(getActivity(), "Friend email", "Invite your friend via email", "Invite", "Cancel", true, 32, (button, email) -> {
                        if (button == 0) {

                            SAProgressDialog.getInstance().showProgress(getActivity());

                            KWS.sdk.inviteUser(email, invited -> {

                                SAProgressDialog.getInstance().hideProgress();

                                if (invited) {
                                    SAAlert.getInstance().show(getContext(), "Hey!", "You successfully invited " + email + " in KWS.", "Thanks!", null, false, 0, null);
                                } else {
                                    SAAlert.getInstance().show(getContext(), "Hey!", "An error occurred while inviting " + email + " in KWS. Plase make sure the email is valid.", "Got it!", null, false, 0, null);
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

    private void permissionCallback (boolean success, boolean requested) {
        SAProgressDialog.getInstance().hideProgress();
        if (!success) {
            SAAlert.getInstance().show(getContext(), "Hey!", "Something happened while asking for permissions", "Got it!", null, false, 0, null);
        } else {
            if (requested) {
                SAAlert.getInstance().show(getContext(), "Great!", "You've successfully asked for permission for " + currentTitle, "Got it!", null, false, 0 , null);
            } else {
                SAAlert.getInstance().show(getActivity(), "Parent email", "You have to add your parent email", "Submit", "Cancel", true, 32, (button, email) -> {
                    if (button == 0) {
                        KWS.sdk.submitParentEmail(email, b -> {
                            if (b) {
                                SAProgressDialog.getInstance().showProgress(getActivity());
                                KWS.sdk.requestPermission(requestedType, FeaturesFragment.this::permissionCallback);
                            }
                        });
                    }
                });
            }
        }
    }

    private void registerCallback (boolean success, KWSErrorType kwsErrorType) {
        SAProgressDialog.getInstance().hideProgress();
        if (success) {
            KWSSingleton.getInstance().markUserRegistrationStatus(true);
            adapter.notifyDataSetChanged();
            SAAlert.getInstance().show(getContext(), "Hey!", "You successfully registered for remote notifications in KWS.", "Got it!", null, false, 0, null);
        } else {
            if (kwsErrorType == KWSErrorType.UserHasNoParentEmail) {
                SAAlert.getInstance().show(getActivity(), "Parent email", "You have to add your parent email", "Submit", "Cancel", true, 32, (button, email) -> {
                    if (button == 0) {
                        KWS.sdk.submitParentEmail(email, b -> {
                            if (b) {
                                SAProgressDialog.getInstance().showProgress(getActivity());
                                KWS.sdk.register(FeaturesFragment.this::registerCallback);
                            }
                        });

                    }
                });
            } else {
                SAAlert.getInstance().show(getActivity(), "Hey!", "Error occurred trying to register for Push Notifications: " + kwsErrorType.toString(), "Got it!", null, false, 0, null);
            }
        }
    }
}

package superawesome.tv.kwsdemoapp.aux;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.models.user.KWSScore;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 06/12/2016.
 */

public class RxKWS {

    public static Observable<KWSAppData> getAppData (Context context) {

        return Observable.create(subscriber -> {

                KWS.sdk.getAppData(context, list -> {

                    for (KWSAppData data : list) {
                        subscriber.onNext(data);
                    }
                    subscriber.onCompleted();

                });

        });
    }

    public static Observable <KWSLeader> getLeaderBoard(Context context) {

        return Observable.create(subscriber -> {

            KWS.sdk.getLeaderBoard(context, list -> {

                for (KWSLeader leader : list) {
                    subscriber.onNext(leader);
                }
                subscriber.onCompleted();

            });

        });

    }

    public static Observable<Boolean> login (Context context, final String username, final String password) {
        return Observable.create(subscriber -> {

            KWS.sdk.loginUser(context, username, password, kwsAuthUserStatus -> {
                switch (kwsAuthUserStatus) {
                    case Success: {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        break;
                    }
                    case InvalidCredentials: {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        break;
                    }
                    case NetworkError: {
                        subscriber.onError(new Throwable());
                        break;
                    }
                }
            });

        });
    }

    public static Observable<Boolean> submitData(Context context, final String name, final Integer value) {

        return Observable.create(subscriber -> {

            KWS.sdk.setAppData(context, name, value, success -> {
                if (success) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable());
                }
            });

        });
    }

    public static rx.Observable<Boolean> signUp (Context context, final String username, final String password, final String dateOfBirth, final String parentEmail) {

        return rx.Observable.create(subscriber -> {

            KWS.sdk.createUser(context, username, password, dateOfBirth, "US", parentEmail, kwsCreateUserStatus -> {

                switch (kwsCreateUserStatus) {
                    case Success: {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        break;
                    }
                    case DuplicateUsername: {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        break;
                    }
                    case InvalidUsername:
                    case InvalidPassword:
                    case InvalidDateOfBirth:
                    case InvalidCountry:
                    case InvalidParentEmail:
                    case NetworkError:
                    case InvalidOperation: {
                        subscriber.onError(new Throwable());
                        break;
                    }
                }
            });

        });
    }

    public static Observable<KWSUser> getUser (Context context) {

        return Observable.create(subscriber -> {

            KWS.sdk.getUser(context, kwsUser -> {

                if (kwsUser == null) {
                    subscriber.onError(new Throwable());
                } else {
                    subscriber.onNext(kwsUser);
                    subscriber.onCompleted();
                }

            });

        });

    }

    public static Observable <Boolean> triggerEvent (Context context, String evt) {

        return Observable.create(subscriber -> {

            KWS.sdk.triggerEvent(context, evt, 20, b -> {

                subscriber.onNext(b);
                subscriber.onCompleted();

            });

        });

    }

    public static Observable <KWSScore> getScore (Context context) {

        return Observable.create(subscriber -> {

            KWS.sdk.getScore(context, kwsScore -> {

                subscriber.onNext(kwsScore);
                subscriber.onCompleted();

            });

        });

    }

    public static Observable <String> inviteFriendPopup (Context context) {

        return Observable.create(subscriber -> {

            SAAlert.getInstance().show(context,
                    context.getString(R.string.feature_friend_email_popup_title),
                    context.getString(R.string.feature_friend_email_popup_message),
                    context.getString(R.string.feature_friend_email_popup_submit),
                    context.getString(R.string.feature_friend_email_popup_cancel),
                    true,
                    32,
                    (button, email) -> {

                        if (button == 0) {
                            subscriber.onNext(email);
                            subscriber.onCompleted();
                        }
                    });

        });

    }

    public static Observable <Boolean> inviteFriend (Context context, String email) {

        return Observable.create(subscriber -> {

            KWS.sdk.inviteUser(context, email, b -> {

                subscriber.onNext(b);
                subscriber.onCompleted();

            });

        });

    }

    public static Observable <Boolean> disableNotifications (Context context) {

        return Observable.create(subscriber -> {

            KWS.sdk.unregister(context, b -> {

                subscriber.onNext(b);
                subscriber.onCompleted();

            });

        });

    }

    public static Observable <Boolean> enableNotifications (Context context) {

        return Observable.create(subscriber -> {

            KWS.sdk.register(context, kwsNotificationStatus -> {

                SAProgressDialog.getInstance().hideProgress();
                switch (kwsNotificationStatus) {
                    case ParentDisabledNotifications:
                    case UserDisabledNotifications:
                    case NoParentEmail:
                    case FirebaseNotSetup:
                    case FirebaseCouldNotGetToken:
                    case NetworkError: {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        break;
                    }
                    case Success: {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        break;
                    }
                }
            });

        });

    }

    public static Observable <KWSPermissionType[]> requestPermissionPopup (Context context) {

        return Observable.create(subscriber -> {

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

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.feature_perm_alert_title));
            builder.setItems(titles, (dialog, which) -> {

                KWSPermissionType[] requestedType = new KWSPermissionType[] { types[which] };
                subscriber.onNext(requestedType);
                subscriber.onCompleted();

            });
            builder.show();

        });

    }

    public static Observable<Boolean> requestPermission (Context context, KWSPermissionType[] types) {

        return Observable.create(subscriber -> {

            KWS.sdk.requestPermission(context, types, kwsPermissionStatus -> {

                switch (kwsPermissionStatus) {
                    case Success: {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                        break;
                    }
                    case NoParentEmail:
                    case NeworkError: {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                        break;
                    }
                }
            });

        });

    }
 }

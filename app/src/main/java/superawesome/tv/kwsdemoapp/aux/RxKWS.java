package superawesome.tv.kwsdemoapp.aux;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import rx.Observable;

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

    public static Observable <KWSLeader> getLeaderboard (Context context) {

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
}

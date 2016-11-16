package superawesome.tv.kwsdemoapp.activities.login;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;

/**
 * Created by gabriel.coman on 16/11/16.
 */
public class LoginSource {

    public Observable<Boolean> login (final Context context, final String username, final String password) {
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
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
}

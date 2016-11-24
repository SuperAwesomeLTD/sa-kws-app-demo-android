package superawesome.tv.kwsdemoapp.activities.signup;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;

public class SignUpSource {

    public rx.Observable<Boolean> signUp (final Context context, final String username, final String password, final String dateOfBirth, final String parentEmail) {
        return rx.Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {

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
}

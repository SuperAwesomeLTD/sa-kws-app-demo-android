package superawesome.tv.kwsdemoapp.activities.setappdata;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;

public class SetAppDataSource {

    public Observable<Boolean> submitData(final Context context, final String name, final Integer value) {
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {

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
}

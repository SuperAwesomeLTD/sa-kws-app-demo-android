package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import rx.Observable;

public class GetAppDataSource {

    public Observable<KWSAppData> getAppData (Context context) {
        return rx.Observable.create((Observable.OnSubscribe<KWSAppData>) subscriber -> {

            KWS.sdk.getAppData(context, list -> {
                for (KWSAppData data : list) {
                    subscriber.onNext(data);
                }
                subscriber.onCompleted();
            });
        });
    }

}

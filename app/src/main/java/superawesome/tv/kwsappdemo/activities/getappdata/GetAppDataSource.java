package superawesome.tv.kwsappdemo.activities.getappdata;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import rx.Observable;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class GetAppDataSource {

    public Observable<KWSAppData> getAppData () {
        return rx.Observable.create((Observable.OnSubscribe<KWSAppData>) subscriber -> {
            KWS.sdk.getAppData(list -> {
                for (KWSAppData data : list) {
                    subscriber.onNext(data);
                }
                subscriber.onCompleted();
            });
        });
    }

}

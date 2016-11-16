package superawesome.tv.kwsdemoapp.activities.getappdata;

import android.content.Context;

import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.appdata.KWSAppData;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetAppDataInterface;
import rx.Observable;

/**
 * Created by gabriel.coman on 31/08/16.
 */
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

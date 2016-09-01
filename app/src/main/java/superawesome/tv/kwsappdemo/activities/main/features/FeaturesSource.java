package superawesome.tv.kwsappdemo.activities.main.features;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class FeaturesSource {

    public Observable<ViewModel> getFeatures () {
        return Observable.create((Observable.OnSubscribe<ViewModel>) subscriber -> {

            // add data
            List<ViewModel> rows = new ArrayList<>();
            rows.add(new FeaturesAuthRowViewModel());
            rows.add(new FeaturesNotifRowViewModel());
            rows.add(new FeaturesPermRowViewModel());
            rows.add(new FeaturesEventsRowViewModel());
            rows.add(new FeaturesInviteRowViewModel());
            rows.add(new FeaturesAppDataRowViewModel());

            // emmit it
            for (ViewModel row : rows) {
                subscriber.onNext(row);
            }
            subscriber.onCompleted();

        });
    }
}

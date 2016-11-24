package superawesome.tv.kwsdemoapp.activities.main.features;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class FeaturesSource {

    public Observable<GenericViewModelInterface> getFeatures () {
        return Observable.create((Observable.OnSubscribe<GenericViewModelInterface>) subscriber -> {

            // add data
            List<GenericViewModelInterface> rows = new ArrayList<>();
            rows.add(new FeaturesAuthRowViewModel());
            rows.add(new FeaturesNotifRowViewModel());
            rows.add(new FeaturesPermRowViewModel());
            rows.add(new FeaturesEventsRowViewModel());
            rows.add(new FeaturesInviteRowViewModel());
            rows.add(new FeaturesAppDataRowViewModel());

            // emmit it
            for (GenericViewModelInterface row : rows) {
                subscriber.onNext(row);
            }
            subscriber.onCompleted();

        });
    }
}

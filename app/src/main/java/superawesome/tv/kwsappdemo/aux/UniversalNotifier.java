package superawesome.tv.kwsappdemo.aux;

import rx.subjects.PublishSubject;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class UniversalNotifier {

    private static PublishSubject<String> changeObservable = PublishSubject.create();;

    public static void postNotification (String notification) {
        changeObservable.onNext(notification);
    }

    public static PublishSubject<String> getObservable() {
        return changeObservable;
    }
}

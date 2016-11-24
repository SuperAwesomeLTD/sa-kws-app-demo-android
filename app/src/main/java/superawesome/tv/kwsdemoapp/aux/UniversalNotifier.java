package superawesome.tv.kwsdemoapp.aux;

import rx.subjects.PublishSubject;

public class UniversalNotifier {

    private static PublishSubject<String> changeObservable = PublishSubject.create();;

    public static void postNotification (String notification) {
        changeObservable.onNext(notification);
    }

    public static PublishSubject<String> getObservable() {
        return changeObservable;
    }
}

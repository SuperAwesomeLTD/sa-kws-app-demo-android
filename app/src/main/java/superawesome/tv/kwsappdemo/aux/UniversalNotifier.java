package superawesome.tv.kwsappdemo.aux;

import rx.subjects.PublishSubject;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class UniversalNotifier {

    private String notification = null;
    private PublishSubject<String> changeObservable = null;

    private static UniversalNotifier instance = new UniversalNotifier();
    public static UniversalNotifier getInstance(){
        return instance;
    }

    private UniversalNotifier () {
        changeObservable = PublishSubject.create();
    }

    public void postNotification (String notification) {
        this.notification = notification;
        changeObservable.onNext(notification);
    }

    public PublishSubject<String> getChangeObservable () {
        return changeObservable;
    }
}

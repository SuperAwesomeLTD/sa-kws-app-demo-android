package superawesome.tv.kwsappdemo.rxkws;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import kws.superawesome.tv.kwssdk.process.IsRegisteredInterface;
import rx.Observable;
import rx.Subscriber;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sanetwork.request.SANetwork;

// transform KWS into RX
public class RXKWS {

    public static rx.Observable<List<KWSLeader>> getLeaderboardObserver() {
        return rx.Observable.create((Observable.OnSubscribe<List<KWSLeader>>) subscriber -> {
                KWS.sdk.getLeaderBoard(list -> {
                    if (subscriber != null) {
                        if (list.size() > 0) {
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception());
                        }
                    }
                });
        });
    }

    public static rx.Observable<KWSUser> getUserDetailsObserver() {
        return rx.Observable.create((Observable.OnSubscribe<KWSUser>) subscriber -> {
                KWS.sdk.getUser(kwsUser -> {
                    if (subscriber != null) {
                        if (kwsUser != null) {
                            Log.d("SuperAwesome_RX", "Logged as " + kwsUser.username + "");
                            subscriber.onNext(kwsUser);
                            subscriber.onCompleted();
                        } else {
                            Log.d("SuperAwesome_RX", "Could not get data for user!");
                            subscriber.onError(new Exception());
                        }
                    }
                });
        });
    }

    public static rx.Observable<KWSModel> createUserObserver (Context context, JSONObject query, JSONObject header, JSONObject body) {
        return rx.Observable.create((Observable.OnSubscribe<KWSModel>) subscriber -> {
                SANetwork network = new SANetwork();
                network.sendPOST(context, "https://kwsdemobackend.herokuapp.com/create", query, header, body, (status, payload, success) -> {

                    if (!success) {
                        subscriber.onError(new Throwable());
                    } else {
                        JSONObject jsonObject = SAJsonParser.newObject(payload);
                        KWSModel model = new KWSModel(jsonObject);

                        if (model.status == 1) {
                            subscriber.onNext(model);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Throwable());
                        }
                    }
                });
        });
    }

    public static rx.Observable<Boolean> getIsRegistered () {
        return  rx.Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            KWS.sdk.isRegistered(b -> {
                if (subscriber != null) {
                    subscriber.onNext(b);
                    subscriber.onCompleted();
                }
            });
        });
    }
}

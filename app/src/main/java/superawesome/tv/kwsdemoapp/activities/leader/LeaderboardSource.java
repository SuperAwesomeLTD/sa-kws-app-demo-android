package superawesome.tv.kwsdemoapp.activities.leader;

import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetLeaderboardInterface;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class LeaderboardSource {

    public Observable<KWSLeader> getLeaderboard() {
        return Observable.create((Observable.OnSubscribe<KWSLeader>) subscriber -> {
            KWS.sdk.getLeaderBoard(leaders -> {
                for (KWSLeader leader : leaders) {
                    subscriber.onNext(leader);
                }
                subscriber.onCompleted();
            });
        });
    }
}

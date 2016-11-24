package superawesome.tv.kwsdemoapp.activities.leader;

import android.content.Context;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import rx.Observable;

public class LeaderboardSource {
    public Observable<KWSLeader> getLeaderboard(Context context) {
        return Observable.create((Observable.OnSubscribe<KWSLeader>) subscriber -> {
            KWS.sdk.getLeaderBoard(context, leaders -> {
                for (KWSLeader leader : leaders) {
                    subscriber.onNext(leader);
                }
                subscriber.onCompleted();
            });
        });
    }
}

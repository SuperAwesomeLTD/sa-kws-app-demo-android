package superawesome.tv.kwsappdemo.activities.leader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import superawesome.tv.kwsappdemo.aux.DataSource;
import superawesome.tv.kwsappdemo.aux.DataSourceInterface;
import superawesome.tv.kwsappdemo.aux.ViewModel;
import superawesome.tv.kwsappdemo.rxkws.RXKWS;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class LeaderboardAdapter extends ArrayAdapter<ViewModel> implements DataSource {

    // internal list
    private List<ViewModel> leaders = null;

    public LeaderboardAdapter (Context context, int resource) {
        super(context, resource);
    }

//    public rx.Observable<Boolean> startObserver () {
//
//        RXKWS.getLeaderboardObserver().
//                doOnSubscribe(() -> {
//                    leaders = new ArrayList<>();
//                    leaders.add(new LeaderHeaderViewModel());
//                }).
//                flatMap(leaders1 -> Observable.from(leaders1)).
//                flatMap((Func1<KWSLeader, Observable<ViewModel>>) kwsLeader -> {
//                    LeaderRowViewModel viewModel = new LeaderRowViewModel(kwsLeader.rank, kwsLeader.score, kwsLeader.user);
//                    return Observable.just(viewModel);
//                }).
//                subscribe(viewModel -> {
//                    leaders.add(viewModel);
//                }, throwable -> {
//                    //
//                }, () -> {
//                    //
//                });
//    }

    @Override public void update(DataSourceInterface start, DataSourceInterface success, DataSourceInterface error) {
        RXKWS.getLeaderboardObserver().
                doOnSubscribe(() -> {
                    if (start != null) start.event();
                    leaders = new ArrayList<>();
                    leaders.add(new LeaderHeaderViewModel());
                }).
                flatMap(leaders1 -> Observable.from(leaders1)).
                flatMap((Func1<KWSLeader, Observable<ViewModel>>) kwsLeader -> {
                    LeaderRowViewModel viewModel = new LeaderRowViewModel(kwsLeader.rank, kwsLeader.score, kwsLeader.user);
                    return Observable.just(viewModel);
                }).
                subscribe(viewModel -> {
                    leaders.add(viewModel);
                }, throwable -> {
                    if (error != null) error.event();
                }, () -> {
                    if (success != null) success.event();
                });
    }

    @Override public int getItemViewType(int position) { return position == 0 ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

    @Override public int getCount() { return leaders.size(); }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        return leaders.get(position).representationAsRow(getContext(), convertView);
    }
}

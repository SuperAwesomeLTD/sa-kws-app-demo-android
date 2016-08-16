package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import rx.functions.Action1;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.UniversalNotifier;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesEventsRowViewModel implements ViewModel {
    @Override public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_events, null);
        }

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();

        Button add20Points = (Button) v.findViewById(R.id.pointsAdd20);
        Button sub10Points = (Button) v.findViewById(R.id.pointsSub10);
        Button seeLeaders = (Button) v.findViewById(R.id.pointsLeader);
        Button seeDocs = (Button) v.findViewById(R.id.pointsDocs);

        add20Points.setEnabled(isLogged);
        sub10Points.setEnabled(isLogged);
        seeLeaders.setEnabled(isLogged);

        RxView.clicks(add20Points).subscribe(aVoid -> UniversalNotifier.getInstance().postNotification("ADD_20_POINTS"));
        RxView.clicks(sub10Points).subscribe(aVoid -> UniversalNotifier.getInstance().postNotification("SUB_10_POINTS"));
        RxView.clicks(seeLeaders).subscribe(aVoid -> UniversalNotifier.getInstance().postNotification("SEE_LEADERBOARD"));
        RxView.clicks(seeDocs).subscribe(aVoid -> UniversalNotifier.getInstance().postNotification("DOCS_NOTIFICATION"));

        return v;
    }
}

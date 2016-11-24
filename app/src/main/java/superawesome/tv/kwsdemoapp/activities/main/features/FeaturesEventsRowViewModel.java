package superawesome.tv.kwsdemoapp.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

public class FeaturesEventsRowViewModel implements GenericViewModelInterface {
    @Override public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_events, parent, false);
        }

        boolean isLogged = KWS.sdk.getLoggedUser() != null;

        Button add20Points = (Button) v.findViewById(R.id.pointsAdd20);
        Button sub10Points = (Button) v.findViewById(R.id.pointsSub10);
        Button getScore = (Button) v.findViewById(R.id.getScore);
        Button seeLeaders = (Button) v.findViewById(R.id.pointsLeader);
        Button seeDocs = (Button) v.findViewById(R.id.pointsDocs);

        add20Points.setEnabled(isLogged);
        sub10Points.setEnabled(isLogged);
        seeLeaders.setEnabled(isLogged);
        getScore.setEnabled(isLogged);

        RxView.clicks(add20Points).subscribe(aVoid -> UniversalNotifier.postNotification("ADD_20_POINTS"));
        RxView.clicks(sub10Points).subscribe(aVoid -> UniversalNotifier.postNotification("SUB_10_POINTS"));
        RxView.clicks(getScore).subscribe(aVoid -> UniversalNotifier.postNotification("GET_SCORE"));
        RxView.clicks(seeLeaders).subscribe(aVoid -> UniversalNotifier.postNotification("SEE_LEADERBOARD"));
        RxView.clicks(seeDocs).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));

        return v;
    }
}

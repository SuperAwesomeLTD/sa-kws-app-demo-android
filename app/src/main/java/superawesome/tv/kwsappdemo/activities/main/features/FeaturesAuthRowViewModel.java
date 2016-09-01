package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import rx.functions.Action1;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.UniversalNotifier;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesAuthRowViewModel implements ViewModel {

    @Override public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_auth, null);
        }

        Button authButton = (Button) v.findViewById(R.id.authAction);
        Button docsButton = (Button) v.findViewById(R.id.authDocs);

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();
        KWSModel local = KWSSingleton.getInstance().getUser();

        authButton.setText(isLogged ? "Logged in as " + local.username : "AUTHENTICATE USER");

        RxView.clicks(docsButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));
        RxView.clicks(authButton).subscribe(aVoid -> UniversalNotifier.postNotification("AUTH_NOTIFICATION"));

        return v;
    }
}

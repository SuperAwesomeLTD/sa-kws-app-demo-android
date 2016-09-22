package superawesome.tv.kwsdemoapp.activities.main.features;

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
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.KWSModel;
import superawesome.tv.kwsdemoapp.aux.KWSSingleton;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

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

        authButton.setText(isLogged ?
                context.getString(R.string.feature_cell_auth_button_1_loggedout) + local.username :
                context.getString(R.string.feature_cell_auth_button_1_loggedout));

        RxView.clicks(docsButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));
        RxView.clicks(authButton).subscribe(aVoid -> UniversalNotifier.postNotification("AUTH_NOTIFICATION"));

        return v;
    }
}

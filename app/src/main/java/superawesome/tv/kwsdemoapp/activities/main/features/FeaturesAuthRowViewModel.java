package superawesome.tv.kwsdemoapp.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.oauth.KWSLoggedUser;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

public class FeaturesAuthRowViewModel implements GenericViewModelInterface {

    @Override public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_auth, parent, false);
        }

        Button authButton = (Button) v.findViewById(R.id.authAction);
        Button docsButton = (Button) v.findViewById(R.id.authDocs);

        boolean isLogged = KWS.sdk.getLoggedUser() != null;
        KWSLoggedUser local = KWS.sdk.getLoggedUser();

        authButton.setText(isLogged ?
                context.getString(R.string.feature_cell_auth_button_1_loggedin) + " " + local.username :
                context.getString(R.string.feature_cell_auth_button_1_loggedout));

        RxView.clicks(docsButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));
        RxView.clicks(authButton).subscribe(aVoid -> UniversalNotifier.postNotification("AUTH_NOTIFICATION"));

        return v;
    }
}

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

public class FeaturesInviteRowViewModel implements GenericViewModelInterface {

    @Override
    public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_invite, parent, false);
        }

        boolean isLogged = KWS.sdk.getLoggedUser() != null;

        Button docButton = (Button) v.findViewById(R.id.inviteDocs);
        Button addButton = (Button) v.findViewById(R.id.inviteAddUser);

        addButton.setEnabled(isLogged);

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(addButton).subscribe(aVoid -> UniversalNotifier.postNotification("ADD_USER_NOTIFICATION"));

        return v;
    }
}

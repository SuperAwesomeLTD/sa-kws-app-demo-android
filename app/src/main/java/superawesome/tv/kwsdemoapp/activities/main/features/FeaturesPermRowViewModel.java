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

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesPermRowViewModel implements GenericViewModelInterface {
    @Override public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_perm, parent, false);
        }

        boolean isLogged = KWS.sdk.getLoggedUser() != null;

        Button addPermission = (Button) v.findViewById(R.id.permissionAddButton);
        Button permDocs = (Button) v.findViewById(R.id.permissionDocs);

        addPermission.setEnabled(isLogged);

        RxView.clicks(addPermission).subscribe(aVoid -> UniversalNotifier.postNotification("REQUEST_PERMISSION"));
        RxView.clicks(permDocs).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));

        return v;
    }
}

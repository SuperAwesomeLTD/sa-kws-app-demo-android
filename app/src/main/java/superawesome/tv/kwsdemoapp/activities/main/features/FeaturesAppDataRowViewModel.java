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
 * Created by gabriel.coman on 31/08/16.
 */
public class FeaturesAppDataRowViewModel implements GenericViewModelInterface {
    @Override
    public View representationAsRow(Context context, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_appdata, parent, false);
        }

        boolean isLogged = KWS.sdk.getLoggedUser() != null;

        Button docButton = (Button) v.findViewById(R.id.appdataDocs);
        Button seeButton = (Button) v.findViewById(R.id.appdataSee);

        seeButton.setEnabled(isLogged);

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(seeButton).subscribe(aVoid -> UniversalNotifier.postNotification("SEE_APP_DATA_NOTIFICATION"));

        return v;
    }
}

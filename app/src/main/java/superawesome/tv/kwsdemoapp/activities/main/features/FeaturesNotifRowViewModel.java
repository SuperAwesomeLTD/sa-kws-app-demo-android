package superawesome.tv.kwsdemoapp.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesNotifRowViewModel implements ViewModel {
    @Override public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_notif, null);
        }

        boolean isLogged = KWS.sdk.getLoggedUser() != null;
        boolean isRegistered = KWS.sdk.getLoggedUser() != null && KWS.sdk.getLoggedUser().isRegisteredForNotifications();

        Button docButton = (Button) v.findViewById(R.id.notifDocs);
        Button subButton = (Button) v.findViewById(R.id.notifEnableDisable);

        subButton.setEnabled(isLogged);
        subButton.setText(isRegistered ?
                context.getString(R.string.feature_cell_notif_button_1_disable) :
                context.getString(R.string.feature_cell_notif_button_1_enable));

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(subButton).subscribe(aVoid -> UniversalNotifier.postNotification("SUBSCRIBE_NOTIFICATION"));

        return v;
    }
}

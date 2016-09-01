package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.UniversalNotifier;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesNotifRowViewModel implements ViewModel {
    @Override public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_notif, null);
        }

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();
        boolean isRegistered = KWSSingleton.getInstance().isUserMarkedAsRegistered();

        Button docButton = (Button) v.findViewById(R.id.notifDocs);
        Button subButton = (Button) v.findViewById(R.id.notifEnableDisable);

        subButton.setEnabled(isLogged);
        subButton.setText(isRegistered ? "DISABLE PUSH NOTIFICATIONS" : "ENABLE PUSH NOTIFICATIONS");

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(subButton).subscribe(aVoid -> UniversalNotifier.postNotification("SUBSCRIBE_NOTIFICATION"));

        return v;
    }
}

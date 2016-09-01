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
 * Created by gabriel.coman on 31/08/16.
 */
public class FeaturesAppDataRowViewModel implements ViewModel {
    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_appdata, null);
        }

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();

        Button docButton = (Button) v.findViewById(R.id.appdataDocs);
        Button seeButton = (Button) v.findViewById(R.id.appdataSee);

        seeButton.setEnabled(isLogged);

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(seeButton).subscribe(aVoid -> UniversalNotifier.postNotification("SEE_APP_DATA_NOTIFICATION"));

        return v;
    }
}

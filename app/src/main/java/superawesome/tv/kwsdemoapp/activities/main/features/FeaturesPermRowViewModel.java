package superawesome.tv.kwsdemoapp.activities.main.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import rx.functions.Action1;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.KWSSingleton;
import superawesome.tv.kwsdemoapp.aux.UniversalNotifier;
import superawesome.tv.kwsdemoapp.aux.ViewModel;

/**
 * Created by gabriel.coman on 16/08/16.
 */
public class FeaturesPermRowViewModel implements ViewModel {
    @Override public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_perm, null);
        }

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();

        Button addPermission = (Button) v.findViewById(R.id.permissionAddButton);
        Button permDocs = (Button) v.findViewById(R.id.permissionDocs);

        addPermission.setEnabled(isLogged);

        RxView.clicks(addPermission).subscribe(aVoid -> UniversalNotifier.postNotification("REQUEST_PERMISSION"));
        RxView.clicks(permDocs).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION"));

        return v;
    }
}

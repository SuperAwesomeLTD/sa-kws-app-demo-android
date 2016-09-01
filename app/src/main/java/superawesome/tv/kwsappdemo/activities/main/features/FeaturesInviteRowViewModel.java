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
public class FeaturesInviteRowViewModel implements ViewModel {

    @Override
    public View representationAsRow(Context context, View convertView) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.listitem_features_invite, null);
        }

        boolean isLogged = KWSSingleton.getInstance().isUserLogged();

        Button docButton = (Button) v.findViewById(R.id.inviteDocs);
        Button addButton = (Button) v.findViewById(R.id.inviteAddUser);

        addButton.setEnabled(isLogged);

        RxView.clicks(docButton).subscribe(aVoid -> UniversalNotifier.postNotification("DOCS_NOTIFICATION") );
        RxView.clicks(addButton).subscribe(aVoid -> UniversalNotifier.postNotification("ADD_USER_NOTIFICATION"));

        return v;
    }
}

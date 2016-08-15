package superawesome.tv.kwsappdemo.activities.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsappdemo.aux.DataSource;
import superawesome.tv.kwsappdemo.aux.DataSourceInterface;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserAdapter extends ArrayAdapter<ViewModel> implements DataSource {

    private List<ViewModel> items = null;

    public UserAdapter (Context context, int resource) {
        super(context, resource);
    }

    @Override public void update(DataSourceInterface start, DataSourceInterface success, DataSourceInterface error) {
        if (start != null) start.event();

        KWS.sdk.getUser(kwsUser -> {
            if (kwsUser != null) {
                items = new ArrayList<>();

                // start with details
                items.add(new UserHeaderViewModel("Details"));
                items.add(new UserRowViewModel("ID", kwsUser.id));
                items.add(new UserRowViewModel("Username", kwsUser.username));
                items.add(new UserRowViewModel("First name", kwsUser.firstName));
                items.add(new UserRowViewModel("Last name", kwsUser.lastName));
                items.add(new UserRowViewModel("Birth date", kwsUser.dateOfBirth));
                items.add(new UserRowViewModel("Gender", kwsUser.gender));
                items.add(new UserRowViewModel("Phone", kwsUser.phoneNumber));
                items.add(new UserRowViewModel("Language", kwsUser.language));
                items.add(new UserRowViewModel("Email", kwsUser.email));

                // continue with address
                if (kwsUser.address != null) {
                    items.add(new UserHeaderViewModel("Address"));
                    items.add(new UserRowViewModel("Street", kwsUser.address.street));
                    items.add(new UserRowViewModel("City", kwsUser.address.city));
                    items.add(new UserRowViewModel("Post code", kwsUser.address.postCode));
                    items.add(new UserRowViewModel("Country", kwsUser.address.country));
                }

                // continue with points
                if (kwsUser.points != null) {
                    items.add(new UserHeaderViewModel("Points"));
                    items.add(new UserRowViewModel("Received", kwsUser.points.totalReceived));
                    items.add(new UserRowViewModel("Total", kwsUser.points.total));
                    items.add(new UserRowViewModel("In this app", kwsUser.points.totalPointsReceivedInCurrentApp));
                    items.add(new UserRowViewModel("Available", kwsUser.points.availableBalance));
                    items.add(new UserRowViewModel("Pending", kwsUser.points.pending));
                }

                // continue with permissions
                if (kwsUser.applicationPermissions != null) {
                    items.add(new UserHeaderViewModel("Permissions"));
                    items.add(new UserRowViewModel("Address", kwsUser.applicationPermissions.accessAddress));
                    items.add(new UserRowViewModel("Phone number", kwsUser.applicationPermissions.accessPhoneNumber));
                    items.add(new UserRowViewModel("First name", kwsUser.applicationPermissions.accessFirstName));
                    items.add(new UserRowViewModel("Last name", kwsUser.applicationPermissions.accessLastName));
                    items.add(new UserRowViewModel("Email", kwsUser.applicationPermissions.accessEmail));
                    items.add(new UserRowViewModel("Street address", kwsUser.applicationPermissions.accessStreetAddress));
                    items.add(new UserRowViewModel("City", kwsUser.applicationPermissions.accessCity));
                    items.add(new UserRowViewModel("Postal code", kwsUser.applicationPermissions.accessPostalCode));
                    items.add(new UserRowViewModel("Country", kwsUser.applicationPermissions.accessCountry));
                    items.add(new UserRowViewModel("Notifications", kwsUser.applicationPermissions.sendPushNotification));
                    items.add(new UserRowViewModel("Newsletter", kwsUser.applicationPermissions.sendNewsletter));
                }

                if (success != null) success.event();
            } else {
                if (error != null) error.event();
            }
        });
    }

    @Override public int getCount() { return items.size(); }

    @Override public int getItemViewType(int position) { return items.get(position) instanceof UserHeaderViewModel ? 0 : 1; }

    @Override public int getViewTypeCount() { return 2; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).representationAsRow(getContext(), convertView);
    }
}

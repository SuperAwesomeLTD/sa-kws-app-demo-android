package superawesome.tv.kwsappdemo.activities.user;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetUserInterface;
import rx.Observable;
import rx.Subscriber;
import superawesome.tv.kwsappdemo.aux.ViewModel;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class UserSource {

    public Observable<ViewModel> getUser () {
        return Observable.create((Observable.OnSubscribe<ViewModel>) subscriber -> {
            KWS.sdk.getUser(kwsUser -> {

                List<ViewModel> items1 = new ArrayList<>();

                items1.add(new UserHeaderViewModel("Details"));
                items1.add(new UserRowViewModel("ID", kwsUser.id));
                items1.add(new UserRowViewModel("Username", kwsUser.username));
                items1.add(new UserRowViewModel("First name", kwsUser.firstName));
                items1.add(new UserRowViewModel("Last name", kwsUser.lastName));
                items1.add(new UserRowViewModel("Birth date", kwsUser.dateOfBirth));
                items1.add(new UserRowViewModel("Gender", kwsUser.gender));
                items1.add(new UserRowViewModel("Phone", kwsUser.phoneNumber));
                items1.add(new UserRowViewModel("Language", kwsUser.language));
                items1.add(new UserRowViewModel("Email", kwsUser.email));

                // continue with address
                if (kwsUser.address != null) {
                    items1.add(new UserHeaderViewModel("Address"));
                    items1.add(new UserRowViewModel("Street", kwsUser.address.street));
                    items1.add(new UserRowViewModel("City", kwsUser.address.city));
                    items1.add(new UserRowViewModel("Post code", kwsUser.address.postCode));
                    items1.add(new UserRowViewModel("Country", kwsUser.address.country));
                }

                // continue with points
                if (kwsUser.points != null) {
                    items1.add(new UserHeaderViewModel("Points"));
                    items1.add(new UserRowViewModel("Received", kwsUser.points.totalReceived));
                    items1.add(new UserRowViewModel("Total", kwsUser.points.total));
                    items1.add(new UserRowViewModel("In this app", kwsUser.points.totalPointsReceivedInCurrentApp));
                    items1.add(new UserRowViewModel("Available", kwsUser.points.availableBalance));
                    items1.add(new UserRowViewModel("Pending", kwsUser.points.pending));
                }

                // continue with permissions
                if (kwsUser.applicationPermissions != null) {
                    items1.add(new UserHeaderViewModel("Permissions"));
                    items1.add(new UserRowViewModel("Address", kwsUser.applicationPermissions.accessAddress));
                    items1.add(new UserRowViewModel("Phone number", kwsUser.applicationPermissions.accessPhoneNumber));
                    items1.add(new UserRowViewModel("First name", kwsUser.applicationPermissions.accessFirstName));
                    items1.add(new UserRowViewModel("Last name", kwsUser.applicationPermissions.accessLastName));
                    items1.add(new UserRowViewModel("Email", kwsUser.applicationPermissions.accessEmail));
                    items1.add(new UserRowViewModel("Street address", kwsUser.applicationPermissions.accessStreetAddress));
                    items1.add(new UserRowViewModel("City", kwsUser.applicationPermissions.accessCity));
                    items1.add(new UserRowViewModel("Postal code", kwsUser.applicationPermissions.accessPostalCode));
                    items1.add(new UserRowViewModel("Country", kwsUser.applicationPermissions.accessCountry));
                    items1.add(new UserRowViewModel("Notifications", kwsUser.applicationPermissions.sendPushNotification));
                    items1.add(new UserRowViewModel("Newsletter", kwsUser.applicationPermissions.sendNewsletter));
                }

                // send messages
                for (ViewModel item : items1) {
                    subscriber.onNext(item);
                }
                subscriber.onCompleted();
            });
        });
    }

}

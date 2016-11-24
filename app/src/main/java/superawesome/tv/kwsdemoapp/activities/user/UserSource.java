package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModelInterface;

public class UserSource {

    public Observable<GenericViewModelInterface> getUser (Context context) {
        return Observable.create((Observable.OnSubscribe<GenericViewModelInterface>) subscriber -> {

            KWS.sdk.getUser(context, kwsUser -> {
                if (kwsUser == null) {
                    subscriber.onError(new Throwable());
                } else {

                    List<GenericViewModelInterface> items1 = new ArrayList<>();

                    items1.add(new UserHeaderViewModel(kwsUser.username));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_first_name), kwsUser.firstName));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_last_name), kwsUser.lastName));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_birth_date), kwsUser.dateOfBirth));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_email), kwsUser.email));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_phone), kwsUser.phoneNumber));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_gender), kwsUser.gender));
                    items1.add(new UserRowViewModel(context.getString(R.string.user_row_details_language), kwsUser.language));

                    // continue with address
                    if (kwsUser.address != null) {
                        items1.add(new UserHeaderViewModel(context.getString(R.string.user_header_address)));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_address_street), kwsUser.address.street));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_address_post_code), kwsUser.address.postCode));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_address_city), kwsUser.address.city));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_address_country), kwsUser.address.country));
                    }

                    // continue with points
                    if (kwsUser.points != null) {
                        items1.add(new UserHeaderViewModel(context.getString(R.string.user_header_points)));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_points_received), kwsUser.points.totalReceived));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_points_total), kwsUser.points.total));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_points_app), kwsUser.points.totalPointsReceivedInCurrentApp));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_points_available), kwsUser.points.availableBalance));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_points_pending), kwsUser.points.pending));
                    }

                    // continue with permissions
                    if (kwsUser.applicationPermissions != null) {
                        items1.add(new UserHeaderViewModel(context.getString(R.string.user_header_perm)));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_address), kwsUser.applicationPermissions.accessAddress));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_phone), kwsUser.applicationPermissions.accessPhoneNumber));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_first_name), kwsUser.applicationPermissions.accessFirstName));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_last_name), kwsUser.applicationPermissions.accessLastName));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_email), kwsUser.applicationPermissions.accessEmail));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_street), kwsUser.applicationPermissions.accessStreetAddress));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_city), kwsUser.applicationPermissions.accessCity));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_post_code), kwsUser.applicationPermissions.accessPostalCode));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_country), kwsUser.applicationPermissions.accessCountry));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_notifications), kwsUser.applicationPermissions.sendPushNotification));
                        items1.add(new UserRowViewModel(context.getString(R.string.user_row_perm_newsletter), kwsUser.applicationPermissions.sendNewsletter));
                    }

                    // send messages
                    for (GenericViewModelInterface item : items1) {
                        subscriber.onNext(item);
                    }
                    subscriber.onCompleted();
                }
            });
        });
    }

}

package superawesome.tv.kwsdemoapp.activities.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import superawesome.tv.kwsdemoapp.aux.RxKWS;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

public class UserActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Context context = this;
        SAProgressDialog dialog = SAProgressDialog.getInstance();

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        RxView.clicks(logoutButton).subscribe(aVoid -> logout());

        ListView userDetailsListView = (ListView) findViewById(R.id.userDetailsListView);

        RxKWS.getUser(context)
                .map(usr -> {

                    List<GenericViewModel> list = new ArrayList<>();

                    list.addAll(Arrays.asList(
                            UserHeaderViewModel.create(usr.username),
                            UserRowViewModel.create(context, R.string.user_row_details_first_name, usr.firstName),
                            UserRowViewModel.create(context, R.string.user_row_details_last_name, usr.lastName),
                            UserRowViewModel.create(context, R.string.user_row_details_birth_date, usr.dateOfBirth),
                            UserRowViewModel.create(context, R.string.user_row_details_email, usr.email),
                            UserRowViewModel.create(context, R.string.user_row_details_phone, usr.phoneNumber),
                            UserRowViewModel.create(context, R.string.user_row_details_gender, usr.gender),
                            UserRowViewModel.create(context, R.string.user_row_details_language, usr.language)
                    ));

                    if (usr.address != null) {
                        list.addAll(Arrays.asList(
                                UserHeaderViewModel.create(context.getString(R.string.user_header_address)),
                                UserRowViewModel.create(context, R.string.user_row_address_street, usr.address.street),
                                UserRowViewModel.create(context, R.string.user_row_address_post_code, usr.address.postCode),
                                UserRowViewModel.create(context, R.string.user_row_address_city, usr.address.city),
                                UserRowViewModel.create(context, R.string.user_row_address_country, usr.address.country)
                        ));
                    }

                    if (usr.points != null) {
                        list.addAll(Arrays.asList(
                                UserHeaderViewModel.create(context.getString(R.string.user_header_points)),
                                UserRowViewModel.create(context, R.string.user_row_points_received, usr.points.totalReceived),
                                UserRowViewModel.create(context, R.string.user_row_points_total, usr.points.total),
                                UserRowViewModel.create(context, R.string.user_row_points_app, usr.points.totalPointsReceivedInCurrentApp),
                                UserRowViewModel.create(context, R.string.user_row_points_available, usr.points.availableBalance),
                                UserRowViewModel.create(context, R.string.user_row_points_pending, usr.points.pending)
                        ));
                    }

                    if (usr.applicationPermissions != null) {
                        list.addAll(Arrays.asList(
                                UserHeaderViewModel.create(context.getString(R.string.user_header_perm)),
                                UserRowViewModel.create(context, R.string.user_row_perm_address, usr.applicationPermissions.accessAddress),
                                UserRowViewModel.create(context, R.string.user_row_perm_phone, usr.applicationPermissions.accessPhoneNumber),
                                UserRowViewModel.create(context, R.string.user_row_perm_first_name, usr.applicationPermissions.accessFirstName),
                                UserRowViewModel.create(context, R.string.user_row_perm_last_name, usr.applicationPermissions.accessLastName),
                                UserRowViewModel.create(context, R.string.user_row_perm_email, usr.applicationPermissions.accessEmail),
                                UserRowViewModel.create(context, R.string.user_row_perm_street, usr.applicationPermissions.accessStreetAddress),
                                UserRowViewModel.create(context, R.string.user_row_perm_city, usr.applicationPermissions.accessCity),
                                UserRowViewModel.create(context, R.string.user_row_perm_post_code, usr.applicationPermissions.accessPostalCode),
                                UserRowViewModel.create(context, R.string.user_row_perm_country, usr.applicationPermissions.accessCountry),
                                UserRowViewModel.create(context, R.string.user_row_perm_notifications, usr.applicationPermissions.sendPushNotification),
                                UserRowViewModel.create(context, R.string.user_row_perm_newsletter, usr.applicationPermissions.sendNewsletter)
                        ));
                    }

                    return list;
                })
                .doOnSubscribe(() -> dialog.showProgress(context))
                .doOnCompleted(dialog::hideProgress)
                .doOnError(throwable -> dialog.hideProgress())
                .subscribe(genericViewModels -> {

                    RxDataSource.from(context, genericViewModels)
                            .bindTo(userDetailsListView)
                            .customiseRow(R.layout.listitem_user_header, UserHeaderViewModel.class, (viewModel, view) -> {

                                UserHeaderViewModel header = (UserHeaderViewModel) viewModel;
                                TextView headerTextView = (TextView) view.findViewById(R.id.UserHeader);
                                headerTextView.setText(header.getHeaderTxt());

                            })
                            .customiseRow(R.layout.listitem_user_row, UserRowViewModel.class, (viewModel, view) -> {

                                UserRowViewModel row = (UserRowViewModel) viewModel;
                                row.processValue(context);

                                TextView itemTitleTextView = (TextView) view.findViewById(R.id.UserItemTitle);
                                itemTitleTextView.setText(row.getItemTxt());

                                TextView itemValueTextView = (TextView) view.findViewById(R.id.UserItemValue);
                                itemValueTextView.setText(row.getValueTxt());
                                itemValueTextView.setTextColor(row.getValueColor());

                            })
                            .update();

                }, throwable -> {
                    errorAlert();
                });
    }

    private void logout () {
        KWS.sdk.logoutUser(UserActivity.this);
        setResult(RESULT_OK);
        finish();
    }

    private void errorAlert () {
        SAAlert.getInstance().show(UserActivity.this,
                getString(R.string.user_popup_error_title),
                getString(R.string.user_popup_error_message),
                getString(R.string.user_popup_dismiss_button),
                null,
                false,
                0,
                null);
    }
}

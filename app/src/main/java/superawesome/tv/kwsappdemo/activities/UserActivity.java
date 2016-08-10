package superawesome.tv.kwsappdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.user.KWSUser;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetUserInterface;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.adapters.UserAdapter;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.UserDetailsModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserActivity extends AppCompatActivity {

    // toolbar
    android.support.v7.widget.Toolbar toolbar = null;
    private ListView userDetailsListView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        userDetailsListView = (ListView) findViewById(R.id.userDetailsListView);

        SAProgressDialog.getInstance().showProgress(this);
        KWS.sdk.getUser(new KWSGetUserInterface() {
            @Override
            public void gotUser(KWSUser kwsUser) {
                SAProgressDialog.getInstance().hideProgress();

                if (kwsUser != null) {
                    // populate array
                    List<UserDetailsModel> userDetailsModels = new ArrayList<>();

                    // start with details
                    userDetailsModels.add(UserDetailsModel.Header("Details"));
                    userDetailsModels.add(UserDetailsModel.Item("ID", kwsUser.id + ""));
                    userDetailsModels.add(UserDetailsModel.Item("Username", kwsUser.username + ""));
                    userDetailsModels.add(UserDetailsModel.Item("First name", kwsUser.firstName + ""));
                    userDetailsModels.add(UserDetailsModel.Item("Last name", kwsUser.lastName + ""));
                    userDetailsModels.add(UserDetailsModel.Item("Birth date", kwsUser.dateOfBirth + ""));
                    userDetailsModels.add(UserDetailsModel.Item("Gender", kwsUser.gender+ ""));
                    userDetailsModels.add(UserDetailsModel.Item("Phone", kwsUser.phoneNumber+ ""));
                    userDetailsModels.add(UserDetailsModel.Item("Language", kwsUser.language+ ""));
                    userDetailsModels.add(UserDetailsModel.Item("Email", kwsUser.email + ""));

                    // continue with address
                    if (kwsUser.address != null) {
                        userDetailsModels.add(UserDetailsModel.Header("Address"));
                        userDetailsModels.add(UserDetailsModel.Item("Street", kwsUser.address.street + ""));
                        userDetailsModels.add(UserDetailsModel.Item("City", kwsUser.address.city + ""));
                        userDetailsModels.add(UserDetailsModel.Item("Post code", kwsUser.address.postCode + ""));
                        userDetailsModels.add(UserDetailsModel.Item("Country", kwsUser.address.country + ""));
                    }

                    // continue with points
                    if (kwsUser.points != null) {
                        userDetailsModels.add(UserDetailsModel.Header("Points"));
                        userDetailsModels.add(UserDetailsModel.Item("Received", kwsUser.points.totalReceived+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Total", kwsUser.points.total + ""));
                        userDetailsModels.add(UserDetailsModel.Item("In this app", kwsUser.points.totalPointsReceivedInCurrentApp+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Available", kwsUser.points.availableBalance+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Pending", kwsUser.points.pending+ ""));
                    }

                    // continue with permissions
                    if (kwsUser.applicationPermissions != null) {
                        userDetailsModels.add(UserDetailsModel.Header("Permissions"));
                        userDetailsModels.add(UserDetailsModel.Item("Address", kwsUser.applicationPermissions.accessAddress+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Phone number", kwsUser.applicationPermissions.accessPhoneNumber+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("First name", kwsUser.applicationPermissions.accessFirstName+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Last name", kwsUser.applicationPermissions.accessLastName+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Email", kwsUser.applicationPermissions.accessEmail+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Street address", kwsUser.applicationPermissions.accessStreetAddress+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("City", kwsUser.applicationPermissions.accessCity + ""));
                        userDetailsModels.add(UserDetailsModel.Item("Postal code", kwsUser.applicationPermissions.accessPostalCode+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Country", kwsUser.applicationPermissions.accessCountry+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Push Notifications", kwsUser.applicationPermissions.sendPushNotification+ ""));
                        userDetailsModels.add(UserDetailsModel.Item("Send Newsletter", kwsUser.applicationPermissions.sendNewsletter+ ""));
                    }

                    // populate table
                    UserAdapter adapter = new UserAdapter(UserActivity.this, R.layout.listitem_leaderboard, userDetailsModels);
                    userDetailsListView.setAdapter(adapter);
                }
                else {
                    SAAlert.getInstance().show(UserActivity.this, "Hey!", "An error occurred retrieving user data, please try again.", "Got it!", null, false, 0, null);
                }
            }
        });
    }

    public void onLogout(View v) {
        // perform logout
        FirebaseInstanceId.getInstance().getToken();
        KWSSingleton.getInstance().setModel(null);

        Intent intent = new Intent();
        intent.setAction("superawesome.tv.RECEIVED_LOGOUT");
        intent.putExtra("STATUS", 1);
        sendBroadcast(intent);

        onBackPressed();
    }
}

package superawesome.tv.kwsappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import kws.superawesome.tv.kwslib.SANetwork;
import kws.superawesome.tv.kwslib.SANetworkInterface;
import kws.superawesome.tv.kwslib.SANetworkResponse;
import kws.superawesome.tv.kwslib.SAUtils;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class SignInActivity extends AppCompatActivity {

    // toolbar
    android.support.v7.widget.Toolbar toolbar = null;

    // text fields
    EditText usernameEdit = null;
    EditText password1Edit = null;
    EditText password2Edit = null;
    EditText yearEdit = null;
    EditText monthEdit = null;
    EditText dayEdit = null;
    Button submit = null;

    // private vars
    private String username = null;
    private String password1 = null;
    private String password2 = null;
    private String year = null;
    private String month = null;
    private String day = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.signinToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get edits
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        password1Edit = (EditText) findViewById(R.id.password1Edit);
        password2Edit = (EditText) findViewById(R.id.password2Edit);
        yearEdit = (EditText) findViewById(R.id.yearEdit);
        monthEdit = (EditText) findViewById(R.id.monthEdit);
        dayEdit = (EditText) findViewById(R.id.dayEdit);
    }

    public void onSubmitClick(View vi) {
        // get values
        if (usernameEdit.getText() != null) { username = usernameEdit.getText().toString(); }
        if (password1Edit.getText() != null) { password1 = password1Edit.getText().toString(); }
        if (password2Edit.getText() != null) { password2 = password2Edit.getText().toString(); }
        if (yearEdit.getText() != null) { year = yearEdit.getText().toString(); }
        if (monthEdit.getText() != null) { month = monthEdit.getText().toString(); }
        if (dayEdit.getText() != null) { day = dayEdit.getText().toString(); }

        if (username == null || username.isEmpty()) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid username.", "Got it!");
            return;
        }
        if (password1 == null || password1.isEmpty() || password1.length() < 8) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a password (that is more than 8 characters)", "Got it!");
            return;
        }
        if (password2 == null || password2.isEmpty() || password2.length() < 8) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Pleace confirm the password (and make sure it also has 8 characters)", "Got it!" );
            return;
        }
        if (!password1.equals(password2)) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "The two passwords you specified do not match.", "Got it!");
            return;
        }
        if (year == null || year.isEmpty()) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth year.", "Got it!");
            return;
        } else {
            if (Integer.parseInt(year) > 2016 || Integer.parseInt(year) < 1900) {
                KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth year.", "Got it!");
                return;
            }
        }
        if (month == null || month.isEmpty()) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth month.", "Got it!");
            return;
        } else {
            if (Integer.parseInt(month) > 12 || Integer.parseInt(year) < 1) {
                KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth month.", "Got it!");
                return;
            }
        }
        if (day == null || day.isEmpty()) {
            KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth day.", "Got it!");
            return;
        } else {
            if (Integer.parseInt(day) > 30 || Integer.parseInt(day) < 1) {
                KWSSimpleAlert.getInstance().show(this, "Hey!", "Please specify a valid birth day.", "Got it!");
                return;
            }
        }

        // finally make request
        makeRequest();
    }

    private void makeRequest () {
        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("password", password1);
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            int dayInt = Integer.parseInt(day);
            if (monthInt < 10) month = "0" + monthInt;
            if (dayInt < 10) day = "0" + dayInt;
            object.put("dateOfBirth", year + "-" + month + "-" + day);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SANetwork network = new SANetwork();
        network.sendPOST("https://kwsdemobackend.herokuapp.com/create", "", object, new SANetworkInterface() {
            @Override
            public void success(Object data) {
                SANetworkResponse response = (SANetworkResponse)data;
                int status = response.statusCode;
                String body = response.payload;
                try {
                    JSONObject json = new JSONObject(body);
                    KWSModel model = new KWSModel(json);
                    model.username = username;

                    // copy the model
                    KWSSingleton.getInstance().model = model;

                    // close activity
                    onBackPressed();

                } catch (JSONException e) {
                    KWSSimpleAlert.getInstance().show(SignInActivity.this, "Hey!", "Failed to sign up user", "Got it!");
                }
            }

            @Override
            public void failure() {
                KWSSimpleAlert.getInstance().show(SignInActivity.this, "Hey!", "Failed to sign up user", "Got it!");
            }
        });
    }
}

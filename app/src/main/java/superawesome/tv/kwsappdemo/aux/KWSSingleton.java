package superawesome.tv.kwsappdemo.aux;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import kws.superawesome.tv.kwssdk.KWS;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sautils.SAApplication;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class KWSSingleton {

    private final String KWS_API = "https://kwsapi.demo.superawesome.tv/v1/";

    private Context context;
    private KWSModel model = null;
    private boolean isRegistered = false;
    private boolean isLogged = false;
    SharedPreferences preferences = null;

    // singleton vars
    private static KWSSingleton instance = new KWSSingleton();

    public static KWSSingleton getInstance() {
        return instance;
    }

    // private constructor
    private KWSSingleton() {
        context = SAApplication.getSAApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void start() {
        if (preferences.contains("KWS_MODEL")) {
            String modelStr = preferences.getString("KWS_MODEL", null);
            JSONObject jsonObject = SAJsonParser.newObject(modelStr);
            this.model = new KWSModel(jsonObject);
            this.isLogged = true;
            KWS.sdk.setup(context, this.model.token, KWS_API);
        } else {
            logoutUser();
        }
    }

    /**
     * Login / create a new user
     * @param username - the username
     * @param password - the password
     * @param dateOfBirth - the users birthdate, as yyyy-mm-dd
     * @return - an observable
     */
    public rx.Observable<Boolean> loginUser(String username, String password, String dateOfBirth) {

        final JSONObject query = SAJsonParser.newObject(new Object[]{});

        final JSONObject body = SAJsonParser.newObject(new Object[]{
                "username", username,
                "password", password,
                "dateOfBirth", dateOfBirth,
                "country", "US"
        });

        final JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json"
        });

        return rx.Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
                SANetwork network = new SANetwork();
                network.sendPOST(context, "https://kwsdemobackend.herokuapp.com/create", query, header, body, (status, payload, success) -> {

                    if (!success) {
                        subscriber.onError(new Throwable());
                    } else {
                        JSONObject jsonObject = SAJsonParser.newObject(payload);
                        model = new KWSModel(jsonObject);

                        // check status
                        if (model.status == 1) {

                            // do some more additional setup

                            model.username = username;
                            isLogged = true;

                            // save to shared preferences
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("KWS_MODEL", model.writeToJson().toString());
                            editor.apply();

                            // setup the KWS SDK
                            KWS.sdk.setup(context, model.token, KWS_API);

                            // call subscriber functions
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Throwable());
                        }
                    }
                });
        });
    }

    /**
     * Logout user method that deletes all stuff related to a user
     */
    public void logoutUser() {
        this.model = null;
        this.isLogged = false;
        this.isRegistered = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("KWS_MODEL");
        editor.apply();
        // destup KWS
        KWS.sdk.desetup();
    }

    /**
     * Check if a user is registered on the KWS backend
     * @return
     */
    public rx.Observable<Boolean> getIsRegistered () {
        return  rx.Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            KWS.sdk.isRegistered(b -> {
                if (subscriber != null) {
                    subscriber.onNext(b);
                    subscriber.onCompleted();
                }
            });
        });
    }

//    public void loginUser(KWSModel model) {
//        this.model = model;
//        if (model != null) {
//            this.isLogged = true;
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("KWS_MODEL", this.model.writeToJson().toString());
//            editor.apply();
//            // setup KWS
//            KWS.sdk.setup(context, model.token, KWS_API);
//        }
//    }


    public boolean isUserLogged() {
        return isLogged;
    }

    public KWSModel getUser() {
        return model;
    }

    public void markUserRegistrationStatus(boolean b) { isRegistered = b; }

    public void markUserAsUnregistered() {
        isRegistered = false;
    }

    public boolean isUserMarkedAsRegistered() {
        return isRegistered;
    }
}
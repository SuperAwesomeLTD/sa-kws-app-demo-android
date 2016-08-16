package superawesome.tv.kwsappdemo.aux;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import kws.superawesome.tv.kwssdk.KWS;
import rx.subjects.PublishSubject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
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
            KWSModel local = new KWSModel(jsonObject);
            loginUser(local);
        } else {
            logoutUser();
        }

//        String modelStr = preferences.getString("KWS_MODEL", null);
//        if (modelStr != null) {
//            JSONObject jsonObject = SAJsonParser.newObject(modelStr);
//            this.model = new KWSModel(jsonObject);
//            KWS.sdk.setup(context, model.token, KWS_API);
//            this.isLogged = true;
//        } else {
//            this.isLogged = false;
//            this.isRegistered = false;
//            KWS.sdk.desetup();
//        }
    }

    public void loginUser(KWSModel model) {
        this.model = model;
        if (model != null) {
            this.isLogged = true;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("KWS_MODEL", this.model.writeToJson().toString());
            editor.apply();
            // setup KWS
            KWS.sdk.setup(context, model.token, KWS_API);
        }
    }

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

    public boolean isUserLogged() {
        return isLogged;
    }

    public KWSModel getUser() {
        return model;
    }

    public void markUserRegistrationStatus(boolean b) { isRegistered = b; }

    public void markUserAsRegister() {
        isRegistered = true;
    }

    public void markUserAsUnregistered() {
        isRegistered = false;
    }

    public boolean isUserMarkedAsRegistered() {
        return isRegistered;
    }
}
package superawesome.tv.kwsappdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import kws.superawesome.tv.kwslib.SAApplication;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class KWSSingleton {

    // private vars
    private Context context;
    private KWSModel model = null;
    private static KWSSingleton instance = new KWSSingleton();

    private KWSSingleton() {
        context = SAApplication.getSAApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String modelStr = preferences.getString("KWS_MODEL", null);
        if (modelStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(modelStr);
                this.model = new KWSModel(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static KWSSingleton getInstance(){
        return instance;
    }

    public void setModel(KWSModel model) {
        this.model = model;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KWS_MODEL", this.model.writeToJson().toString());
        editor.apply();
    }

    public KWSModel getModel() {
        return model;
    }
}

package superawesome.tv.kwsappdemo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class KWSModel implements JSONSerializable, Parcelable{

    public int status = 0;
    public int userId = 0;
    public String username = null;
    public String token = null;
    public String error = null;

    public KWSModel(JSONObject json) throws JSONException {
        readFromJson(json);
    }

    protected KWSModel(Parcel in) {
        status = in.readInt();
        userId = in.readInt();
        token = in.readString();
        error = in.readString();
        username = in.readString();
    }

    public static final Creator<KWSModel> CREATOR = new Creator<KWSModel>() {
        @Override
        public KWSModel createFromParcel(Parcel in) {
            return new KWSModel(in);
        }

        @Override
        public KWSModel[] newArray(int size) {
            return new KWSModel[size];
        }
    };

    @Override
    public void readFromJson(JSONObject jsonObject) {
        if (!jsonObject.isNull("status")) {
            status = jsonObject.optInt("status");
        }
        if (!jsonObject.isNull("userId")) {
            userId = jsonObject.optInt("userId");
        }
        if (!jsonObject.isNull("token")) {
            token = jsonObject.optString("token");
        }
        if (!jsonObject.isNull("error")) {
            error = jsonObject.optString("error");
        }
        if (!jsonObject.isNull("username")) {
            username = jsonObject.optString("username");
        }
    }

    @Override
    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("error", error);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeInt(userId);
        dest.writeString(token);
        dest.writeString(error);
        dest.writeString(username);
    }
}

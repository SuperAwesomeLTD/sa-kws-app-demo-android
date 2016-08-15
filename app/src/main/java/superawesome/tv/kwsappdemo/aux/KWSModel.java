package superawesome.tv.kwsappdemo.aux;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class KWSModel implements JSONSerializable, Parcelable{

    public int status = 0;
    public int userId = 0;
    public String username = null;
    public String token = null;
    public String error = null;

    public KWSModel(JSONObject json){
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

    @Override
    public void readFromJson(JSONObject json) {
        status = SAJsonParser.getInt(json, "status");
        userId = SAJsonParser.getInt(json, "userId");
        token = SAJsonParser.getString(json, "token");
        error = SAJsonParser.getString(json, "error");
        username = SAJsonParser.getString(json, "username");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[]{
                "status", status,
                "userId", userId,
                "token", token,
                "error", error,
                "username", username
        });
    }

    @Override
    public boolean isValid () {
        return true;
    }
}

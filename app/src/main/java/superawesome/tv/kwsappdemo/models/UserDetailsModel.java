package superawesome.tv.kwsappdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

/**
 * Created by gabriel.coman on 09/08/16.
 */
public class UserDetailsModel implements Parcelable, JSONSerializable {
    public String headerText = null;
    public String itemTitle = null;
    public String itemValue = null;
    public UserDetailModelType type = null;

    protected UserDetailsModel(Parcel in) {
        headerText = in.readString();
        itemTitle = in.readString();
        itemValue = in.readString();
    }

    public UserDetailsModel () {
        // do nothing
    }

    public static UserDetailsModel Header(String header) {
        UserDetailsModel model = new UserDetailsModel();
        model.headerText = header;
        model.type = UserDetailModelType.HEADER;
        return model;
    }

    public static UserDetailsModel Item(String title, String value) {
        UserDetailsModel model = new UserDetailsModel();
        model.itemTitle = title;
        model.itemValue = value;
        model.type = UserDetailModelType.ITEM;
        return model;
    }

    public UserDetailsModel (JSONObject json) {
        readFromJson(json);
    }

    public static final Creator<UserDetailsModel> CREATOR = new Creator<UserDetailsModel>() {
        @Override
        public UserDetailsModel createFromParcel(Parcel in) {
            return new UserDetailsModel(in);
        }

        @Override
        public UserDetailsModel[] newArray(int size) {
            return new UserDetailsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headerText);
        dest.writeString(itemTitle);
        dest.writeString(itemValue);
    }

    @Override
    public void readFromJson(JSONObject json) {
        headerText = SAJsonParser.getString(json, "headerText");
        itemTitle = SAJsonParser.getString(json, "itemTitle");
        itemValue = SAJsonParser.getString(json, "itemValue");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "headerText", headerText,
                "itemTitle", itemTitle,
                "itemValue", itemValue
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }
}

package superawesome.tv.kwsdemoapp.activities.country;

import android.content.Context;

import org.json.JSONObject;

import java.util.Locale;

import superawesome.tv.kwsdemoapp.R;
import superawesome.tv.kwsdemoapp.aux.GenericViewModel;
import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class CountryRowViewModel extends GenericViewModel implements JSONSerializable {

    private String defaultFlagString = null;
    private String countryISOCode = null;
    private String countryName = null;
    private String countryFlagString = null;

    private Context context = null;

    CountryRowViewModel (Context context, String isoCode) {

        // get the context
        this.context = context;

        // get the default flag string
        defaultFlagString = "flag_" + context.getString(R.string.page_country_row_default_country_flag);

        // get the ISO code
        countryISOCode = isoCode;

        // any other best case
        if (countryISOCode != null) {

            Locale locale = new Locale("", countryISOCode);
            countryName = locale.getDisplayName();
            countryFlagString = "flag_" + countryISOCode.toLowerCase();
        }
        // go to UN
        else {
            countryISOCode = context.getString(R.string.page_country_row_default_country_iso);
            countryName = context.getString(R.string.page_country_row_default_country_name);
            countryFlagString = defaultFlagString;
        }
    }

    public CountryRowViewModel(JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryFlagString() {
        return countryFlagString;
    }

    public String getDefaultCountryFlagString() {
        return defaultFlagString;
    }

    @Override
    public void readFromJson(JSONObject jsonObject) {
        countryISOCode = SAJsonParser.getString(jsonObject, "countryISOCode");
        countryName = SAJsonParser.getString(jsonObject, "countryName");
        countryFlagString = SAJsonParser.getString(jsonObject, "countryFlagString");
        defaultFlagString = SAJsonParser.getString(jsonObject, "defaultFlagString");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "countryISOCode", countryISOCode,
                "countryName", countryName,
                "countryFlagString", countryFlagString,
                "defaultFlagString", defaultFlagString
        });
    }

    @Override
    public boolean isValid() {
        return true;
    }
}

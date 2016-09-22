package superawesome.tv.kwsdemoapp.activities.signup;

import android.util.Log;

/**
 * Created by gabriel.coman on 31/08/16.
 */
public class SignUpModel {

    // constants
    private static final int INVALID_DATE = -Integer.MAX_VALUE;

    // normal private vars
    private String username;
    private String password1;
    private String password2;
    private Integer year;
    private Integer month;
    private Integer day;

    public SignUpModel (String username, String password1, String password2, String year, String month, String day) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.year = year != null && !year.isEmpty() ? Integer.parseInt(year) : INVALID_DATE;
        this.month = month != null && !month.isEmpty() ? Integer.parseInt(month) : INVALID_DATE;
        this.day = day != null && !day.isEmpty() ? Integer.parseInt(day) : INVALID_DATE;
    }

    public boolean isValid () {
        boolean userOK = username != null && !username.isEmpty();
        boolean pass1OK = password1 != null && !password1.isEmpty() && password1.length() >= 8;
        boolean pass2OK = password2 != null && !password2.isEmpty() && password2.length() >= 8;
        boolean passSame = password1 != null && password2 != null && password1.equals(password2);
        boolean yearOK = year > 1900;
        boolean monthOK = month >= 1 && month <= 12;
        boolean dayOK = day >= 1 && day <= 30;
        return userOK && pass1OK && pass2OK && passSame && yearOK && monthOK && dayOK;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password1;
    }

    public String getDate () {
        return (year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day));
    }
}

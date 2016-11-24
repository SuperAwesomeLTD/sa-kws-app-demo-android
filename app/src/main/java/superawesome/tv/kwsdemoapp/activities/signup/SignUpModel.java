package superawesome.tv.kwsdemoapp.activities.signup;

import tv.superawesome.lib.sautils.SAUtils;

public class SignUpModel {

    // constants
    private static final int INVALID_DATE = -Integer.MAX_VALUE;

    // normal private vars
    private String username;
    private String password1;
    private String password2;
    private String parentEmail;
    private Integer year;
    private Integer month;
    private Integer day;

    public SignUpModel (String username, String password1, String password2, String parentEmail, String year, String month, String day) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.parentEmail = parentEmail;
        this.year = year != null && !year.isEmpty() ? Integer.parseInt(year) : INVALID_DATE;
        this.month = month != null && !month.isEmpty() ? Integer.parseInt(month) : INVALID_DATE;
        this.day = day != null && !day.isEmpty() ? Integer.parseInt(day) : INVALID_DATE;
    }

    public boolean isUserOK () {
        return username != null && !username.isEmpty();
    }

    public boolean isPassword1OK () {
        return password1 != null && !password1.isEmpty() && password1.length() >= 8;
    }

    public boolean isPassword2OK () {
        return password2 != null && !password2.isEmpty() && password2.length() >= 8;
    }

    public boolean arePasswordsSame () {
        return password1 != null && password2 != null && password1.equals(password2);
    }

    public boolean isParentEmailOK () {
        return SAUtils.isValidEmail(parentEmail);
    }

    public boolean isYearOK () {
        return year > 1900;
    }

    public boolean isMonthOK () {
        return month >= 1 && month <= 12;
    }

    public boolean isDayOK () {
        return day >= 1 && day <= 31;
    }

    public boolean isValid () {
        return isUserOK() && isPassword1OK() && isPassword2OK() && arePasswordsSame() && isParentEmailOK() && isYearOK() && isMonthOK() && isDayOK();
    }

    public String getParentEmail () {
        return parentEmail;
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

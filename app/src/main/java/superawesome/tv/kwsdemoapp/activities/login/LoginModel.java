package superawesome.tv.kwsdemoapp.activities.login;

public class LoginModel {

    private String username;
    private String password;

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isValid () {
        boolean userOK = username != null && !username.isEmpty();
        boolean passOK = password != null && !password.isEmpty();
        return userOK && passOK;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

}

package superawesome.tv.kwsdemoapp.activities.login;

class LoginModel {

    private String username;
    private String password;

    LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    boolean isValid() {
        boolean userOK = username != null && !username.isEmpty();
        boolean passOK = password != null && !password.isEmpty();
        return userOK && passOK;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

}

package superawesome.tv.kwsdemoapp.activities.leader;

import superawesome.tv.kwsdemoapp.aux.GenericViewModel;

class LeaderRowViewModel extends GenericViewModel {

    private String rankTxt = null;
    private String scoreTxt = null;
    private String usernameTxt = null;

    LeaderRowViewModel(int rank, int score, String username) {
        rankTxt = Integer.toString(rank);
        scoreTxt = Integer.toString(score);
        usernameTxt = username;
    }

    String getRankTxt() {
        return rankTxt;
    }

    String getScoreTxt() {
        return scoreTxt;
    }

    String getUsernameTxt() {
        return usernameTxt;
    }
}

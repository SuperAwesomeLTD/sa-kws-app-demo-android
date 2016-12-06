package superawesome.tv.kwsdemoapp.activities.user;

import superawesome.tv.kwsdemoapp.aux.GenericViewModel;

class UserHeaderViewModel extends GenericViewModel {

    private String headerTxt = null;

    UserHeaderViewModel (String header) {
        this.headerTxt = header;
    }

    static UserHeaderViewModel create (String header) {
        return new UserHeaderViewModel(header);
    }

    String getHeaderTxt () {
        return headerTxt;
    }
}

package superawesome.tv.kwsdemoapp.activities.getappdata;

import superawesome.tv.kwsdemoapp.aux.GenericViewModel;

class GetAppDataRowViewModel extends GenericViewModel {

    private String name;
    private String value;

    GetAppDataRowViewModel(String name, int value) {
        this.name = name;
        this.value = "" + value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

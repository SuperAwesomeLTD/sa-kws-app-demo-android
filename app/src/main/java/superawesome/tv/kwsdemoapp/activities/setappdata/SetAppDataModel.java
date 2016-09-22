package superawesome.tv.kwsdemoapp.activities.setappdata;

/**
 * Created by gabriel.coman on 01/09/16.
 */
public class SetAppDataModel {

    private static final int INVALID_VALUE = -Integer.MAX_VALUE;

    private String name;
    private int value;

    public SetAppDataModel(String name, String value) {
        this.name = name != null && !name.isEmpty() && name.length() > 4 ? name : null;
        this.value = value != null && !value.isEmpty() ? Integer.parseInt(value) : INVALID_VALUE;
    }

    public boolean isValid () {
        return name != null && value != INVALID_VALUE;
    }

    public String getName () {
        return name;
    }

    public int getValue () {
        return value;
    }
}

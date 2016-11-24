package superawesome.tv.kwsdemoapp.aux;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public interface GenericViewModelInterface {
    View representationAsRow (Context context, View convertView, ViewGroup parent);
}

package superawesome.tv.kwsdemoapp.activities.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import rx.functions.Action2;

/**
 * Created by gabriel.coman on 06/12/2016.
 */

public class BaseFragment extends Fragment {

    private Action2<Integer, Integer> onActivityResult = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (onActivityResult != null) {
            onActivityResult.call(requestCode, resultCode);
        }
    }

    public void setOnActivityResult(Action2<Integer, Integer> onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

}

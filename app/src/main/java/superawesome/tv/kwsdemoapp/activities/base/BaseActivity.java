package superawesome.tv.kwsdemoapp.activities.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import rx.functions.Action3;

public class BaseActivity extends AppCompatActivity {

    private Action3<Integer, Integer, Intent> onActivityResult = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (onActivityResult != null) {
            onActivityResult.call(requestCode, resultCode, data);
        }
    }

    public void setOnActivityResult(Action3<Integer, Integer, Intent> onActivityResult) {
        this.onActivityResult = onActivityResult;
    }
}

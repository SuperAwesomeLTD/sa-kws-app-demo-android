package superawesome.tv.kwsappdemo.aux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public class SmartReceiver extends BroadcastReceiver {

    private Context context = null;
    private SmartReceiverInterface callback = null;

    public SmartReceiver(@NonNull Context context, SmartReceiverInterface callback) {
        this.context = context;
        this.callback = callback != null ? callback : (context1, intent) -> {};
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.execute(context, intent);
    }
}

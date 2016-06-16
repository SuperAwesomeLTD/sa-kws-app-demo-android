package superawesome.tv.kwsappdemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class KWSMessageReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null) {
                final String title = notification.getTitle();
                final String message = notification.getBody();

                // send data
                Intent intent = new Intent();
                intent.setAction("superawesome.tv.RECEIVED_BROADCAST");
                intent.putExtra("TITLE", title);
                intent.putExtra("MESSAGE", message);
                sendBroadcast(intent);
            }
        }
    }
}

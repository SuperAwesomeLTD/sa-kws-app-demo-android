package superawesome.tv.kwsappdemo.receivers;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            if (notification != null) {
                final String title = notification.getTitle();
                final String body = notification.getBody();

                Log.d("SuperAwesome", "Title is " + title);
                Log.d("SuperAwesome", "BODY is " + body);

                // send data
                Intent intent = new Intent();
                intent.setAction("superawesome.tv.RECEIVED_BROADCAST");
                intent.putExtra("TITLE", title);
                intent.putExtra("MESSAGE", body);
                sendBroadcast(intent);
            }

            if (remoteMessage.getData().size() > 0 && data != null) {
                Iterator it = data.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Log.d("SuperAwesome", pair.getKey() + " = " + pair.getValue());
                    it.remove();
                }
            }
        }
    }
}

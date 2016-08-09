package superawesome.tv.kwsappdemo.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.services.kws.KWSPermissionType;
import kws.superawesome.tv.kwssdk.services.kws.KWSRequestPermissionInterface;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAAlert;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeaturePermView extends LinearLayout {

    // constants
    private final String PERMURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // private vars
    private Button permissionAdd;
    private Button permissionDocs;
    private KWSModel localModel = null;

    public FeaturePermView(Context context) {
        this(context, null, 0);
    }

    public FeaturePermView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeaturePermView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_feature_perm, this, true);

        permissionAdd = (Button) findViewById(R.id.permissionAddButton);
        permissionDocs = (Button) findViewById(R.id.permissionDocs);

        localModel = KWSSingleton.getInstance().getModel();

        permissionAdd.setOnClickListener(v -> {
            Context c = getContext();
            KWSPermissionType types[] = new KWSPermissionType[] {
                    KWSPermissionType.accessEmail,
                    KWSPermissionType.accessAddress,
                    KWSPermissionType.accessFirstName,
                    KWSPermissionType.accessLastName,
                    KWSPermissionType.accessPhoneNumber,
                    KWSPermissionType.sendNewsletter
            };
            CharSequence titles[] = new CharSequence[] {
                    "Access email",
                    "Access address",
                    "Access first name",
                    "Access last name",
                    "Access phone number",
                    "Send newslatter"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Ask for a permission");
            builder.setItems(titles, (dialog, which) -> {
                KWS.sdk.requestPermission(new KWSPermissionType[]{ types[which] }, (success, requested) -> {
                    if (success && requested) {
                        SAAlert.getInstance().show(c, "Great!", "You've successfully asked for permission for " + titles[which], "Got it!", null, false, 0 , null);
                    } else {
                        SAAlert.getInstance().show(c, "Hey!", "Something happened while asking for permissions", "Got it!", null, false, 0, null);
                    }
                });
            });
            builder.show();
        });

        permissionDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PERMURL));
            getContext().startActivity(browserIntent);
        });

        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SignUpReceiver(), filter1);
        context.registerReceiver(new LogoutReceiver(), filter2);
    }

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeaturePermView.this.localModel = KWSSingleton.getInstance().getModel();
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeaturePermView.this.localModel = KWSSingleton.getInstance().getModel();
        }
    }
}

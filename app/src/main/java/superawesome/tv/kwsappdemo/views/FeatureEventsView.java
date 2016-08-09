package superawesome.tv.kwsappdemo.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.models.leaderboard.KWSLeader;
import kws.superawesome.tv.kwssdk.services.kws.KWSGetLeaderboardInterface;
import kws.superawesome.tv.kwssdk.services.kws.KWSTriggerEventInterface;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.LeaderboardActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.models.KWSModel;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAProgressDialog;

/**
 * Created by gabriel.coman on 08/08/16.
 */
public class FeatureEventsView extends LinearLayout {

    // constants
    private final String EVENTSURL = "https://developers.superawesome.tv/extdocs/sa-kws-android-sdk/html/index.html";

    // private vars
    private Button pointsAdd20;
    private Button pointsSub10;
    private Button pointsSeeLeader;
    private Button pointsDocs;
    private KWSModel localModel = null;

    public FeatureEventsView(Context context) {
        this(context, null, 0);
    }

    public FeatureEventsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeatureEventsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_feature_events, this, true);

        pointsAdd20 = (Button) findViewById(R.id.pointsAdd20);
        pointsSub10 = (Button) findViewById(R.id.pointsSub10);
        pointsSeeLeader = (Button) findViewById(R.id.pointsLeader);
        pointsDocs = (Button) findViewById(R.id.pointsDocs);

        localModel = KWSSingleton.getInstance().getModel();
        updateInterface();

        pointsAdd20.setOnClickListener(v -> {
            KWS.sdk.triggerEvent("GabrielAdd20ForAwesomeApp", 20, "You just earned 20 points!", b -> {
                if (b) {
                    SAAlert.getInstance().show(getContext(), "Congrats!", "You just earned 20 points!", "Great!", null, false, 0, null);
                }
            });
        });

        pointsSub10.setOnClickListener(v -> {
            KWS.sdk.triggerEvent("GabrielSub10ForAwesomeApp", -10, "You just lost 10 points!", b -> {
                if (b) {
                    SAAlert.getInstance().show(getContext(), "Hey!", "You just lost 10 points!", "Ok!", null, false, 0, null);
                }
            });
        });

        pointsSeeLeader.setOnClickListener(v -> {
            final Context c = getContext();
            SAProgressDialog.getInstance().showProgress(c);
            KWS.sdk.getLeaderBoard(list -> {
                SAProgressDialog.getInstance().hideProgress();
                if (list.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("leaders", (ArrayList) list);
                    Intent leadersActivity = new Intent(c, LeaderboardActivity.class);
                    leadersActivity.putExtras(bundle);
                    c.startActivity(leadersActivity);
                } else {
                    SAAlert.getInstance().show(c, "Hey!", "There are no leaders to display right now!", "Got i1!", null, false, 0, null);
                }
            });
        });

        pointsDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EVENTSURL));
            getContext().startActivity(browserIntent);
        });
        
        IntentFilter filter1 = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        IntentFilter filter2 = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SignUpReceiver(), filter1);
        context.registerReceiver(new LogoutReceiver(), filter2);
    }

    private void updateInterface () {
        boolean state = localModel != null;
        pointsAdd20.setEnabled(state);
        pointsSub10.setEnabled(state);
        pointsSeeLeader.setEnabled(state);
    }

    class SignUpReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureEventsView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }
    }

    class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FeatureEventsView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }
    }
}

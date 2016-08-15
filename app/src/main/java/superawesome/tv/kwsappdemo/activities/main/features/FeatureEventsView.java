package superawesome.tv.kwsappdemo.activities.main.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import kws.superawesome.tv.kwssdk.KWS;
import superawesome.tv.kwsappdemo.R;
import superawesome.tv.kwsappdemo.activities.leader.LeaderboardActivity;
import superawesome.tv.kwsappdemo.aux.KWSSingleton;
import superawesome.tv.kwsappdemo.aux.KWSModel;
import superawesome.tv.kwsappdemo.aux.SmartReceiver;
import superawesome.tv.kwsappdemo.aux.SmartReceiverInterface;
import tv.superawesome.lib.sautils.SAAlert;

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
    private IntentFilter signupFilter = null;
    private IntentFilter logoutFilter = null;

    // constructors
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
            Intent leaderboard = new Intent(c, LeaderboardActivity.class);
            c.startActivity(leaderboard);
        });

        pointsDocs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EVENTSURL));
            getContext().startActivity(browserIntent);
        });
        
        signupFilter = new IntentFilter("superawesome.tv.RECEIVED_SIGNUP");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeatureEventsView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), signupFilter);

        logoutFilter = new IntentFilter("superawesome.tv.RECEIVED_LOGOUT");
        context.registerReceiver(new SmartReceiver(getContext(), (context1, intent) -> {
            FeatureEventsView.this.localModel = KWSSingleton.getInstance().getModel();
            updateInterface();
        }), logoutFilter);
    }

    private void updateInterface () {
        boolean state = localModel != null;
        pointsAdd20.setEnabled(state);
        pointsSub10.setEnabled(state);
        pointsSeeLeader.setEnabled(state);
    }
}

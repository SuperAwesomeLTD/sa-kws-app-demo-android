package superawesome.tv.kwsappdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import kws.superawesome.tv.kwssdk.KWS;
import kws.superawesome.tv.kwssdk.KWSInterface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjc0OSwiYXBwSWQiOjMxMywiY2xpZW50SWQiOiJzYS1tb2JpbGUtYXBwLXNkay1jbGllbnQtMCIsInNjb3BlIjoidXNlciIsImlhdCI6MTQ2NTgwNjg0NSwiZXhwIjoxNDY1ODkzMjQ1LCJpc3MiOiJzdXBlcmF3ZXNvbWUifQ.IiPf7JuntZKUChKl1yy0FWLnNE4I7zChcZtL0dCZnKk";
        KWS.sdk.setApplicationContext(getApplicationContext());
        KWS.sdk.setup(token, "https://kwsapi.demo.superawesome.tv/v1/", new KWSInterface() {
            @Override
            public void isAllowedToRegisterForRemoteNotifications() {
                Log.d("SuperAwesome", "User is allowed to register for remote notifications");
                KWS.sdk.registerForRemoteNotifications();
            }

            @Override
            public void isAlreadyRegisteredForRemoteNotifications() {
                Log.d("SuperAwesome", "User is already registered for remote notifications");
            }

            @Override
            public void didRegisterForRemoteNotifications() {
                Log.d("SuperAwesome", "User successfully registered for remote notifications");
            }

            @Override
            public void didFailBecauseKWSDoesNotAllowRemoteNotifications() {
                Log.d("SuperAwesome", "User could not register because KWS does not allow it");
            }

            @Override
            public void didFailBecauseKWSCouldNotFindParentEmail() {
                Log.d("SuperAwesome", "User could not register becasue KWS could not find parent email");
            }

            @Override
            public void didFailBecauseRemoteNotificationsAreDisabled() {
                Log.d("SuperAwesome", "User could not register because remote notifications are disabled");
            }

            @Override
            public void didFailBecauseOfError() {
                Log.d("SuperAwesome", "User could not register because of misc error");
            }
        });
        KWS.sdk.checkIfNotificationsAreAllowed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

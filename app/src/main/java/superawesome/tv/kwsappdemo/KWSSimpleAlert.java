package superawesome.tv.kwsappdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by gabriel.coman on 15/06/16.
 */
public class KWSSimpleAlert {

    private static KWSSimpleAlert instance = new KWSSimpleAlert();
    private KWSSimpleAlert() {}
    public static KWSSimpleAlert getInstance(){
        return instance;
    }

    public void show(Context c, String title, String message, String button) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(c);
        alert.setTitle(title);
        alert.setCancelable(true);
        alert.setMessage(message);
        final AlertDialog.Builder ok = alert.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}

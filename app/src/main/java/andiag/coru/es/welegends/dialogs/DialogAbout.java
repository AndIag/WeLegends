package andiag.coru.es.welegends.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import andiag.coru.es.welegends.R;

/**
 * Created by Andy on 26/06/2015.
 */
public class DialogAbout extends DialogFragment {
    public static DialogAbout newInstance() {
        DialogAbout f = new DialogAbout();
        return f;
    }

    public static void onTwitterIagoClick(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=Iagocanalejas"));
            activity.startActivity(intent);

        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/Iagocanalejas")));
        }
    }

    public static void onTwitterAndyClick(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=ANDYear20"));
            activity.startActivity(intent);

        } catch (Exception e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/ANDYear20")));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_about, null));

        return builder.create();
    }
}

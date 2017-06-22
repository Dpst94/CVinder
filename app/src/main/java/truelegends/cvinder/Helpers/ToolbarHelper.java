package truelegends.cvinder.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import truelegends.cvinder.R;
import truelegends.cvinder.SplashActivity;

/**
 * Created by jessie on 15/06/2017.
 */

public class ToolbarHelper {

    Context context;
    private FirebaseAuth mAuth;

    /**
     * Gets the clicked toolbar_menu item and handles accordingly.
     */
    public String getClickedMenuItem(MenuItem item, Context context) {

        this.context = context;
        int id = item.getItemId();
        String toast = "";

        switch (id) {
            // when account button is clicked, log user out
            case R.id.action_logout:

                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                toast = "Logged out";

                Intent logOut = new Intent(context, SplashActivity.class);
                logOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logOut.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                context.startActivity(logOut);
                break;

//            case R.id.new_dive_toolbutton:
//
//                Intent new_dive_activity = new Intent(context, NewDiveActivity.class);
//                toast = context.getString(R.string.intent_newdive);
//                context.startActivity(new_dive_activity);
//                break;
//
//            case R.id.dive_log_toolbutton:
//
//                Intent dive_log_activity = new Intent(context, DiveLogActivity.class);
//                toast = context.getString(R.string.intent_divelog);
//                context.startActivity(dive_log_activity);
//                break;
//
//            case R.id.statistics_toolbutton:
//
//                Intent statistics_activity = new Intent(context, StatisticsActivity.class);
//                toast = context.getString(R.string.intent_statistics);
//                context.startActivity(statistics_activity);
//                break;
//
//            case R.id.nitrogen_toolbutton:
//
//                Intent nitrogen_activity = new Intent (context, NitrogenActivity.class);
//                toast = context.getString(R.string.intent_nitrogen);
//                context.startActivity(nitrogen_activity);
//                break;
        }
        return toast;
    }

}

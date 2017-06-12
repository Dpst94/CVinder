package truelegends.cvinder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private boolean exit;
    String title, visibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tx = (TextView)findViewById(R.id.app_title_text);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ArchivoNarrow-Regular.otf");
        tx.setTypeface(custom_font);

        exit = false;

    }

    /* When sign up button is clicked, move to LoginActivity with the corresponding params. */
    public void signUp(View view) {

        title = getString(R.string.signup_text);
        visibility = getString(R.string.visible);

        Intent signUpUser = new Intent(this, AuthActivity.class);

        signUpUser.putExtra("log_sign", title);
        signUpUser.putExtra("visibility", visibility);

        startActivity(signUpUser);
    }

    /* When log in button is clicked, move to LoginActivity with the corresponding params. */
    public void logIn(View view) {

        title = getString(R.string.login_text);
        visibility = getString(R.string.invisible);

        Intent logInUser = new Intent(this, AuthActivity.class);

        logInUser.putExtra("log_sign", title);
        logInUser.putExtra("visibility", visibility);

        startActivity(logInUser);
    }

    /* When back navigation is pressed twice, close the app. */
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, R.string.backnav_exit_warning,
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}

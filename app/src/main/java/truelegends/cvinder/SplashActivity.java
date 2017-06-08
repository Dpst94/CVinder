package truelegends.cvinder;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tx = (TextView)findViewById(R.id.app_title_text);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ArchivoNarrow-Regular.otf");

        tx.setTypeface(custom_font);

    }
}

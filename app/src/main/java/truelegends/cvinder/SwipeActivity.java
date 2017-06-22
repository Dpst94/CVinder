package truelegends.cvinder;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import truelegends.cvinder.Helpers.ToolbarHelper;

public class SwipeActivity extends AuthActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebase_user = mAuth.getCurrentUser();
    FirebaseDatabase my_database = FirebaseDatabase.getInstance();
    DatabaseReference database_ref = my_database.getReference();

    ArrayList<Profile> person_list;

    private Toolbar toolbar;
    ToolbarHelper toolbar_helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // construct toolbar_menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_helper = new ToolbarHelper();

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        person_list = new ArrayList<>();

        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.cvinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.cvinder_swipe_out_msg_view));


        loadFirebaseData();

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu toolbar) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.toolbar_menu, toolbar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem toolbar) {

        String clicked_item = toolbar_helper.getClickedMenuItem(toolbar, this);

        // display toast for clicked toolbar item
        if (!clicked_item.equals("")) {
            Toast.makeText(this, clicked_item, Toast.LENGTH_SHORT).show();
        } else if (clicked_item.equals("Logged out")) {
            finish();
        }

        return super.onOptionsItemSelected(toolbar);
    }

    /*
     * Data from Firebase needs to be loaded here to prevent asynchronous actions.
     */
    public void loadFirebaseData() {

        database_ref.child("superuser").child("profiles").addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // clear lists to avoid duplicates
                        person_list.clear();

                        // iterate over items in Firebase and add to cards
                        Iterable<DataSnapshot> cv_list = dataSnapshot.getChildren();
                        for (DataSnapshot profile : cv_list) {

                            Profile person = profile.getValue(Profile.class);
                            person_list.add(person);

                        }

                        for(Profile profile : person_list){
                            mSwipeView.addView(new CVinderCard(mContext, profile, mSwipeView));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}

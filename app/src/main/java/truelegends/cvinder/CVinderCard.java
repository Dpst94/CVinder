package truelegends.cvinder;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.support.v7.widget.CardView;
import android.widget.Space;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

/**
 * Created by janisharali on 19/08/16.
 */

@Layout(R.layout.cvinder_card_view)
@NonReusable
public class CVinderCard {

    @View(R.id.cvinderFrameLayout)
    private FrameLayout frameLayout;

    @View(R.id.cvinderCardView)
    private CardView cardView;

    @View(R.id.topSpace)
    private Space space;

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.profileLayout)
    private LinearLayout profileLayout;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    @View(R.id.moreTxt)
    private TextView moreTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private int iMaxHeight, iMinHeight, height, minHeight;
    private boolean isExpanded;

    public CVinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView, int maxHeight, int minHeight) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        iMaxHeight = maxHeight;
        iMinHeight = minHeight;
        isExpanded = false;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImage()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());
    }

    @Click(R.id.cvinderCardView)
    private void onClick(){
        Log.d("EVENT", "profileImageView click");
//        Intent intent = new Intent(mContext, DisplayDetailedCVActivity.class);
//        mContext.startActivity(intent);

        Log.d("cvinder", "" + cardView);
        height = cardView.getHeight();

        toggleCardViewnHeight(height);


        //mSwipeView.addView(this);
    }

    private void toggleCardViewnHeight(int height) {
        Log.d("HEIGHT", " " + height); //1184 = maxHeight, 810 = cardview height, 784
        Log.d("minHEIGHT", " " + iMinHeight); //1064
        Log.d("maxHeight", " " + iMaxHeight); //1184
        if (isExpanded == false) {
            // expand
            //collapseOrExpandView(iMaxHeight);
            expandView();
            Log.d("EVENT", "expandView");
            isExpanded = true;
        } else {
            // collapse
            //collapseOrExpandView(iMinHeight);
            collapseView();
            Log.d("EVENT", "collapseView");
            isExpanded = false;
        }
    }

    public void expandView() {
        space.setVisibility(android.view.View.GONE);
        profileImageView.setVisibility(android.view.View.GONE);
        moreTxt.setVisibility(android.view.View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)layoutParams;
        lp.setMargins(0,0,0,0);
        cardView.setLayoutParams(lp);

        layoutParams = nameAgeTxt.getLayoutParams();
        lp = (LinearLayout.LayoutParams)layoutParams;
        int margin = Utils.dpToPx(10);
        lp.setMargins(0,margin,0,margin);
        nameAgeTxt.setTextSize(26);
        nameAgeTxt.setLayoutParams(lp);

        ViewGroup.LayoutParams layoutParams1 = profileLayout.getLayoutParams();
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams)layoutParams1;
        lp1.gravity = Gravity.TOP;
        profileLayout.setLayoutParams(lp1);

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.TOP;
//        profileLayout.setLayoutParams(params);

//        ViewGroup.LayoutParams layoutParams1 = profileLayout.getLayoutParams();
//        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) layoutParams1;
//        Log.d("layoutparam", "" + lp1.gravity);
//        lp1.gravity = Gravity.TOP;
//        profileLayout.setLayoutParams(lp1);

    }

    public void collapseView() {
        space.setVisibility(android.view.View.VISIBLE);
        profileImageView.setVisibility(android.view.View.VISIBLE);
        moreTxt.setVisibility(android.view.View.GONE);
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)layoutParams;
        int margin = Utils.dpToPx(10);
        lp.setMargins(margin,margin,margin,margin);

        cardView.setLayoutParams(lp);

        layoutParams = nameAgeTxt.getLayoutParams();
        lp = (LinearLayout.LayoutParams)layoutParams;
        lp.setMargins(0,0,0,0);
        nameAgeTxt.setTextSize(18);
        nameAgeTxt.setLayoutParams(lp);

        ViewGroup.LayoutParams layoutParams1 = profileLayout.getLayoutParams();
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams)layoutParams1;
        lp1.gravity = Gravity.BOTTOM;
        profileLayout.setLayoutParams(lp1);

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.BOTTOM; // switch matchparent with wrap content and you get half picture.
//        profileLayout.setLayoutParams(params);

//        ViewGroup.LayoutParams params = profileLayout.getLayoutParams();
//        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams)params;
//        lp1.gravity = Gravity.BOTTOM;
//        profileLayout.requestLayout();

//        WindowManager.LayoutParams params = new LayoutParams();
//        params.gravity = Gravity.TOP;
//        profileLayout.setLayoutParams(params);

//        layoutParams = profileLayout.getLayoutParams();
//        layoutParams.height = 100;
//        profileLayout.setLayoutParams(layoutParams);
    }

    public void collapseOrExpandView(int height) {
        ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(), height);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                Log.d("collapseexpand", "val " + val);
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                Log.d("collapseexpand", "height " + layoutParams.height);
                layoutParams.height = val;
                frameLayout.setLayoutParams(layoutParams);
            }
        });
        anim.start();
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}

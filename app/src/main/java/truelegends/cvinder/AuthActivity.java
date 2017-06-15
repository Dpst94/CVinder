package truelegends.cvinder;

/**
 * Created by jessie on 09/06/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

//package com.google.firebase.quickstart.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import truelegends.cvinder.Helpers.ToolbarHelper;

public class AuthActivity extends SplashActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";

    String title, visibility;
    TextView title_text;
    EditText username_field, email_field, pass_field, passconf_field;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference my_database;

    private Toolbar toolbar;
    ToolbarHelper toolbar_helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // construct toolbar_menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_helper = new ToolbarHelper();

        // initialize views
        username_field = (EditText) findViewById(R.id.name_input);
        email_field = (EditText) findViewById(R.id.email_input);
        pass_field = (EditText) findViewById(R.id.pass_input);
        passconf_field = (EditText) findViewById(R.id.passconf_input);

        title_text = (TextView) findViewById(R.id.auth_title_text);

        Button login_button = (Button) findViewById(R.id.login_button);

        // set listeners for buttons
        findViewById(R.id.login_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.go_menu_button).setOnClickListener(this);

        // get extras from SplashActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("log_sign");
            visibility = extras.getString("visibility");
        }

        // edit initial UI according to logging in or signing up
        title_text.setText(title);

        // display EditText for password confirmation in case of signing up
        if (visibility != null) {
            if (visibility.equals(getString(R.string.visible))) {
                username_field.setVisibility(View.VISIBLE);
                passconf_field.setVisibility(View.VISIBLE);
                login_button.setText(R.string.signup_text);
            }
            else {
                login_button.setText(R.string.login_text);
            }
        }

        // set Firebase listener for auth state
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // UserItem is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    instr_text.setText(R.string.login_stilllogedin_instr);

                } else {
                    // UserItem is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                //updateUI(user);
            }
        };

        // initialize Firebase reference
        my_database = FirebaseDatabase.getInstance().getReference();
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /* Create new user account. */
    private void createAccount(String email, String password) {

        //final String toast = getString(R.string.login_toast);
        Log.d("test", "does it even get into the create account?");

        // validate user input and create user with e-mail and password
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Log.d("test", "not validated");
            return;
        }
        else {
            Log.d("test", "validated");
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Log.d("test", "create user with email, task is succesful geeft: " + task.isSuccessful());

                        // if sign in fails, display a message to the user
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // if sign in succeeds, auth state listener will be notified
                        else {
                            //instr_text.setText(new_instr);
                            Log.d("test", "to save user info");
                            saveUserInformation();
                        }
                    }
                });
    }

    /* Log in user with existing account. */
    private void signIn(String email, String password) {

        //final String new_instr = getString(R.string.login_signedup_newinstr);

        Log.d("test", "does it even get into the sign in?");

        // validate user input and sign in user with e-mail and password
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            Log.d("test", "not validated");

            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // if sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(AuthActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // if sign in succeeds, auth state listener will be notified
                        else {
                            //instr_text.setText(new_instr);
                            Log.d("test", "sign in succeededddddd");
                        }

                    }
                });
    }
//
//    /* Sign user out. */
//    private void signOut() {
//        mAuth.signOut();
//        updateUI(null);
//    }

    /* Validate user's data. */
    private boolean validateForm() {

        boolean valid = true;

        // validate e-mail
        String email = email_field.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_field.setError(getString(R.string.required));
            valid = false;
        } else {
            email_field.setError(null);

            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(this, R.string.mail_requirement, Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        // validate password
        int min_char = 6;
        String password = pass_field.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass_field.setError(getString(R.string.required));
            valid = false;
        } else {
            pass_field.setError(null);

            if (password.length() < min_char) {
                Toast.makeText(this, R.string.pass_requirement, Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        // if user is signing up, validate username and password confirmation
        if (passconf_field.getVisibility() == View.VISIBLE ) {

            // validate username
            int max_char = 15;
            String username = username_field.getText().toString();
            if (TextUtils.isEmpty(username)) {
                username_field.setError(getString(R.string.required));
                valid = false;
            } else {
                username_field.setError(null);

                if (username.length() > max_char) {
                    Toast.makeText(this, R.string.name_requirement,
                            Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }

            // validate password confirmation
            String password_confirm = passconf_field.getText().toString();
            if (TextUtils.isEmpty(password_confirm)) {
                passconf_field.setError(getString(R.string.required));
                valid = false;
            } else if (!password.equals(password_confirm)) {
                Toast.makeText(this, R.string.no_pass_match, Toast.LENGTH_SHORT).show();
                valid = false;
            } else {
                pass_field.setError(null);
            }
        }

        return valid;
    }

//    /* Update interface after logging in or signing up. */
//    private void updateUI(FirebaseUser user) {
//
//        if (user != null) {
//            Button login_button = (Button) findViewById(R.id.login_button);
//
//            if (login_button != null) {
//                findViewById(R.id.sign_up_button).setVisibility(View.GONE);
//                findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
//                findViewById(R.id.go_menu_button).setVisibility(View.VISIBLE);
//                username_field.setVisibility(View.GONE);
//                email_field.setVisibility(View.GONE);
//                password_field.setVisibility(View.GONE);
//
//                if (pass_confirm_field.getVisibility() != View.INVISIBLE) {
//                    pass_confirm_field.setVisibility(View.GONE);
//                }
//            } else {
//
//                username_field.setVisibility(View.VISIBLE);
//                email_field.setVisibility(View.VISIBLE);
//                password_field.setVisibility(View.VISIBLE);
//
//                if (confirm_pass != null) {
//                    if (confirm_pass.equals(getString(R.string.home_passconfirm_invisible))) {
//                        pass_confirm_field.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        }
//    }

    /* Save user information to Firebase upon signing up. */
    private void saveUserInformation() {

        Log.d("test", "inside save user info");


        String username = username_field.getText().toString().trim();
        String email = email_field.getText().toString().trim();

        FirebaseUser firebase_user = mAuth.getCurrentUser();
        String user_id = firebase_user.getUid();

        UserItem user = new UserItem(username, email);

        my_database.child("users").child(user_id).child("user_info").setValue(user);

    }

    /* Listen for button click to determine what to do. */
    @Override
    public void onClick (View v){

        int i = v.getId();

        // upon pressing sign up button, create new account or log in
        if (i == R.id.login_button) {

            String email = email_field.getText().toString();
            String password = pass_field.getText().toString();

            if (title.equals(getString(R.string.signup_text))) {

                Log.d("test", "Logging in");

                if (validateForm()) {
                    createAccount(email, password);
                    signIn(email, password);

                    Toast.makeText(this, "Logged in succesfully.", Toast.LENGTH_LONG).show();

//                    Intent goToMenu = new Intent(this, MenuActivity.class);
//                    startActivity(goToMenu);
//
//                    finish();

                }
                else {
                    Log.d("test", "onclick validate form not okay");
                }

            } else if (title.equals(getString(R.string.login_text))) {

                if (validateForm()) {
                    signIn(email, password);

                    Toast.makeText(this, "Signed up succesfully.", Toast.LENGTH_LONG).show();

//                    Intent goToMenu = new Intent(this, MenuActivity.class);
//                    startActivity(goToMenu);
//
//                    finish();
                }
            }

        }
        // upon pressing sign out button, sign user out and go back to HomeActivity
//        else if (i == R.id.sign_out_button) {
//            signOut();
//            finish();
//        }
        // upon pressing continue button, move on to MenuActivity
//        else if (i == R.id.go_menu_button) {
//
//            Intent goToMenu = new Intent(this, MenuActivity.class);
//            startActivity(goToMenu);
//
//            finish();
//        }
    }
}

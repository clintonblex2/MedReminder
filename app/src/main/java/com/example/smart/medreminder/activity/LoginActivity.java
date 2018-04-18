package com.example.smart.medreminder.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smart.medreminder.R;
import com.example.smart.medreminder.helper.DatabaseHelper;
import com.example.smart.medreminder.model.User;
import com.example.smart.medreminder.util.Util;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.onurkaganaldemir.ktoastlib.KToast;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = LoginActivity.this;
    private final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText textInputEditTextEmail, textInputEditTextPassword;
    private Button resetBtn, loginBtn, signupBtn;
    private SignInButton googleSignInButton;
    GoogleSignInOptions gso;
    private Util util;
    DatabaseHelper databaseHelper;
    User user;
//    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new SpotsDialog(activity, R.style.Custom).dismiss();

        initViews();
        initListeners();
        initObjects();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

    }

    private void initViews() {
        textInputEditTextEmail = findViewById(R.id.login_email);
        textInputEditTextPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);
        googleSignInButton = findViewById(R.id.sign_in_button);
    }

    private void initListeners() {

        googleSignInButton.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        setGoogleSignButton(googleSignInButton, "Sign in with Google");
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        util = new Util();

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sign_in_button:
                signInWithGoogle();
                break;

            case R.id.login_btn:
                verifyFromSQLite();
                break;

            case R.id.signup_btn:
                startActivity(new Intent(activity, RegisterActivity.class));
                break;
        }
    }

    private void verifyFromSQLite() {

        String email = textInputEditTextEmail.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
//            Toast.makeText(activity, "Enter your email address", Toast.LENGTH_SHORT).show();
            KToast.customColorToast(activity, getString(R.string.error_message_email_empty),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.blue_grey_900, R.drawable.ic_email);

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            KToast.customColorToast(activity, getString(R.string.error_message_email),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.blue_grey_900, R.drawable.ic_email);

            return;
        }
        if (password.isEmpty()) {
            KToast.customColorToast(activity, "Enter your password",
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.blue_grey_900, R.drawable.ic_key);
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim(),
                textInputEditTextPassword.getText().toString().trim())) {

//            progressBar.setVisibility(View.VISIBLE);
            new SpotsDialog(activity, R.style.Custom).show();

//            googleProgressBar.setVisibility(View.VISIBLE);

            Intent accountIntent = new Intent(activity, MainActivity.class);
            accountIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountIntent);
            finish();

        } else {
            KToast.customColorToast(activity, "Wrong Email or Password",
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.fuchsia, R.drawable.ic_menu_profile);

            new SpotsDialog(activity, R.style.Custom).dismiss();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);

//        googleProgressBar.setVisibility(View.GONE);
        new SpotsDialog(activity, R.style.Custom).dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new SpotsDialog(activity, R.style.Custom).dismiss();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount currentUser = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(currentUser);

        if (currentUser != null) {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            Signed in successfully, show authenticated UI.
//            googleProgressBar.setVisibility(View.GONE);

            updateUI(account);
            startActivity(new Intent(activity, MainActivity.class));
            finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);

        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {

        if (account != null) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);

            new SpotsDialog(activity, R.style.Custom).show();

        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (userInfo.isLoggedIn()) {
//            Intent intent = new Intent(activity, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    private void signInWithGoogle() {

        new SpotsDialog(activity, R.style.Custom).show();

//        progressBar.setVisibility(View.VISIBLE);
//        googleProgressBar.setVisibility(View.VISIBLE);

        //        Starting the intent prompts the user to select a Google account to sign in with
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    protected void setGoogleSignButton(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View view = signInButton.getChildAt(i);

            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setText(buttonText);
                return;
            }
        }
    }
}

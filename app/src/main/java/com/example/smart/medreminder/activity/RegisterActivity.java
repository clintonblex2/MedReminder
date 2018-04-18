package com.example.smart.medreminder.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.onurkaganaldemir.ktoastlib.KToast;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = RegisterActivity.this;
    private final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private EditText textInputEditTextName, textInputEditTextEmail,
            editTextPassword, editTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private SignInButton googleSignUpButton;
    GoogleSignInOptions gso;
    private DatabaseHelper databaseHelper;
    private User user;
    private Util util;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        new SpotsDialog(activity, R.style.Custom).dismiss();

        initViews();
        initListeners();
        initObjects();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initViews() {

        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        editTextPassword = findViewById(R.id.textInputEditTextPassword);
        editTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);
    }

    private void initListeners() {

        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        googleSignUpButton.setOnClickListener(this);

        googleSignUpButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignUpButton.setColorScheme(SignInButton.COLOR_LIGHT);

        setGoogleSignupButtonText(googleSignUpButton, "Sign up with Google");

    }

    private void initObjects() {

        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
                break;

            case R.id.googleSignUpButton:
                signUpWithGoogle();
                break;
        }
    }

    private void postDataToSQLite() {

        String fullname = textInputEditTextName.getText().toString().trim();
        String email = textInputEditTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            KToast.customColorToast(activity, getString(R.string.error_message_name),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.blue_grey_900, R.drawable.ic_menu_profile);

            return;
        }
        if (email.isEmpty()) {
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
        if (!password.equals(confirmPassword)) {
            KToast.customColorToast(activity, getString(R.string.error_password_match),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.blue_grey_900, R.drawable.ic_key);

            return;
        }
        // If user does not exist in the database, register the user

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setFullname(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(editTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            KToast.successToast(activity, getString(R.string.reg_success_message),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO);

            startActivity(new Intent(activity, LoginActivity.class));
            finish();

            emptyInputEditText();

        } else {
            KToast.customColorToast(activity, getString(R.string.error_email_exists),
                    Gravity.BOTTOM, KToast.LENGTH_AUTO, R.color.fuchsia, R.drawable.ic_menu_profile);

        }
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
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

            // Signed in successfully, show authenticated UI.
//            googleProgressBar.setVisibility(View.GONE);
            String name = account.getDisplayName();
            String email = account.getEmail();

            user.setFullname(name);
            user.setEmail(email);

            /**
             * Save to SQLite database
             */
            databaseHelper.addUser(user);


            startActivity(new Intent(activity, MainActivity.class));
            finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }


    private void signUpWithGoogle() {

        new SpotsDialog(activity, R.style.Custom).show();

        //        Starting the intent prompts the user to select a Google account to sign in with
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    protected void setGoogleSignupButtonText(SignInButton signInButton, String buttonText) {
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

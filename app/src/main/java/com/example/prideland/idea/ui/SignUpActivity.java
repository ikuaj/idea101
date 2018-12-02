package com.example.prideland.idea.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prideland.idea.MainActivity;
import com.example.prideland.idea.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.SinguptextView) TextView mSinguptextView;
    @BindView(R.id.FirstName) EditText mFirstName;
    @BindView(R.id.LastName) EditText mLastName;
    @BindView(R.id.emailtext) EditText mEmailText;
    @BindView(R.id.Phone) EditText mPhone;
    @BindView(R.id.signUp) Button mSignUpButton;
    @BindView(R.id.Password) EditText mPasswordSignUp;
    @BindView(R.id.ConfirmPassword) EditText mConfirmPasswordEditText;
    @BindView(R.id.loginTextView) TextView mLoginTextView;

    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mLoginTextView.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        createAuthStateListener();
        createAuthProgressDialog();

        //        Typeface righteousFonts = Typeface.createFromAsset(getAssets(), "fonts/Righteous/Righteous-Regular.ttf");
//        mSinguptextView.setTypeface(righteousFonts);
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginTextView) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        if (view == mSignUpButton) {
            createUser();
        }
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

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mFirstName.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordSignUp.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordSignUp.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void createUser() {
        final String FirstName = mFirstName.getText().toString().trim();
        final String emailtext = mEmailText.getText().toString().trim();
        String Password = mPasswordSignUp.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(emailtext);
        boolean validName = isValidName(FirstName);
        boolean validPassword = isValidPassword(Password, confirmPassword);
        if (!validEmail || !validName || !validPassword) return;

        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailtext, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful");
                            createFirebaseUserProfile(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, user.getDisplayName());
//                        }
                    }

                });
    }

    private void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

        };
    }
}

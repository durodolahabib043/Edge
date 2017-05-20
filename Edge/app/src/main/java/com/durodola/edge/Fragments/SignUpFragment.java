package com.durodola.edge.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.durodola.edge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private EditText emailT, passwordT;
    private Button signUpbtn, forgotbtn;
    TextView statustxt, detailstxt;
    private FirebaseAuth mAuth;
    String emailString, passwordString;
    private String TAG = "SignUpFragment";

    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(view);
        signOut();

        return view;
    }

    private void init(View view) {
        emailT = (EditText) view.findViewById(R.id.email);
        passwordT = (EditText) view.findViewById(R.id.password);
        signUpbtn = (Button) view.findViewById(R.id.signupbtn);
        forgotbtn = (Button) view.findViewById(R.id.forget);
        statustxt = (TextView) view.findViewById(R.id.status);
        detailstxt = (TextView) view.findViewById(R.id.details);
        emailString = emailT.getText().toString();
        passwordString = passwordT.getText().toString();
        signUpbtn.setOnClickListener(this);
        forgotbtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount() {
        Log.d(TAG, "createAccount:" + emailT.getText().toString());
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(emailT.getText().toString(), passwordT.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void signIn() {
        Log.d(TAG, "signIn:" + emailT.getText().toString());
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(emailT.getText().toString(), passwordT.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            statustxt.setText("falied");
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        // signUpbtn.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //   signUpbtn.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(emailString)) {
            emailT.setError("Required.");
            Toast.makeText(getContext(),
                    "Required.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            //mEmailField.setError(null);
        }


        if (TextUtils.isEmpty(passwordString)) {
            passwordT.setError("Required.");
            valid = false;
        } else {
            passwordT.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            statustxt.setText(" " +
                    user.getEmail() + " " + user.isEmailVerified());
            detailstxt.setText(" " + user.getUid());


            //  findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            //findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            statustxt.setText(" sign out ");
            detailstxt.setText(null);
/*
            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);*/
        }
    }


    @Override
    public void onClick(View v) {
        int position = v.getId();

        switch (position) {
            case R.id.signupbtn:
                // createAccount();
                signIn();
                break;
            case R.id.forget:
                sendEmailVerification();
                break;
        }

    }

}

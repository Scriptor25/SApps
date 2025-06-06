package io.scriptor.sapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;

import io.scriptor.sapps.FB;
import io.scriptor.sapps.databinding.ActivityLoginBinding;
import io.scriptor.sapps.model.UserModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FB.getAuth().getCurrentUser() != null) {
            onLoginComplete();
            return;
        }

        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.signInButton.setOnClickListener(this::onClickLogin);
        mBinding.forgotPassword.setOnClickListener(this::onClickForgotPassword);
        mBinding.signUp.setOnClickListener(this::onClickSignup);
        mBinding.googleLogin.setOnClickListener(this::onClickGoogle);
    }

    private void onLoginComplete() {
        final var intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickLogin(final View v) {
        final var email = mBinding.usernameEmail.getText().toString().trim();
        final var password = mBinding.password.getText().toString().trim();

        onLogin(email, password);
    }

    private void onLogin(final String email, final String password) {
        if (email.isEmpty()) {
            message("Please enter your email address");
            return;
        }
        if (password.isEmpty()) {
            message("Please enter your password");
            return;
        }

        FB.getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this::onCompleteEmailLogin);
    }

    private void onCompleteEmailLogin(final Task<AuthResult> task) {
        if (task.isSuccessful()) {
            onLoginComplete();
        } else {
            message("Failed to log into account");
        }
    }

    private void onClickForgotPassword(final View v) {
        final var email = mBinding.usernameEmail.getText().toString().trim();

        onForgotPassword(email);
    }

    private void onForgotPassword(final String email) {
        if (email.isEmpty()) {
            message("Please enter your email address");
            return;
        }

        FB.getAuth()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(this::onCompleteSendPasswordResetEmail);
    }

    private void onCompleteSendPasswordResetEmail(final Task<Void> task) {
        if (task.isSuccessful()) {
            message("Successfully sent password reset email");
        } else {
            message("Failed to send password reset email");
        }
    }

    private void onClickSignup(final View v) {
        final var email = mBinding.usernameEmail.getText().toString().trim();
        final var password = mBinding.password.getText().toString().trim();

        onSignup(email, password);
    }

    private void onSignup(final String email, final String password) {
        if (email.isEmpty()) {
            message("Please enter a valid email address");
            return;
        }
        if (password.isEmpty()) {
            message("Please enter a valid password");
            return;
        }

        FB.getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this::onCompleteEmailSignup);
    }

    private void onCompleteEmailSignup(final Task<AuthResult> task) {
        if (task.isSuccessful()) {
            var user = new UserModel();
            user.uid = FB.getAuth().getCurrentUser().getUid();
            FB.getDatabase().getReference("users").child(user.uid).setValue(user);

            onLoginComplete();
        } else {
            message("Failed to create new account");
        }
    }

    private void onClickGoogle(final View v) {
        message("Google sign in not yet supported");
    }

    private void message(final String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}

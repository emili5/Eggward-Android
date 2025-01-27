package com.example.eggward.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eggward.R;
import com.example.eggward.Schedule.ScheduleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText inputEmail;
    private EditText inputPassword;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.edittext_email);
        inputPassword = (EditText) findViewById(R.id.edittext_password);

        inputEmail.setOnFocusChangeListener(editTextListener);
        inputPassword.setOnFocusChangeListener(editTextListener);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.v("btn", "clicked");
                        View focusedView = getCurrentFocus();
                        if (focusedView == null)
                            focusedView = new View(null);
                        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);

                        if (!inputEmail.getText().toString().equals("") && !inputPassword.getText().toString().equals("")) {
                            // 이메일과 비밀번호가 공백이 아닌 경우
                            signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
                        } else {
                            // 이메일과 비밀번호가 공백인 경우
                            Toast.makeText(getApplicationContext(), "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    View.OnFocusChangeListener editTextListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    };

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("log-in", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                            intent.putExtra("userEmail", email);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("log-in", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
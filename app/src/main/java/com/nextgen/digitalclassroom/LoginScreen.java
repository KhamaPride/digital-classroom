package com.nextgen.digitalclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    Button signIn;
    TextView signUp, forgotPass;
    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        //find views by id
        signIn = findViewById(R.id.signInBtn);
        signUp = findViewById(R.id.sign_up);
        forgotPass = findViewById(R.id.forgot_password);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        //set an on click listener to direct user to other activities
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);

        //initializing firebase database
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_up) {
            Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
            startActivity(intent);
        } else if (view.getId() == R.id.forgot_password) {
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(intent);
        } else if (view.getId() == R.id.signInBtn) signinUser();
    }

    private void signinUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email field is empty");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter valid email address");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editTextPassword.setError("Password of at least 8 characters is required");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // redirect user to home screen
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginScreen.this, Home.class));

                        }
                        else {
                        Toast.makeText(LoginScreen.this,"User sign up failed! Please try again.",Toast.LENGTH_LONG).show();
                    }
                }
    });
    }
}
package com.nextgen.digitalclassroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignupScreen extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUserName, editTextEmail, editTextPassword, editTextConfirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        mAuth = FirebaseAuth.getInstance();

        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(this);

        TextView signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirm_pass);
        editTextUserName = (EditText) findViewById(R.id.username);

    }

    @Override
    public void onClick(View view) {
          if (view.getId() == R.id.signUpBtn) {
              signUp();
          }

         else if (view.getId() == R.id.signIn) {
            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(intent);
        }
    }

    private void signUp() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editTextUserName.getText().toString().trim();
        String confirm_pass = editTextConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUserName.setError("Full name is required");
            editTextUserName.requestFocus();
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
        if (email.isEmpty()) {
            editTextEmail.setError("Email field is empty");
            editTextEmail.requestFocus();
            return;
        }
        if (!password.equals(confirm_pass)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextPassword.requestFocus();
            editTextConfirmPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter valid email address");
            editTextEmail.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {

                                        Toast.makeText(SignupScreen.this, "User has been registered", Toast.LENGTH_LONG).show();

                                    }

                                    else{
                                        Toast.makeText(SignupScreen.this,"User sign up failed! Please try again.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            ;
                        }
                    }
                });
    }
}


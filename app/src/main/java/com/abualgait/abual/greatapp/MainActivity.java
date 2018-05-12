package com.abualgait.abual.greatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogin = (Button) findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError("at least 6 digit ");
                                    } else {
                                        Toast.makeText(MainActivity.this, "some error", Toast.LENGTH_LONG).show();
                                    }
                                } else {


                                    Toast.makeText(MainActivity.this,
                                            "Welcome " + auth.getInstance()
                                                    .getCurrentUser()
                                                    .getEmail(),
                                            Toast.LENGTH_LONG)
                                            .show();


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("email", auth.getInstance()
                                            .getCurrentUser()
                                            .getEmail().toString());
                                    editor.putString("uid", auth.getInstance().getCurrentUser().getUid().toString());
                                    editor.apply();


                                    // Load chat room contents
                                    startActivity(new Intent(MainActivity.this, Profile.class));

                                }
                            }
                        });
            }
        });
    }


}

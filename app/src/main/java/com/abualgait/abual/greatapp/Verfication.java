package com.abualgait.abual.greatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verfication extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private EditText code1, code2, code3, code4, code5, code6;
    Button verfiyBtn;


    String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool); // get the reference of Toolbar
        toolbar.setTitle("VERIFICATION");

        // set Title for Toolbar
        //toolbar.setLogo(R.drawable.android); // set logo for Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        verfiyBtn = (Button) findViewById(R.id.verify);
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);


        verfiyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code1Str = code1.getText().toString();
                String code2Str = code2.getText().toString();
                String code3Str = code3.getText().toString();
                String code4Str = code4.getText().toString();
                String code5Str = code5.getText().toString();
                String code6Str = code6.getText().toString();

                String code = code1Str + code2Str + code3Str + code4Str + code5Str + code6Str;


                String verficationId = null;
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    verficationId = bundle.getString("id");
                }

                verifyPhoneNumberWithCode(verficationId, code);

            }
        });

     /*   mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override

            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);

                // signInWithPhoneAuthCredential(phoneAuthCredential);

            }


            @Override

            public void onVerificationFailed(FirebaseException e) {

                Log.w("JEJE", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    Log.d("JEJE", "INVALID REQUEST");

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    Log.d("JEJE", "Too many Request");

                }

            }


            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                super.onCodeSent(verificationId, forceResendingToken);
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("JEJE", "onCodeSent:" + verificationId);


                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

                // resendVerificationCode(numberStr, mResendToken);


            }

        };*/
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVERifier", "signInWithCredential:success");
                            startActivity(new Intent(Verfication.this, ChatRoom.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseAuth.getInstance().signOut();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                              /*  String number = null;
                                PhoneAuthProvider.ForceResendingToken token;
                                Intent intent = getIntent();
                                Bundle bundle = intent.getExtras();

                                if (bundle != null) {
                                    number = bundle.getString("number");
                                    token = (PhoneAuthProvider.ForceResendingToken) bundle.getString("token");
                                    String phone_number = "+2" + number;

                                    resendVerificationCode(phone_number, token);
                                }*/

                            }
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        String Formatted = "+2";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                Formatted + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*  mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verificaiton without
            //     user action.
            Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);

            // signInWithPhoneAuthCredential(phoneAuthCredential);

        }


        @Override

        public void onVerificationFailed(FirebaseException e) {

            Log.w("JEJE", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                Log.d("JEJE", "INVALID REQUEST");

            } else if (e instanceof FirebaseTooManyRequestsException) {

                Log.d("JEJE", "Too many Request");

            }

        }




        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            super.onCodeSent(verificationId, forceResendingToken);
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("JEJE", "onCodeSent:" + verificationId);


            mVerificationId = verificationId;
            mResendToken = forceResendingToken;

            // resendVerificationCode(numberStr, mResendToken);


        }

    };*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
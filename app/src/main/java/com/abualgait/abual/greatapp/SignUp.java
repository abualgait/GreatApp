package com.abualgait.abual.greatapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private EditText username, email, phone_number, password, re_password;
    private FirebaseAuth auth;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private ProgressBar progressBar;
    public static Uri selectedImage;
    private LinearLayout blur;
    private String usernameStr = null, emailStr = null, numberStr = null, passwordStr = null, repasswordStr = null;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    private String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;


    boolean isGranted = false;

    public static final int REQUEST_CODE = 1;
    private StorageReference mStorageRef;

    private boolean isImageUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool); // get the reference of Toolbar
        toolbar.setTitle("Sign UP"); // set Title for Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        auth = FirebaseAuth.getInstance();
        FloatingActionButton buttonLoadImage = findViewById(R.id.fabButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                ActivityCompat.requestPermissions(SignUp.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("imgStr", selectedImage.toString());
            editor.apply();

            ImageView imageView = (ImageView) findViewById(R.id.ivBackground);
            imageView.setImageURI(selectedImage);


            //  Toast.makeText(getApplicationContext(), selectedImage.toString(), Toast.LENGTH_SHORT).show();


        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageToFirebaseStorage(String useremail) {


        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("uploadedEmail", useremail);
        editor.apply();


        Log.d("email", FirebaseAuth.getInstance().getCurrentUser().getEmail().toString() + " ==? " + useremail);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Uri file = Uri.parse(prefs.getString("imgStr", null));//"No name defined" is the default value.selectedImage; //Uri.fromFile(new File(selectedImage.toString()));

        //"/My device/DCIM/Screenshots/20180502_132337.png"
        StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS + useremail  );//+ "." + getFileExtension(Uri.parse(prefs.getString("imgStr", null)
        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("uploadedurl", downloadUrl.toString());
                editor.apply();

                isImageUploaded = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                // ...
            }
        });

    }

   /* private void downloadImagefromFirebaseStorage() throws IOException {
        File localFile = File.createTempFile("images", "jpg");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        //Uri file = Uri.fromFile(new File(selectedImage.toString()));
        StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS + FirebaseAuth.getInstance().getCurrentUser().getEmail().toString() + "." + getFileExtension(Uri.parse(prefs.getString("imgStr", null))));

        riversRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                // ...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    isGranted = true;

                } else {
                    isGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(SignUp.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sighup_true_label, menu);
        return true;
    }

  /*  private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            phone_number.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    private void verifyPhoneNumberInit(String number) {
        PhoneNumberUtils.formatNumber(number);
        if (!validatePhoneNumber(number)) {
            return;
        }

        verifyPhoneNumber(number);

    }*/


    public void verifyPhone(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {

        String Formatted = "+2";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                Formatted + phoneNumber,        // Phone number to verify

                60,                 // Timeout duration

                TimeUnit.SECONDS,   // Unit of timeout

                this,               // Activity (for callback binding)

                mCallbacks);        // OnVerificationStateChangedCallback

    }

    public void bigCall() {

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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

                Intent i = new Intent(SignUp.this, Verfication.class);
                i.putExtra("id", mVerificationId);
                i.putExtra("number", numberStr);
                i.putExtra("token", String.valueOf(mResendToken));
                startActivity(i);

                // resendVerificationCode(numberStr, mResendToken);


            }

        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                // do someing
                progressBar = findViewById(R.id.progressBar);
                blur = findViewById(R.id.blur_layout);
                blur.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                username = findViewById(R.id.input_name);
                email = findViewById(R.id.input_email);
                phone_number = findViewById(R.id.input_number);
                password = findViewById(R.id.input_password);
                re_password = findViewById(R.id.re_input_password);


                usernameStr = username.getText().toString();
                emailStr = email.getText().toString();
                numberStr = phone_number.getText().toString();
                passwordStr = password.getText().toString();
                repasswordStr = re_password.getText().toString();


                // String code = "+2";
                /*
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.countryCodes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                        code =item;

                    }
                });*/


                if (TextUtils.isEmpty(usernameStr)) {
                    username.setError("Enter username!");
                    blur.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(emailStr)) {
                    email.setError("Enter email address !");
                    blur.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Enter email address !", Toast.LENGTH_SHORT).show();
                    break;
                }


                /*if (TextUtils.isEmpty(numberStr)) {
                    phone_number.setError("Enter phonenumber!");
                    blur.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "Enter phonenumber!", Toast.LENGTH_SHORT).show();
                    break;
                }*/

                if (TextUtils.isEmpty(passwordStr)) {
                    password.setError("Enter password !");
                    blur.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "Enter password !", Toast.LENGTH_SHORT).show();
                    break;
                }


                if (TextUtils.isEmpty(repasswordStr)) {
                    re_password.setError("reEnter password!");
                    blur.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "reEnter password!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!passwordStr.equals(repasswordStr)) {
                    blur.setVisibility(View.GONE);
                    password.setError("reEnter password!");
                    re_password.setError("reEnter password!");
                    //Toast.makeText(getApplicationContext(), "re-write same password", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (isGranted == false) {
                    progressBar.setVisibility(View.GONE);
                    blur.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "IMAGE IS MANDATORY", Toast.LENGTH_SHORT).show();
                    break;
                }


                //authenticate user
                auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            // there was an error
                            if (passwordStr.length() < 6) {
                                password.setError("at least 6 digit ");
                            } else {
                                Toast.makeText(SignUp.this, "some error", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                blur.setVisibility(View.GONE);
                            }
                        } else {

                            //verify
                            bigCall();
                            verifyPhone(numberStr, mCallBacks);
                            resendVerificationCode(numberStr, mResendToken);



                            /*SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("emailpicture", auth.getInstance()
                                    .getCurrentUser()
                                    .getEmail().toString());

                            editor.apply();
                            */

                            uploadImageToFirebaseStorage(auth.getInstance().getCurrentUser().getEmail().toString());

                            progressBar.setVisibility(View.GONE);
                            blur.setVisibility(View.GONE);
                            Toast.makeText(SignUp.this, "Sigh up successful", Toast.LENGTH_LONG).show();

                        }
                    }
                });


                break;

        }
        return true;
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
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

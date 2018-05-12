package com.abualgait.abual.greatapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Profile extends AppCompatActivity {
    Button button;
    FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    private CoordinatorLayout coordinatorLayout;
    CardView golbalCard, privateCard;
    ImageView profilepic;
    TextView xprofilepic;
    String url;
    String userID;

    private FirebaseListAdapter<ChatMessage> adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // url = prefs.getString("imgStr", null);
        profilepic = findViewById(R.id.userprofileimag);
        xprofilepic = findViewById(R.id.emailTitle);


        //profilepic.setImageURI(Uri.parse(url));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("GREAT CHAT");
        getSupportActionBar();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle("GREAT CHAT");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        //"/My device/DCIM/Screenshots/20180502_132337.png"
        String Curemail = prefs.getString("email", null);//"No name defined" is the default value.
        final String Curuid = prefs.getString("uid", null);
        Log.d("data", Curemail);
        Log.d("uid", Curuid);


        xprofilepic.setText(Curemail);

//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);


        golbalCard = findViewById(R.id.globalcard);
        golbalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, ChatRoom.class));
            }
        });


        privateCard = findViewById(R.id.privatecard);
        privateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AllUsers.class));
            }
        });


        //"/My device/DCIM/Screenshots/20180502_132337.png"
        ///url = prefs.getString("imgStr", null);//"No name defined" is the default value.
        //profilepic.setImageURI(Uri.parse(url));


        //StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS + useremail + "." + getFileExtension(Uri.parse(prefs.getString("imgStr", null))));
        StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS +
                prefs.getString("uploadedEmail", null) );//+ "." +
       // getFileExtension(Uri.parse(prefs.getString("imgStr", null)))


        Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(riversRef).into(profilepic);

    }

    public void downloadimage() {


    }
        public String getFileExtension (Uri uri){
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }

        public static Bitmap getResizedBitmap (Bitmap bm,int newHeight, int newWidth){
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);
            // RECREATE THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;

        }
    }

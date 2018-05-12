package com.abualgait.abual.greatapp;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChatRoom extends AppCompatActivity {
    FirebaseAuth auth;
    private FirebaseListAdapter<ChatMessage> adapter;
    //String url =getIntent().getStringExtra("EXTRA_SESSION_ID");// "/My device/DCIM/Screenshots/20180502_132337.png";//
    String url;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private StorageReference mStorageRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            auth.getInstance().signOut();

            Toast.makeText(ChatRoom.this, "You have been signed out.", Toast.LENGTH_LONG).show();

            // Close activity
            finish();

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool); // get the reference of Toolbar

        mStorageRef = FirebaseStorage.getInstance().getReference();
        toolbar.setTitle("Chat Room"); // set Title for Toolbar
        //toolbar.setLogo(R.drawable.android); // set logo for Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        //"/My device/DCIM/Screenshots/20180502_132337.png"
        url = prefs.getString("uploadedurl", null);//"No name defined" is the default value.


        displayChatMessages();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                // Toast.makeText(getApplicationContext(), "URL" + url, Toast.LENGTH_SHORT).show();
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseUser user = auth.getCurrentUser();
                String userId = user.getUid();
                FirebaseDatabase.getInstance().getReference().push().
                        setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), url));

                // Clear the input
                input.setText("");
                displayChatMessages();
            }
        });


    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
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

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /*  private void downloadImagefromFirebaseStorage() throws IOException {
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
      }
  */
    public void displayChatMessages() {
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        listOfMessages.setAdapter(null);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {


                // if (model.getMessageUser().equals("abualgaitad@gmail.com")) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                ImageView messageImage = (ImageView) v.findViewById(R.id.message_image);
                //Toast.makeText(getApplicationContext(), "retrieved: " + model.getMessageImgurl(), Toast.LENGTH_SHORT).show();
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // messageImage.setImageURI(null);


                ///////////////
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS + model.getMessageUser());//+ "."
                //+ getFileExtension(Uri.parse(prefs.getString("imgStr", null)))


                Glide.with(v.getContext()).using(new FirebaseImageLoader()).load(riversRef).into(messageImage);


                ///////////////

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

                //} else {
                //      adapter.getRef(position).removeValue();
                // }
            }
        };

        listOfMessages.setAdapter(adapter);
    }

}
               /* messageImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), Uri.parse(model.getMessageImgurl()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Bitmap bmResized = getResizedBitmap(bitmap, 40, 40);
                messageImage.setImageBitmap(bmResized);*/
//messageImage.setImageURI(Uri.parse(model.getMessageImgurl()));
//Toast.makeText(getApplicationContext(), model.getMessageImgurl(), Toast.LENGTH_SHORT).show();
/*
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url

                    Uri photoUrl = user.getPhotoUrl();
                    messageImage.setImageURI(photoUrl);
                }*/
// Format the date before showing it
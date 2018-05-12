package com.abualgait.abual.greatapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> albumList;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new UserAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareAlbums();


    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

       /* User a = new User("habualgait@gmail.com", "habualgait@gmail.com");
        albumList.add(a);


        a = new User("qyabualgait@gmail.com", "qyabualgait@gmail.com ");
        albumList.add(a);


        a = new User("newabualgait@gmail.com", "newabualgait@gmail.com");
        albumList.add(a);


        a = new User("yabualgait@gmail.com", "yabualgait@gmail.com");
        albumList.add(a);*/
       // FirebaseUser user = auth.getCurrentUser();

        //String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();//.child(userId);


        //myRef.setValue("Hello, World!");

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("greatapp-48c11");
        // Read from the database
        //User a;
        myRef.addValueEventListener(new ValueEventListener() {
            User a;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DatabaseReference myRef = dataSnapshot.getChildren();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {


                    ChatMessage message = ds.getValue(ChatMessage.class);
                    a = new User(message.getMessageUser(), message.getMessageUser());
                    albumList.add(a);


                    /*String key = ds.getKey();
                    String value = ds.getValue(String.class);
                    Log.d("TAG", key + value);*/
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
       /* FirebaseDatabase.getInstance().getReference().child("greatapp-48c11").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage user = snapshot.getValue(ChatMessage.class);

                    User a = new User(user.getMessageUser(), user.getMessageUser());
                    albumList.add(a);

                    //System.out.println(user.getMessageUser());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
*/

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
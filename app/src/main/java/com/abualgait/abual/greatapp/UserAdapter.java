package com.abualgait.abual.greatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> userList;
    public View v;

    private StorageReference mStorageRef;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public ImageView profile;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.userprofile);
            email = (TextView) view.findViewById(R.id.useremail);

            v = view;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, PrivateChatRoom.class);
                    i.putExtra("theemail", email.getText().toString());
                    mContext.startActivity(i);


                }
            });
        }

    }


    public UserAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.email.setText(user.getEmail());
       // Glide.with(mContext).load(R.drawable.icicon).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(holder.profile);
        //Glide.with(mContext).load(riversRef).into(holder.profile);
        StorageReference riversRef = mStorageRef.child(Uploads.STORAGE_PATH_UPLOADS + user.getEmail());
       Glide.with(mContext).using(new FirebaseImageLoader()).load(riversRef).into(holder.profile);


    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
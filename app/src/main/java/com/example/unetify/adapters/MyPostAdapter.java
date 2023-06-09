package com.example.unetify.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unetify.R;
import com.example.unetify.activities.PostDetailActivity;
import com.example.unetify.models.Like;
import com.example.unetify.models.Post;
import com.example.unetify.providers.AuthProvider;
import com.example.unetify.providers.LikeProvider;
import com.example.unetify.providers.PostProvider;
import com.example.unetify.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();

        holder.mTextViewTitle.setText(post.getTitle().toUpperCase());
        if (post.getIdUser().equals(mAuthProvider.getUid())){
            holder.mImageViewDelete.setVisibility(View.VISIBLE);
        }else {
            holder.mImageViewDelete.setVisibility(View.GONE);
        }

        if (post.getImage() != null){
            if (!post.getImage().isEmpty()){
                Picasso.with(context).load(post.getImage()).into(holder.mCircleImagePost);
            }
        }
        holder.mViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

        holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelete(postId);
            }
        });

    }

    private void showConfirmDelete(String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar publicación")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "El post se elimino correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No se pudo eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewTitle;
        CircleImageView mCircleImagePost;
        ImageView mImageViewDelete;
        View mViewHolder;

        public ViewHolder (View view){
            super(view);
            mTextViewTitle = view.findViewById(R.id.textViewTitleMyPost);
            mCircleImagePost = view.findViewById(R.id.circleImageMyPost);
            mImageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            mViewHolder = view;

        }
    }
}

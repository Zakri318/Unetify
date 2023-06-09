package com.example.unetify.providers;

import com.example.unetify.models.Comment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommentProvider {

    CollectionReference mCollection;

    public CommentProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment){
        return mCollection.document().set(comment);
    }

    public Query getCommentByPost(String idPost) {
        return mCollection.whereEqualTo("idPost", idPost);
    }
}

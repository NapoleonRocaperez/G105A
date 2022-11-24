package com.example.g105.providers;

import com.example.g105.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProviders {

    private CollectionReference mColletion;

    public UsersProviders() {
        mColletion=FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return mColletion.document(id).get();
    }

    public Task<Void> create(User user){
        return mColletion.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());

        return mColletion.document(user.getId()).update(map);
    }
}
